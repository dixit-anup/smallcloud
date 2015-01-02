package com.mamascode.smallcloud.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.mamascode.smallcloud.model.Article;
import com.mamascode.smallcloud.model.ArticleUpload;
import com.mamascode.smallcloud.repository.ArticleDao;
import com.mamascode.smallcloud.repository.FileUploadDao;
import com.mamascode.smallcloud.utils.ListHelper;

@Service
public class ArticleServiceImpl implements ArticleService {
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// Article DAO
	
	private @Autowired ArticleDao articleDao;
	private @Autowired FileUploadDao fileUploadDao;
	
	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}
	
	public void setFileUploadDao(FileUploadDao fileUploadDao) {
		this.fileUploadDao = fileUploadDao;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// transaction manager
	
	@Autowired private PlatformTransactionManager transactionManager;
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// implemented methods

	/******* writeArticle *******/
	@Override
	public int writeArticle(Article article, List<MultipartFile> uploadFiles, String webRootPath) {
		int uploadDaoSuccessCount = 0;
		int fileUploadSuccessCount = 0;
		List<String> fileNames = new ArrayList<String>();
		
		// transaction boundary setting(manual) 
		// ****** when unexpected exception(ex, sql exception) is thrown,
		// ****** then DB roll back by @Transactional Declaration (in Article Service)
		
		// ****** default setting(PROPAGATION_REQUIRED, ISOLATION_DEFAULT, TIMEOUT_DEFAULT, readOnly=false)
		TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
		
		// set a parent id if it exists
		if(article.getParentId() != 0) {
			Article parent = articleDao.get(article.getParentId());
			article.setLevel((short) (parent.getLevel() + 1));
		}
		
		// write article
		int articleId = articleDao.write(article);
		
		if(articleId == 0) {
			// something wrong! roll-back
			transactionManager.rollback(transactionStatus);
			return articleId;
		}
		
		if(uploadFiles != null && uploadFiles.size() != 0) {
			for(MultipartFile uploadFile : uploadFiles) {
				int uploadId = 0;
				
				ArticleUpload upload = new ArticleUpload();
				upload.setArticleId(articleId);
				upload.setFileName(uploadFile.getOriginalFilename());
				
				if((uploadId = fileUploadDao.upload(upload)) != 0)
					uploadDaoSuccessCount++;
				
				try {
					// upload a file
					StringBuilder builder = new StringBuilder();
					builder.append(webRootPath).append("/user_files/").append(articleId)
					.append(uploadId).append("_").append(upload.getFileName());
					
					String filePath = builder.toString();
					fileNames.add(filePath);
					
					File file = new File(filePath);
					FileUtils.writeByteArrayToFile(file, uploadFile.getBytes());
					fileUploadSuccessCount++;
				} catch (IOException e) {
					// file upload failed: database roll-back
					transactionManager.rollback(transactionStatus);
					
					// and delete files uploaded
					cleanUploadedFiles(fileUploadSuccessCount, fileNames);
				}
			}
		}
		
		if(uploadDaoSuccessCount != uploadFiles.size()) {
			// db error: roll-back
			transactionManager.rollback(transactionStatus);
			cleanUploadedFiles(fileUploadSuccessCount, fileNames);
			return 0;
		}
		
		// all success! commit
		transactionManager.commit(transactionStatus);
		return articleId;
	}
	
	private void cleanUploadedFiles(int fileUploadSuccessCount, List<String> fileNames) {
		
		for(int i = 0; i < fileUploadSuccessCount; i++) {
			File file = new File(fileNames.get(i));
			file.delete();
		}
	}
	
	/******* setArticle *******/
	@Override
	public boolean setArticle(Article article) {
		int result = articleDao.update(article);
		
		if(result == 1)
			return true;
		
		return false;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	
	/******* deleteArticle *******/
	@Override
	public boolean deleteArticle(int articleId) {
		// transaction boundary setting(manual) 
		// ****** when unexpected exception(ex, sql exception) is thrown,
		// ****** then DB roll back by @Transactional Declaration (in Article Service)
		
		// ****** default setting(PROPAGATION_REQUIRED, ISOLATION_DEFAULT, TIMEOUT_DEFAULT, readOnly=false)
		TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
		
		// 1. check if there are children of this article
		if(articleDao.getChildCount(articleId) > 0) {
			// then, mask this article and return
			int maskingResult = articleDao.masking(articleId);
			
			if(maskingResult == 1) {
				// end. commit and return
				transactionManager.commit(transactionStatus);
				return true;
			} else {
				// something wrong, transaction roll back and return false
				transactionManager.rollback(transactionStatus);
				return false;
			}
		}
		
		// 2. check if this article has a masked parent
		int parentId = articleDao.getParent(articleId).getArticleId();
		int currentId = articleId;	
		
		while(hasMaskedParentArticle(currentId)) {
			if(articleDao.getChildCount(parentId) > 1) {
				// if there are siblings of this article,
				// then delete this article and return
				int deleteResult = articleDao.delete(currentId);
				
				if(deleteResult == 1) {
					// end. commit and return
					transactionManager.commit(transactionStatus);
					return true;
				} else {
					// something wrong, transaction roll back and return false
					transactionManager.rollback(transactionStatus);
					return false;
				}
			} else {
				int deleteResult = articleDao.delete(currentId);
				
				if(deleteResult != 1) {
					// something wrong, transaction roll back and return false
					transactionManager.rollback(transactionStatus);
					return false;
				}
				
				// reset parentId and currentId and proceed loop
				int currentParentId = parentId;
				parentId = articleDao.getParent(currentParentId).getArticleId();
				currentId = currentParentId;
			}
		}
		
		// delete article: this article may be a current article 
		// or top masked parent article
		int deleteResult = articleDao.delete(currentId);
		
		if(deleteResult == 1) {
			// end. commit and return
			transactionManager.commit(transactionStatus);
			return true;
		} else {
			// something wrong, transaction roll back and return false
			transactionManager.rollback(transactionStatus);
			return false;
		}
	}
	
	// internal method: hasMaskedParentArticle
	private boolean hasMaskedParentArticle(int articleId) {
		if(articleId == 0) // empty article
			return false;
		
		Article parentArticle = articleDao.getParent(articleId);
		
		if(parentArticle.getArticleId() == 0) // no parent
			return false;
		
		return parentArticle.isMask();
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	/******* getArticle *******/
	@Override
	public Article getArticle(int articleId) {
		return articleDao.get(articleId);
	}
	
	/******* getArticles *******/
	@Override
	public ListHelper<Article> getArticles(int page, int articlePerPage) {
		int totalCount = articleDao.getChildCount(0);
		
		ListHelper<Article> listHelper = new ListHelper<Article>(
				totalCount, page, articlePerPage);
		
		List<Article> articleList = articleDao.getChildArticles(
				0, listHelper.getOffset(), listHelper.getObjectPerPage(), true);
		
		for(Article article : articleList) {
			int childCount = articleDao.getChildCount(article.getArticleId());
			article.setChildCount(childCount);
		}
		
		listHelper.setList(articleList);
		
		return listHelper;
	}
	
	/******* checkPassword *******/
	@Override
	public boolean checkPassword(int articleId, String password) {
		return articleDao.checkPassword(articleId, password) == 1 ? true : false;
	}
	
	/******* getAncestorId *******/
	@Override
	public int getAncestorId(int articleId) {
		Article parent = articleDao.getParent(articleId);
		int parentId = 0;
		
		while(parent != null && parent.getArticleId() != 0) {
			parentId = parent.getArticleId();
			parent = articleDao.getParent(parentId);
		}
		
		return parentId;
	}
	
	///////////////////////////////////////////////////////////////////////////////

	/******* searchArticles *******/
	@Override
	public ListHelper<Article> searchArticles(int page, int articlePerPage,
			String keyword, int searchby) {
		int totalCount = articleDao.getCount(keyword, searchby);
		ListHelper<Article> listHelper = new ListHelper<Article>(totalCount, page, articlePerPage);
		List<Article> articleList = articleDao.searchArticles(
				keyword, searchby, listHelper.getOffset(), listHelper.getLimit());
		
		listHelper.setList(articleList);
		
		return listHelper;
	}
	
	/******* getSearchTitle *******/
	@Override
	public List<String> getSearchTitle(String keyword, int keywordPerPage) {
		return articleDao.getSearchTitle(keyword, 0, keywordPerPage);
	}
	
	///////////////////////////////////////////////////////////////////////////////

	/******* getChildArticles *******/
	@Override
	public List<Article> getChildArticles(int articleId) {
		List<Article> children = articleDao.getChildArticles(articleId, false);
		List<Article> returnList = new ArrayList<Article>();
		
		for(Article article : children) {
			returnList.add(article);
			getChildArticlesInternal(article.getArticleId(), returnList);
		}
		
		return returnList;
	}
	
	// internal method: getChildArticlesInternal
	private void getChildArticlesInternal(int articleId, List<Article> articleList) {
		if(articleId == 0)
			return;
		
		List<Article> children = articleDao.getChildArticles(articleId, false);
		
		if(children == null || children.size() == 0)
			return;
		
		for(Article childArticle : children) {
			articleList.add(childArticle);
			getChildArticlesInternal(childArticle.getArticleId(), articleList);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean deleteUploadFile(int uploadId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUploadFileByArticleId(int articleId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ListHelper<ArticleUpload> getUploadFiles(int articleId) {
		// TODO Auto-generated method stub
		return null;
	}
}
