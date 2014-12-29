package com.mamascode.smallcloud.test.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mamascode.smallcloud.model.Article;
import com.mamascode.smallcloud.repository.ArticleDao;
import com.mamascode.smallcloud.repository.test.DaoTestSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/application-config.xml"})
public class MyBatisArticleDaoTest {
	@Autowired ArticleDao articleDao;
	@Autowired DaoTestSetup testSetup;
	
	int parentArticleId;
	Article article1;
	Article article2;
	Article article3;
	Article article4;
	
	Logger logger = LoggerFactory.getLogger(MyBatisArticleDaoTest.class);
	
	@Before
	public void setup() {
		article1 = new Article();
		article1.setArticleTitle("test1");
		article1.setArticleContent("this is a test");
		article1.setWriterName("hwang");
		article1.setPassword("1234");
		article1.setWriteIp("127.0.0.1");
		
		article2 = new Article();
		article2.setArticleTitle("test2");
		article2.setArticleContent("this is a test");
		article2.setWriterName("abeno miku");
		article2.setPassword("1234");
		article2.setWriteIp("127.0.0.1");
		article2.setHomepage("http://myhome.com");
		
		article3 = new Article();
		article3.setArticleTitle("test3");
		article3.setArticleContent("this is a test");
		article3.setWriterName("suzy");
		article3.setPassword("1234");
		article3.setWriteIp("127.0.0.1");
		article3.setLevel((short) 3);
		
		article4 = new Article();
		article4.setArticleTitle("test4");
		article4.setArticleContent("this is a test");
		article4.setWriterName("iu");
		article4.setPassword("1234");
		article4.setWriteIp("127.0.0.1");
		article4.setLevel((short) 3);
	}
	
	//@Test
	public void insertTest() throws InterruptedException {
		testSetup.deleteAllArticle();
		insertInternal();
	}
	
	private void insertInternal() throws InterruptedException {
		assertThat(articleDao.write(article1), is(1));
		assertThat(articleDao.getCount(), is(1));
		
		parentArticleId = articleDao.getMaxArticleId();
		
		article3.setParentId(parentArticleId);
		article4.setParentId(parentArticleId);
		
		Thread.sleep(1000);
		
		assertThat(articleDao.write(article2), is(1));
		assertThat(articleDao.getCount(), is(2));
		
		Thread.sleep(1000);
		
		assertThat(articleDao.write(article3), is(1));
		assertThat(articleDao.getCount(), is(3));
		
		Thread.sleep(1000);
		
		assertThat(articleDao.write(article4), is(1));
		assertThat(articleDao.getCount(), is(4));
	}
	
	//@Test
	public void updateTest() {
		testSetup.deleteAllArticle();
		
		assertThat(articleDao.write(article1), is(1));
		assertThat(articleDao.getCount(), is(1));
		
		int articleId = articleDao.getMaxArticleId();
		Article articleUpdate = articleDao.get(articleId);
		String title = "update test";
		String content = "this is a update test";
		
		articleUpdate.setArticleTitle(title);
		articleUpdate.setArticleContent(content);
		
		assertThat(articleDao.update(articleUpdate), is(1));
		
		Article articleGet = articleDao.get(articleId);
		assertThat(articleGet.getArticleTitle(), is(title));
		assertThat(articleGet.getArticleContent(), is(content));
		
		articleUpdate.setArticleId(0);
		articleUpdate.setArticleTitle("update test 2");
		assertThat(articleDao.update(articleUpdate), is(0));
	}
	
	//@Test
	public void deleteTest() {
		testSetup.deleteAllArticle();
		
		assertThat(articleDao.write(article1), is(1));
		assertThat(articleDao.getCount(), is(1));
		
		// masking test
		int articleId = articleDao.getMaxArticleId();
		
		assertThat(articleDao.masking(articleId), is(1));
		Article articleGet = articleDao.get(articleId);
		assertTrue(articleGet.isMask());
		
		// deleting test
		assertThat(articleDao.delete(articleId), is(1));
		assertThat(articleDao.getCount(), is(0));
	}
	
	//@Test
	public void searchCountTest() {
		testSetup.deleteAllArticle();
		
		try {
			insertInternal();
		} catch(Exception e) {
			
		}
		
		assertThat(articleDao.getCount("test", ArticleDao.SEARCH_TITLE), is(3));
		assertThat(articleDao.getCount("suzy", ArticleDao.SEARCH_WRITER_NAME), is(1));
		assertThat(articleDao.getCount("h", ArticleDao.SEARCH_WRITER_NAME), is(1));
		assertThat(articleDao.getCount("abe", ArticleDao.SEARCH_WRITER_NAME), is(1));
		
		assertThat(articleDao.getChildCount(parentArticleId), is(1));
	}
	
	//@Test
	public void selectListTest() {
		testSetup.deleteAllArticle();

		try {
			insertInternal();
		} catch(Exception e) {
			
		}
		
		List<Article> articles = articleDao.getArticles();
		assertThat(articles, is(notNullValue()));
		assertThat(articles.size(), is(4));
		
		printArticleList(1, "no bound", articles);
		
		articles = articleDao.getArticles(0, 1);
		assertThat(articles, is(notNullValue()));
		assertThat(articles.size(), is(1));
		
		printArticleList(2, "bound", articles);
		
		articles = articleDao.searchArticles("suzy", ArticleDao.SEARCH_WRITER_NAME, 0, 10);
		assertThat(articles, is(notNullValue()));
		assertThat(articles.size(), is(1));
		
		printArticleList(3, "search 1: keyword = suzy, searchby = writer name", articles);
		
		articles = articleDao.searchArticles("abe", ArticleDao.SEARCH_WRITER_NAME, 0, 10);
		assertThat(articles, is(notNullValue()));
		assertThat(articles.size(), is(1));
		
		printArticleList(4, "search 2: keyword = abe, searchby = writer name", articles);
		
		articles = articleDao.getChildArticles(parentArticleId, false);
		assertThat(articles, is(notNullValue()));
		assertThat(articles.size(), is(2));
		
		printArticleList(5, "child articles", articles);
	}
	
	@Test
	public void uploadTest() {
		testSetup.deleteAllArticle();

		try {
			insertInternal();
		} catch(Exception e) {
			
		}
		
		/*
		int articleId = articleDao.getMaxArticleId();
		ArticleUpload upload = new ArticleUpload();
		upload.setArticleId(articleId);
		upload.setFileName("test_file.jpg");
		
		assertThat(articleDao.upload(upload), is(1));
		assertThat(articleDao.getUploadCount(articleId), is(1));
		
		List<ArticleUpload> uploadList = articleDao.getUploads(articleId);
		for(ArticleUpload uploadGet : uploadList) {
			logger.info("#{} of article #{}: file name: {}", uploadGet.getUploadId(), 
					uploadGet.getArticleId(), uploadGet.getFileName());
			logger.info("");
		}
		
		assertThat(articleDao.deleteUploadByArticleId(articleId), is(1));
		assertThat(articleDao.getUploadCount(articleId), is(0));
		*/
	}
	
	private void printArticleList(int testNo, String testTitle, List<Article> articleList) {
		logger.info("----------------------------------------------------");
		logger.info("#{} {}", testNo, testTitle);
		logger.info("----------------------------------------------------");
		
		for(Article article: articleList) {
			printArticle(article);
		}
		
		logger.info("----------------------------------------------------");
	}
	
	private void printArticle(Article article) {
		logger.info("\t[{}] {}", 
				article.getArticleId(), article.getArticleTitle());
		logger.info("\twriter: {}, time: {}", 
				article.getWriterName(), article.getWriteTime());
		logger.info("\t{}", 
				article.getArticleContent());
		logger.info("\thomepage: {}, ip: {}", 
				article.getHomepage(), article.getWriteIp());
		logger.info("\tparent: {}, level: {}", 
				article.getParentId(), article.getLevel());
		logger.info("");
	}
	
}
