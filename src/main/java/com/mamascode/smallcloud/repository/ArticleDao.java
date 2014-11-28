package com.mamascode.smallcloud.repository;

import java.util.List;

import com.mamascode.smallcloud.model.Article;
import com.mamascode.smallcloud.model.ArticleUpload;

public interface ArticleDao {
	///////////////////////////////////////////////////////////////////////////////
	
	final static int SEARCH_ALL = 0;
	final static int SEARCH_TITLE = 1;
	final static int SEARCH_WRITER_NAME = 2;
	final static int SEARCH_CONTENT = 3;
	
	///////////////////////////////////////////////////////////////////////////////
	
	int write(Article article);
	int update(Article article);
	int delete(int articleId);
	int masking(int articleId);
	
	///////////////////////////////////////////////////////////////////////////////
	
	int getCount();
	int getCount(String keyword, int searchby);
	int getChildCount(int parentArtilceId);
	
	Article get(int articleId);
	
	Article getParent(int articleId);
	
	List<Article> getArticles();
	List<Article> getArticles(int offset, int limit);
	List<Article> searchArticles(String keyword, int searchby, int offset, int limit);
	List<Article> getChildArticles(int parentArticleId, boolean reverse);
	List<Article> getChildArticles(int parentArticleId, int offset, int limit, boolean reverse);
	
	///////////////////////////////////////////////////////////////////////////////
	
	int upload(ArticleUpload upload);
	int deleteUpload(int uploadId);
	int deleteUploadByArticleId(int articleId);
	
	int getUploadCount(int articleId);
	List<ArticleUpload> getUploads(int articleId);
	
	///////////////////////////////////////////////////////////////////////////////
	
	int getMaxArticleId();
}
