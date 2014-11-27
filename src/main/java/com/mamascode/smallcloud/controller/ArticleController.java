package com.mamascode.smallcloud.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.mamascode.smallcloud.model.Article;
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
			Model model) {
		Article article = new Article(); // new article object
		
		if(parentId != 0) {
			// parentId parameter check
			Article parent = articleService.getArticle(parentId);
			
			if(parent != null && parent.getArticleId() != 0)
				article.setParentId(parentId); // valid parentId
			else
				return "redirect:/"; // invalid parentId: redirect home
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
}
