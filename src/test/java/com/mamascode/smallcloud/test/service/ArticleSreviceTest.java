package com.mamascode.smallcloud.test.service;

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
import com.mamascode.smallcloud.service.ArticleService;
import com.mamascode.smallcloud.utils.ListHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/application-config.xml"})
public class ArticleSreviceTest {
	@Autowired ArticleService articleService;
	@Autowired ArticleDao articleDao;
	@Autowired DaoTestSetup testSetup;
	
	int parentArticleId;
	Article article1;
	Article article2;
	Article article3;
	Article article4;
	Article article5;
	Article article6;
	Article article7;
	Article article8;
	Article article9;
	Article article10;
	
	Logger logger = LoggerFactory.getLogger(ArticleSreviceTest.class);
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	@Before
	public void setup() {
		article1 = new Article();
		article1.setArticleTitle("test1");
		article1.setArticleContent("this is a test 1");
		article1.setWriterName("hwang");
		article1.setPassword("1234");
		article1.setWriteIp("127.0.0.1");
		
		article2 = new Article();
		article2.setArticleTitle("test2");
		article2.setArticleContent("this is a test 2");
		article2.setWriterName("abeno miku");
		article2.setPassword("1234");
		article2.setWriteIp("127.0.0.1");
		article2.setHomepage("http://myhome.com");
		
		article3 = new Article();
		article3.setArticleTitle("test3");
		article3.setArticleContent("this is a test 3");
		article3.setWriterName("배수지");
		article3.setPassword("1234");
		article3.setWriteIp("127.0.0.1");
		
		article4 = new Article();
		article4.setArticleTitle("test4");
		article4.setArticleContent("this is a test 4");
		article4.setWriterName("iu");
		article4.setPassword("1234");
		article4.setWriteIp("127.0.0.1");
		
		article5 = new Article();
		article5.setArticleTitle("test5");
		article5.setArticleContent("this is a test 5");
		article5.setWriterName("Kim-C");
		article5.setPassword("1234");
		article5.setWriteIp("127.0.0.1");
		
		article6 = new Article();
		article6.setArticleTitle("test6");
		article6.setArticleContent("this is a test 6");
		article6.setWriterName("태연");
		article6.setPassword("1234");
		article6.setWriteIp("127.0.0.1");
		
		article7 = new Article();
		article7.setArticleTitle("test7");
		article7.setArticleContent("this is a test 7");
		article7.setWriterName("윤아");
		article7.setPassword("1234");
		article7.setWriteIp("127.0.0.1");
		
		article8 = new Article();
		article8.setArticleTitle("test8");
		article8.setArticleContent("this is a test 8");
		article8.setWriterName("Yuna Kim");
		article8.setPassword("1234");
		article8.setWriteIp("127.0.0.1");
		
		article9 = new Article();
		article9.setArticleTitle("test9");
		article9.setArticleContent("this is a test 9");
		article9.setWriterName("삼돌이");
		article9.setPassword("1234");
		article9.setWriteIp("127.0.0.1");
		
		article10 = new Article();
		article10.setArticleTitle("test10");
		article10.setArticleContent("this is a test 10");
		article10.setWriterName("스톨만");
		article10.setPassword("1234");
		article10.setWriteIp("127.0.0.1");
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void totalTest() throws InterruptedException {
		testSetup.deleteAllArticle();
		insertInternal();
		
		/*
		ListHelper<Article> listHelper = articleService.getArticles(1, 10);
		List<Article> articleList = listHelper.getList();
		
		printArticleList(1, "article list", articleList);
				
		logger.info("----------------------------------------------------");
		
		int maxId = articleDao.getMaxArticleId();
		
		assertTrue(articleService.deleteArticle(maxId-7)); // article 3 deleting
		assertTrue(articleService.deleteArticle(maxId-8)); // article 2 deleting
		assertTrue(articleService.deleteArticle(maxId-3)); // article 7 deleting
		
		listHelper = articleService.getArticles(1, 10);
		articleList = listHelper.getList();
		
		printArticleList(2, "deleting test 1", articleList);
		
		assertTrue(articleService.deleteArticle(maxId-1)); // article 9 deleting
		assertTrue(articleService.deleteArticle(maxId-2)); // article 8 deleting
		
		listHelper = articleService.getArticles(1, 10);
		articleList = listHelper.getList();
		
		printArticleList(3, "deleting test 2", articleList);
		
		assertTrue(articleService.deleteArticle(maxId-4)); // article 6 deleting
		assertTrue(articleService.deleteArticle(maxId)); // article 10 deleting
		
		listHelper = articleService.getArticles(1, 10);
		articleList = listHelper.getList();
		
		printArticleList(4, "deleting test 3", articleList);
		*/
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	
	private void printChildren(Article article) {
		printArticle(article);
		
		if(article.getChildren() == null)
			return;
		
		for(Article children : article.getChildren())
			printChildren(children);
	}
	
	private void insertInternal() throws InterruptedException {
		assertTrue(articleService.writeArticle(article1) != 0);
		int maxId = articleDao.getMaxArticleId();
		
		Thread.sleep(500);
		
		article3.setParentId(maxId);
		//article3.setLevel((short) 2);
		article4.setParentId(maxId);
		//article4.setLevel((short) 2);
		article5.setParentId(maxId);
		//article5.setLevel((short) 2);
		
		assertTrue(articleService.writeArticle(article2) != 0);
		maxId = articleDao.getMaxArticleId();
		
		Thread.sleep(500);
		
		article6.setParentId(maxId);
		//article6.setLevel((short) 2);
		article7.setParentId(maxId);
		//article7.setLevel((short) 2);
		
		assertTrue(articleService.writeArticle(article3) != 0);
		Thread.sleep(500);
		
		assertTrue(articleService.writeArticle(article4) != 0);
		Thread.sleep(500);
		
		assertTrue(articleService.writeArticle(article5) != 0);
		Thread.sleep(500);
		
		assertTrue(articleService.writeArticle(article6) != 0);
		Thread.sleep(500);
		maxId = articleDao.getMaxArticleId();
		article10.setParentId(maxId);
		//article10.setLevel((short) 3);
		
		assertTrue(articleService.writeArticle(article7) != 0);
		Thread.sleep(500);
		maxId = articleDao.getMaxArticleId();
		
		article8.setParentId(maxId);
		//article8.setLevel((short) 3);
		article9.setParentId(maxId);
		//article9.setLevel((short) 3);
		
		assertTrue(articleService.writeArticle(article8) != 0);
		Thread.sleep(500);
		assertTrue(articleService.writeArticle(article9) != 0);
		Thread.sleep(500);
		assertTrue(articleService.writeArticle(article10) != 0);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	private void printArticleList(int testNo, String testTitle, List<Article> articleList) {
		logger.info("----------------------------------------------------");
		logger.info("#{} {}", testNo, testTitle);
		logger.info("----------------------------------------------------");
		
		for(Article article : articleList) {
			printArticle(article);
			List<Article> children = articleService.getChildArticles(article.getArticleId());
			for(Article child : children)
				printChildren(child);
		}
	}
	
	private void printArticle(Article article) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < article.getLevel(); i++)
			builder.append("    ");
		
		if(!article.isMask()) {
			builder.append(article.getArticleTitle()).append(" | ")
			.append(article.getWriterName()).append(" | ").append(article.getWriteTime());
		} else {
			builder.append("��젣��寃뚯떆臾쇱엯�덈떎");
		}
		
		logger.info("{}", builder.toString());
	}
}
