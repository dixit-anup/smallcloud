package com.mamascode.smallcloud.repository.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mamascode.smallcloud.exception.UpdateResultCountNotMatchException;
import com.mamascode.smallcloud.model.Article;
import com.mamascode.smallcloud.model.ArticleUpload;
import com.mamascode.smallcloud.repository.ArticleDao;

@Repository
public class MariaDBMyBatisArticleDao extends MyBatisDao implements ArticleDao {
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// SqlSessionTemplate
	@Autowired private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// constructors(default)
	
	@Autowired
	public MariaDBMyBatisArticleDao(SqlSessionTemplate sqlSessionTemplate) {
		super("ArticleMapper");
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// implemented methods
	
	/******* write *******/
	@Override
	public int write(Article article) {
		int result = sqlSessionTemplate.insert(getMapperId("insertArticle"), article);
		int lastInsertId = sqlSessionTemplate.selectOne(getMapperId("selectLastInsertArticleId"));
		
		if(result == 1)
			return lastInsertId;
		else
			throw new UpdateResultCountNotMatchException("write() in MariaDBMyBatisArticleDao result is not 1");
	}
	
	/******* update *******/
	@Override
	public int update(Article article) {
		int result = sqlSessionTemplate.update(getMapperId("updateArticle"), article);
		
		if(result == 1 || result == 0)
			return result;
		else
			throw new UpdateResultCountNotMatchException(
					"update() in MariaDBMyBatisArticleDao result does not match");
	}
	
	/******* delete *******/
	@Override
	public int delete(int articleId) {
		int result = sqlSessionTemplate.update(getMapperId("deleteArticle"), articleId);
		
		if(result == 1 || result == 0)
			return result;
		else
			throw new UpdateResultCountNotMatchException(
					"delete() in MariaDBMyBatisArticleDao result does not match");
	}
	
	/******* masking *******/
	@Override
	public int masking(int articleId) {
		int result = sqlSessionTemplate.update(getMapperId("maskArticle"), articleId);
		
		if(result == 1 || result == 0)
			return result;
		else
			throw new UpdateResultCountNotMatchException(
					"masking() in MariaDBMyBatisArticleDao result does not match");
	}
	
	// TODO [project memo] masking된 게시물은 마지막 답변 게시물이 삭제될 때 함께 삭제될 수 있도록(Service에서 처리)
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	/******* getCount *******/
	@Override
	public int getCount() {
		return sqlSessionTemplate.selectOne(getMapperId("selectCountDefault"));
	}
	
	/******* getCount: searching *******/
	@Override
	public int getCount(String keyword, int searchby) {
		// parameter
		Map<String, Object> hashmap = new HashMap<String, Object>();
		hashmap.put("keyword", keyword);
		hashmap.put("searchby", searchby);
		
		return sqlSessionTemplate.selectOne(getMapperId("selectCountSearch"), hashmap);
	}
	
	/******* getChildCount *******/
	@Override
	public int getChildCount(int parentArticleId) {
		return sqlSessionTemplate.selectOne(getMapperId("selectCountChild"), parentArticleId);
	}
	
	///////////////////////////////////////////////////////////////////////////////
	
	/******* get *******/
	@Override
	public Article get(int articleId) {
		Article article = sqlSessionTemplate.selectOne(
				getMapperId("selectArticleByArticleId"), articleId);
		
		if(article == null) {
			article = new Article();
		}
		
		return article;
	}
	
	/******* getParent *******/
	@Override
	public Article getParent(int articleId) {
		int parentId = get(articleId).getParentId();
		Article parentArticle = null;
		
		if(parentId != 0)
			parentArticle = get(parentId);
		else
			parentArticle = new Article();	// empty article
		
		return parentArticle;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	
	

	/******* getArticles *******/
	@Override
	public List<Article> getArticles() {
		return sqlSessionTemplate.selectList(getMapperId("selectArticleList"));
	}
	
	/******* getArticles: row bounds *******/
	@Override
	public List<Article> getArticles(int offset, int limit) {
		RowBounds rowBounds = new RowBounds(offset, limit);
		return sqlSessionTemplate.selectList(getMapperId("selectArticleList"), null, rowBounds);
	}
	
	/******* searchArticles *******/
	@Override
	public List<Article> searchArticles(String keyword, int searchby,
			int offset, int limit) {
		// parameter
		Map<String, Object> hashmap = new HashMap<String, Object>();
		hashmap.put("keyword", keyword);
		hashmap.put("searchby", searchby);
		
		// RowBounds
		RowBounds rowBounds = new RowBounds(offset, limit);
				
		return sqlSessionTemplate.selectList(getMapperId("selectArticlesSearch"), hashmap, rowBounds);
	}
	
	/******* getChildArticles *******/
	@Override
	public List<Article> getChildArticles(int parentArticleId) {
		return sqlSessionTemplate.selectList(getMapperId("selectChildArticleList"), parentArticleId);
	}
	
	/******* getChildArticles: bounded *******/
	@Override
	public List<Article> getChildArticles(int parentArticleId, int offset, int limit) {
		// RowBounds
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return sqlSessionTemplate.selectList(
				getMapperId("selectChildArticleList"), parentArticleId, rowBounds);
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	/******* upload *******/
	@Override
	public int upload(ArticleUpload upload) {
		int result = sqlSessionTemplate.insert(getMapperId("insertArticleUpload"), upload);
		
		if(result == 1)
			return result;
		else
			throw new UpdateResultCountNotMatchException(
					"upload() in MariaDBMyBatisArticleDao result does not match");
	}
	
	/******* deleteUpload *******/
	@Override
	public int deleteUpload(int uploadId) {
		int result = sqlSessionTemplate.delete(getMapperId("deleteArticleUpload"), uploadId);
		
		if(result == 1 || result == 0)
			return result;
		else
			throw new UpdateResultCountNotMatchException(
					"deleteUpload() in MariaDBMyBatisArticleDao result does not match");
	}
	
	/******* deleteUploadByArticleId *******/
	@Override
	public int deleteUploadByArticleId(int articleId) {
		return sqlSessionTemplate.delete(getMapperId("deleteArticleUploadByArticleId"), articleId);
	}
	
	/******* getUploadCount *******/
	@Override
	public int getUploadCount(int articleId) {
		return sqlSessionTemplate.selectOne(getMapperId("selectArticleUploadCount"), articleId);
	}
	
	/******* getUploads *******/
	@Override
	public List<ArticleUpload> getUploads(int articleId) {
		return sqlSessionTemplate.selectList(getMapperId("selectArtocleUploadList"), articleId);
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	@Override
	public int getMaxArticleId() {
		return sqlSessionTemplate.selectOne(getMapperId("selectMaxArticleId"));
	}
}
