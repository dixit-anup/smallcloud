<%@page import="com.mamascode.smallcloud.utils.PagingHelper"%>
<%@page import="com.mamascode.smallcloud.utils.DateFormatUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.mamascode.smallcloud.model.Article"%>
<%@page import="com.mamascode.smallcloud.utils.ListHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>검색 결과</title>
	<c:url var="resourceUrl" value="/res" />
	<c:url var="rootUrl" value="/" />
	<c:url var="readUrl" value="/read" />
	<link rel="stylesheet" type="text/css" href="${resourceUrl}/basic.css" media="all" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script type="text/javascript" src="${resourceUrl}/basic.js"></script>
	<style>
		table { width: 600px; margin-top: 100px; margin-left: auto; margin-right: auto; 
			border-collapse: collapse; padding: 1em; }
		table th, td { padding: 1em; border-top: 1px solid black; border-bottom: 1px solid black; }
		#bottom-toolbar { width: 80%; margin-top: 30px; }
		.childArticles { margin-top: 0px; margin-left: auto; margin-right: auto; }
		.childArticles td { font-size: 0.9em; border: none; }
		.deleted { color: orange; }
		#article_list_page { text-align: center; margin-top: 15px; }
		.highlighted { background-color: skyblue; }
	</style>
	
	<script>
		
	</script>
</head>
<body>
	<c:choose>
	
	<c:when test="${articleListHelper != null && articleListHelper.list != null}">
	<% 
		String keyword = (String) request.getAttribute("keyword");
		ListHelper<Article> articleListHelper = null;
		List<Article> articleList = null;
		if(request.getAttribute("articleListHelper") instanceof ListHelper) {
			articleListHelper = (ListHelper<Article>) request.getAttribute("articleListHelper");
			if(articleListHelper != null)
				articleList = articleListHelper.getList();
		}
	%>
	<table>
		<tr>
			<th width="10%"></th>
			<th width="45%">제목</th>
			<th width="20%">작성자</th>
			<th width="15%">작성일시</th>
			<th width="10%"></th>
		</tr>
		
		<% if(articleList != null && articleList.size() > 0) { %>
		<% for(Article article : articleList) { %>
		<%
			article.setArticleTitle(article.getArticleTitle().replaceAll(
					keyword, "<span class='highlighted'>" + keyword + "</span>"));
			article.setWriterName(article.getWriterName().replaceAll(
				keyword, "<span class='highlighted'>" + keyword + "</span>"));
		%>
		<c:set var="article" value="<%=article%>" />
		<tr id="article${article.articleId}">
			<td class="text-center">${article.articleId}</td>
			<td>
				<c:choose>
				<c:when test="${article.mask == false}">
				<a href="${readUrl}/${article.articleId}">${article.articleTitle}</a>
				</c:when>
				<c:otherwise><span class="deleted">삭제된 게시물입니다</span></c:otherwise>
				</c:choose>
			</td>
			<td class="text-center">${article.writerName}</td>
			<td class="text-center"><%=DateFormatUtil.getDateFormat(article.getWriteTime())%></td>
			<td class="text-center font-size-small">
				<% if(article.getChildCount() > 0) { %>
				<span id="articleToggle${article.articleId}" class="font-bold font-size-big btn-cursor" 
					onclick="toggleBtn(${article.articleId});" data-value="no-data">[+]
				</span>
				<% } %>
			</td>
		</tr>
		<% } // for loop End %>
		
		
		<% } else { %>
		<tr>
			<td colspan="5" class="text-center">검색 결과가 없습니다.</td>
		</tr>
		<% } %>
	</table>	<!-- article list table End -->
	
	<% PagingHelper pagingHelper = new PagingHelper(articleListHelper.getTotalPageCount(), 
				articleListHelper.getCurPageNumber(), 10); %>
			<c:set var="pagingHelper" value="<%=pagingHelper%>" />
	<div id="article_list_page">
		<c:if test="${pagingHelper.startPage != 1}">
		<a href="${rootUrl}list?page=${pagingHelper.startPage-pagingHelper.pagePerList}">[prev]</a>
		</c:if>
					
		<c:forEach var="i" begin="${pagingHelper.startPage}" end="${pagingHelper.endPage}">
				
		<c:if test="${i == pagingHelper.curPage}">
		<span class="font-bold">${i}</span>
		</c:if>
		
		<c:if test="${i != pagingHelper.curPage}">
		<a href="${rootUrl}list?page=${i}">${i}</a>
		</c:if>
				
		</c:forEach>
		<c:if test="${pagingHelper.endPage != pagingHelper.totalPage}">
		<a href="${rootUrl}list?page=${pagingHelper.endPage+pagingHelper.pagePerList}">[next]</a>
		</c:if>
	</div>
	
	</c:when>
	
	<c:otherwise>
	<div class="errorMsg">
		오류: 필요 정보가 전달되지 않음
	</div>	
	</c:otherwise>
	
	</c:choose>	<!-- choose tag End -->
	
	<div class="text-center" style="margin-top: 50px;">
		<a href="${rootUrl}">[home]</a>
	</div>
</body>
</html>