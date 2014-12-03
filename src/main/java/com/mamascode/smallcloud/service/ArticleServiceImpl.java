package com.mamascode.smallcloud.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mamascode.smallcloud.model.Article;
import com.mamascode.smallcloud.model.ArticleUpload;
import com.mamascode.smallcloud.repository.ArticleDao;
import com.mamascode.smallcloud.utils.ListHelper;

@Service
public class ArticleServiceImpl implements ArticleService {
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// Article DAO
	
	private @Autowired ArticleDao articleDao;
	
	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
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
	public int writeArticle(Article article) {
		if(article.getParentId() != 0) {
			Article parent = articleDao.get(article.getParentId());
			article.setLevel((short) (parent.getLevel() + 1));
		}
		
		return articleDao.write(article);
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
		// TODO Auto-generated method stub
		return null;
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
