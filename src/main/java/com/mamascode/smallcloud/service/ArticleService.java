package com.mamascode.smallcloud.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mamascode.smallcloud.model.Article;
import com.mamascode.smallcloud.model.ArticleUpload;
import com.mamascode.smallcloud.utils.ListHelper;

@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
public interface ArticleService {
	///////////////////////////////////////////////////////////////////////////////
	
	final static int MAX_UPLOAD_COUNT = 5;
	final static int MAX_FILE_SIZE = 1024 * 1024 * 20; // 20MB
	final static int NO_PAGE_LIMIT = -1;
	
	///////////////////////////////////////////////////////////////////////////////
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	int writeArticle(Article article);
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	boolean setArticle(Article article);
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	boolean deleteArticle(int articleId);
	
	///////////////////////////////////////////////////////////////////////////////
	
	Article getArticle(int articleId);
	ListHelper<Article> getArticles(int page, int articlePerPage);
	ListHelper<Article> searchArticles(int page, int articlePerPage, String keyword, int searchby);
	List<Article> getChildArticles(int articleId);
	
	boolean checkPassword(int articleId, String password);
	int getAncestorId(int articleId);
	List<String> getSearchTitle(String keyword, int keywordPerPage); 
	
	///////////////////////////////////////////////////////////////////////////////
		
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	boolean deleteUploadFile(int uploadId);
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	boolean deleteUploadFileByArticleId(int articleId);
	
	ListHelper<ArticleUpload> getUploadFiles(int articleId);
	
	///////////////////////////////////////////////////////////////////////////////
}
