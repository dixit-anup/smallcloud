package com.mamascode.smallcloud.repository.mybatis;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mamascode.smallcloud.exception.UpdateResultCountNotMatchException;
import com.mamascode.smallcloud.model.ArticleUpload;
import com.mamascode.smallcloud.repository.FileUploadDao;

@Repository
public class MariaDBMyBatisFileUploadDao extends MyBatisDao implements FileUploadDao {
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// SqlSessionTemplate
	@Autowired private SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// constructors
	
	@Autowired
	public MariaDBMyBatisFileUploadDao(SqlSessionTemplate sqlSessionTemplate) {
		super("FileUploadMapper");
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	// implemented methods
	
	/******* upload *******/
	@Override
	public int upload(ArticleUpload upload) {
		int result = sqlSessionTemplate.insert(getMapperId("insertArticleUpload"), upload);
		int lastInsertId = 0;
		
		if(result == 1) {
			lastInsertId = sqlSessionTemplate.selectOne(getMapperId("selectLastInsertUploadId"));
			return lastInsertId;
		} else
			throw new UpdateResultCountNotMatchException(
					"upload() in MariaDBMyBatisFileUploadDao result does not match");
	}
	
	/******* deleteUpload *******/
	@Override
	public int deleteUpload(int uploadId) {
		int result = sqlSessionTemplate.delete(getMapperId("deleteArticleUpload"), uploadId);
		
		if(result == 1 || result == 0)
			return result;
		else
			throw new UpdateResultCountNotMatchException(
					"deleteUpload() in MariaDBMyBatisFileUploadDao result does not match");
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
}
