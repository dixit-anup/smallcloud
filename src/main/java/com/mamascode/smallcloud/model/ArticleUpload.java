package com.mamascode.smallcloud.model;

public class ArticleUpload {
	private int uploadId;
	private int articleId;
	private String fileName;
	
	public int getUploadId() {
		return uploadId;
	}
	
	public void setUploadId(int uploadId) {
		this.uploadId = uploadId;
	}
	
	public int getArticleId() {
		return articleId;
	}
	
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
