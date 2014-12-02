package com.mamascode.smallcloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.mamascode.smallcloud.model.Article;
import com.mamascode.smallcloud.repository.ArticleDao;
import com.mamascode.smallcloud.service.ArticleService;
import com.mamascode.smallcloud.utils.ListHelper;

@Controller
@SessionAttributes({"article"})
@RequestMapping("/")
public class ArticleController {
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	// service interface
	@Autowired private ArticleService articleService;

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	
	@Value("${application.maxArticleLevel}")
	private int MAX_ARTICLE_LEVEL;
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	// handler methods
	
	@RequestMapping(value={"/", "/list"}, method=RequestMethod.GET)
	public String articleList(@RequestParam(value="page", required=true, defaultValue="1") int page,
			Model model) {
		ListHelper<Article> articleListHelper = articleService.getArticles(page, 10);
		model.addAttribute("articleListHelper", articleListHelper);
		
		return "articleList";
	}
	
	@RequestMapping(value="/read/{articleId}", method=RequestMethod.GET)
	public String readArticle(@PathVariable int articleId, Model model) {
		Article article = articleService.getArticle(articleId);
		model.addAttribute("article", article);
		
		return "readArticle";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String writeArticleForm(
			@RequestParam(value="parentId", required=true, defaultValue="0") int parentId,
			Model model,  HttpServletRequest request) {
		
		Article article = new Article(); // new article object
		article.setWriteIp(request.getRemoteAddr());
		
		if(parentId != 0) {
			// parentId parameter check
			Article parent = articleService.getArticle(parentId);
			
			if(parent != null && parent.getArticleId() != 0 && !parent.isMask() 
					&& parent.getLevel() + 1 <= MAX_ARTICLE_LEVEL) {
				article.setParentId(parentId); // valid parentId
				
				// put a original content to a article content
				String articleContent = (article.getArticleContent() != null &&
						!article.getArticleContent().equals("")) ? article.getArticleContent() : "";
				String modifiedContent = new StringBuilder().append(articleContent)
						.append("\n\n 원문 ===================== \n ")
						.append(parent.getArticleContent()).toString();
				article.setArticleContent(modifiedContent);
			} else
				return "redirect:/"; // invalid: redirect home
		}
		
		model.addAttribute("article", article);
		
		return "writeArticle";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String writeArticle(@ModelAttribute @Valid Article article,
			BindingResult bindingResult, SessionStatus sessionStatus, Model model) {
		int articleId = 0;
		
		// TODO: parameter filtering
		
		if(bindingResult.hasErrors() || (articleId = articleService.writeArticle(article)) == 0) {
			return "writeArticle";
		}
		
		sessionStatus.setComplete();
		return "redirect:/read/" + articleId;
	}
	
	@RequestMapping(value="/set/{articleId}", method=RequestMethod.GET)
	public String setArticleForm(@PathVariable int articleId, Model model) {
		
		Article article = articleService.getArticle(articleId);				
		model.addAttribute("article", article);
		
		return "setArticle";
	}
	
	@RequestMapping(value="/set/{articleId}", method=RequestMethod.POST)
	public String setArticle(@PathVariable int articleId, @ModelAttribute @Valid Article article,
			BindingResult bindingResult, SessionStatus sessionStatus) {
		// TODO: parameter filtering
		
		if(bindingResult.hasErrors() || !articleService.setArticle(article)) {
			return "setArticle";
		}
		
		sessionStatus.setComplete();
		return "redirect:/read/" + articleId;
	}
	
	@RequestMapping(value="/children/{parentId}", method=RequestMethod.GET)
	@ResponseBody
	public List<Article> getChildArticles(@PathVariable int parentId) {
		return articleService.getChildArticles(parentId);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public boolean deleteArticle(@RequestParam("articleId") int articleId) {
		return articleService.deleteArticle(articleId);
	}
	
	@RequestMapping(value="/check", method=RequestMethod.GET)
	@ResponseBody
	public boolean checkPassword(@RequestParam("articleId") int articleId,
			@RequestParam("password") String password) {		
		return articleService.checkPassword(articleId, password); 
	}
}
