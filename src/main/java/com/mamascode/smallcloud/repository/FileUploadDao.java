package com.mamascode.smallcloud.repository;

import java.util.List;

import com.mamascode.smallcloud.model.ArticleUpload;


public interface FileUploadDao {
	int upload(ArticleUpload upload);
	int deleteUpload(int uploadId);
	int deleteUploadByArticleId(int articleId);
	
	int getUploadCount(int articleId);
	List<ArticleUpload> getUploads(int articleId);
}
