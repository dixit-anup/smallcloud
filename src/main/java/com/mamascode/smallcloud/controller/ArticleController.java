package com.mamascode.smallcloud.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import org.springframework.web.multipart.MultipartFile;

import com.mamascode.smallcloud.model.Article;
import com.mamascode.smallcloud.repository.ArticleDao;
import com.mamascode.smallcloud.service.ArticleService;
import com.mamascode.smallcloud.utils.ListHelper;
import com.mamascode.smallcloud.utils.SecurityUtil;

@Controller
@SessionAttributes({"article"})
@RequestMapping("/")
public class ArticleController {
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////
	// service interface
	@Autowired private ArticleService articleService;

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	// constant variables
	
	@Value("${application.maxArticleLevel}")
	private int MAX_ARTICLE_LEVEL;
	
	@Value("${application.uploadRootPath}")
	private String WEB_ROOT_PATH;
	
	private final static int ARTICLE_PER_PAGE = 15; 
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////
	// handler methods
	
	/***** articleList *****/
	@RequestMapping(value={"/", "/list"}, method=RequestMethod.GET)
	public String articleList(@RequestParam(value="page", required=true, defaultValue="1") int page,
			@RequestParam(value="openedId", required=true, defaultValue="0") int openedId, 
			HttpSession session, Model model) {
		ListHelper<Article> articleListHelper = articleService.getArticles(page, ARTICLE_PER_PAGE);
		model.addAttribute("articleListHelper", articleListHelper);
		model.addAttribute("openedId", openedId);
		
		session.setAttribute("listPage", articleListHelper.getCurPageNumber());
		
		return "articleList";
	}
	
	/***** readArticle *****/
	@RequestMapping(value="/read/{articleId}", method=RequestMethod.GET)
	public String readArticle(@PathVariable int articleId, HttpSession session, Model model) {
		Article article = articleService.getArticle(articleId);
		int ancestorId = articleService.getAncestorId(articleId);
		
		model.addAttribute("article", article);
		model.addAttribute("ancestorId", ancestorId);
		
		if(session.getAttribute("listPage") != null && ((Integer) session.getAttribute("listPage")) != 1) {
			model.addAttribute("listPage", session.getAttribute("listPage"));
		}
		
		return "readArticle";
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	/***** writeArticleForm *****/
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
	
	/***** writeArticle *****/
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String writeArticle(@ModelAttribute @Valid Article article, HttpSession session,
			BindingResult bindingResult, SessionStatus sessionStatus,
			@RequestParam(value="uploadFiles", required=false) List<MultipartFile> uploadFiles, Model model) {
		int articleId = 0;
		
		// parameter filtering
		article.setArticleTitle(SecurityUtil.replaceScriptTag(article.getArticleTitle(), false));
		article.setArticleContent(SecurityUtil.replaceScriptTag(article.getArticleContent(), true));
		article.setWriterName(SecurityUtil.replaceScriptTag(article.getWriterName(), false));
		article.setHomepage(SecurityUtil.replaceScriptTag(article.getHomepage(), false));
		
		// TODO: check files
		
		if(bindingResult.hasErrors() || 
				(articleId = articleService.writeArticle(article, uploadFiles, WEB_ROOT_PATH)) == 0) {
			return "writeArticle";
		}
		
		sessionStatus.setComplete();
		
		if(article.getParentId() == 0) {
			session.setAttribute("listPage", 1);
		}
		
		return "redirect:/read/" + articleId;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	/***** setArticleForm *****/
	@RequestMapping(value="/set/{articleId}", method=RequestMethod.GET)
	public String setArticleForm(@PathVariable int articleId, Model model) {
		
		Article article = articleService.getArticle(articleId);				
		model.addAttribute("article", article);
		
		return "setArticle";
	}
	
	/***** setArticle *****/
	@RequestMapping(value="/set/{articleId}", method=RequestMethod.POST)
	public String setArticle(@PathVariable int articleId, @ModelAttribute @Valid Article article,
			BindingResult bindingResult, SessionStatus sessionStatus) {
		// parameter filtering
		article.setArticleTitle(SecurityUtil.replaceScriptTag(article.getArticleTitle(), false));
		article.setArticleContent(SecurityUtil.replaceScriptTag(article.getArticleContent(), true));
		article.setHomepage(SecurityUtil.replaceScriptTag(article.getHomepage(), false));
		
		if(bindingResult.hasErrors() || !articleService.setArticle(article)) {
			return "setArticle";
		}
		
		sessionStatus.setComplete();
		return "redirect:/read/" + articleId;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////
	// AJAX
	
	/***** getChildArticles *****/
	@RequestMapping(value="/children/{parentId}", method=RequestMethod.GET)
	@ResponseBody
	public List<Article> getChildArticles(@PathVariable int parentId) {
		return articleService.getChildArticles(parentId);
	}
	
	/***** deleteArticle *****/
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public boolean deleteArticle(@RequestParam("articleId") int articleId) {
		return articleService.deleteArticle(articleId);
	}
	
	/***** checkPassword *****/
	@RequestMapping(value="/check", method=RequestMethod.GET)
	@ResponseBody
	public boolean checkPassword(@RequestParam("articleId") int articleId,
			@RequestParam("password") String password) {		
		return articleService.checkPassword(articleId, password); 
	}
	
	/***** search *****/
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String search(@RequestParam(value="page", required=true, defaultValue="1") int page,
			@RequestParam(value="keyword", required=true) String keyword, Model model) throws UnsupportedEncodingException {
		keyword = URLDecoder.decode(keyword, "utf-8");
		
		ListHelper<Article> articleListHelper = articleService.searchArticles(
				page, ARTICLE_PER_PAGE, keyword, ArticleDao.SEARCH_ALL);
		model.addAttribute("articleListHelper", articleListHelper);
		model.addAttribute("keyword", keyword);
		
		return "searchResult";
	}
	
	/***** getSearchTitle 
	 * @throws UnsupportedEncodingException *****/
	@RequestMapping(value="/searchTitle", method=RequestMethod.GET)
	@ResponseBody
	public List<String> getSearchTitle(@RequestParam("keyword") String keyword) throws UnsupportedEncodingException {
		keyword = URLDecoder.decode(keyword, "utf-8");
		return articleService.getSearchTitle(keyword, 10);	
	}
}
