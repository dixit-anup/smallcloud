package com.mamascode.smallcloud.model;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Article {
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private int articleId = 0;
	
	@NotNull
	@Size(min=1, max=50, message="제목은 50글자 이하로 작성해주세요. 제목은 필수입니다.")
	private String articleTitle;
	
	@NotNull
	@Size(min=1, max=50, message="내용은 50글자 이하로 작성해주세요. 내용은 필수입니다.")
	private String articleContent;
	
	@NotNull
	@Size(min=1, max=10, message="10글자 이하(필수)")
	private String writerName;
	
	@NotNull
	@Size(min=4, max=6, message="4~6글자(필수)")
	@Pattern(regexp="^[a-zA-Z0-9]+$", message="숫자,알파벳")
	private String password;
	
	private String homepage = "";
	private Timestamp writeTime;
	private int parentId = 0;
	private short level = 1;
	private String writeIp;
	private boolean mask;
	
	private List<Article> children;
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////	

	public int getArticleId() {
		return articleId;
	}
	
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	
	public String getArticleTitle() {
		return articleTitle;
	}
	
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	
	public String getArticleContent() {
		return articleContent;
	}
	
	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}
	
	public String getWriterName() {
		return writerName;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getHomepage() {
		return homepage;
	}
	
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	
	public Timestamp getWriteTime() {
		return writeTime;
	}
	
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	public int getParentId() {
		return parentId;
	}
	
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	public short getLevel() {
		return level;
	}
	
	public void setLevel(short level) {
		this.level = level;
	}

	public String getWriteIp() {
		return writeIp;
	}

	public void setWriteIp(String writeIp) {
		this.writeIp = writeIp;
	}

	public boolean isMask() {
		return mask;
	}

	public void setMask(boolean mask) {
		this.mask = mask;
	}
	
	public List<Article> getChildren() {
		return children;
	}

	public void setChildren(List<Article> children) {
		this.children = children;
	}
}
