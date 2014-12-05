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
	<title>게시글 목록</title>
	<c:url var="resourceUrl" value="/res" />
	<c:url var="ajaxUrl" value="/children/" />
	<c:url var="ajaxUrlSearchTitle" value="/searchTitle" />
	<c:url var="readUrl" value="/read" />
	<c:url var="searchUrl" value="/search" />
	<link rel="stylesheet" type="text/css" href="${resourceUrl}/basic.css" media="all" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script type="text/javascript" src="${resourceUrl}/basic.js"></script>
	<style>
		table { width: 600px; margin-top: 100px; margin-left: auto; margin-right: auto; 
			border-collapse: collapse; padding: 1em; }
		table th, td { padding: 1em; border-top: 1px solid black; border-bottom: 1px solid black; }
		#bottom-toolbar { width: 80%; margin-top: 30px; margin-bottom: 200px; }
		.childArticles { margin-top: 0px; margin-left: auto; margin-right: auto; }
		.childArticles td { font-size: 0.9em; border: none; }
		.deleted { color: orange; }
		#article_list_page { text-align: center; margin-top: 15px; }
	</style>
	
	<script>
		function getChildren(parentId) {
			var ajaxUrl = '${ajaxUrl}' + parentId;
			$.ajax({
				url : ajaxUrl,
				method : 'get',
				success : function(result) {
					var $baseTr = $('#article' + parentId);
					var $table = $('<table class="childArticles"></table>');
					for(var i = 0; i < result.length; i++) {
						var $tr = makeArticleFromResult(result[i]);
						$tr.appendTo($table);
					}
					var $containerTr = $('<tr id="childrenList' + parentId + '"></tr>');
					var $containerTd = $('<td colspan="5"></td>');
					$containerTd.appendTo($containerTr);
					
					$table.appendTo($containerTd);
					$containerTr.insertAfter($baseTr);
				},
				error : function() {
					// ajax error
					alert("ajax error: 답변글 목록 가져오기 실패");
				}
			});
		}
		
		function makeArticleFromResult(result) {
			var $tr = $('<tr></tr>');
			var blankTxt = "";
			
			for(var i = 0; i < result.level - 1; i++) {
				blankTxt += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
			}
			blankTxt += 'ㄴ';
			
			var titleStr = (result.mask == false) ? 
					'<a href="${readUrl}/' + result.articleId +'">' + result.articleTitle + '</a>' :
					'<span class="deleted">삭제된 게시물입니다</span>';
			var innerHtml = '<td>' + blankTxt + '['  + result.articleId + ']&nbsp;'
				+ titleStr + '&nbsp;&nbsp;&nbsp;';
			
			var writeTime = new Date(result.writeTime);
			var writeTimeStr = getDateFormat(writeTime);
			
			innerHtml += (result.writerName + ' | ' + writeTimeStr + '</td>');
			
			var $td = $(innerHtml);
			$td.appendTo($tr);
			
			return $tr;
		}
		
		function toggleBtn(articleId) {
			var btnId = '#articleToggle' + articleId;
			var trId = '#childrenList' + articleId;
			var $btn = $(btnId);
			var $tr = $(trId);
			
			if($btn.attr('data-value') == 'no-data') {
				$btn.attr('data-value', 'open');
				$btn.text('[-]');
				getChildren(articleId);
			} else if($btn.attr('data-value') == 'open') {
				$btn.attr('data-value', 'close');
				$btn.text('[+]');
				$tr.css('display', 'none');
			} else {
				$btn.attr('data-value', 'open');
				$btn.text('[-]');
				$tr.css('display', 'table-row');
			}
		}
		
		function goSearch() {
			var keyword = $('#keyword').attr('value');
			keyword = encodeURIComponent(encodeURIComponent($.trim(keyword)));
			
			if(keyword.length == 0) {
				alert('검색 키워드를 입력해주세요!');
			} else {
				location.assign('${searchUrl}?keyword=' + keyword);
			}
		}
		
		$(document).ready(function() {
			var openedId = '${openedId}';
			if(openedId != null && openedId != 0) {
				toggleBtn(openedId);
			}
			
			$('#keyword').change(function(event) {
				var keyword = $('#keyword').attr('value');
				keyword = encodeURIComponent(encodeURIComponent($.trim(keyword)));
				//alert(keyword);
				var ajaxUrl = '${ajaxUrlSearchTitle}?keyword=' + keyword;
				
				if(keyword.length > 0) {
					$.ajax({
						url : ajaxUrl,
						method : 'get',
						success : function(result) {
							if($('#searchTxtBox') != null) {
								$('#searchTxtBox').remove();
							}
							
							var $div = $('<div id="searchTxtBox"></div>');
							$div.css('position', 'absolute');
							$div.css('width', $('#keyword').innerWidth());
							$div.css('left', $('#keyword').offset().left);
							$div.css('top', $('#keyword').offset().top + $('#keyword').innerHeight());
							$div.css('border', '1px solid skyblue');
							
							$div.appendTo($('body'));
							
							for(var i = 0; i < result.length; i++) {
								var keyword = result[i];
								var $span = $('<span onclick="setKeyword(\'' + keyword +'\')">' 
										+ keyword +'</span><br />');
								
								$span.css('cursor', 'pointer');
								$span.appendTo($div);
							}
						},error : function() {
							// ajax error
							alert("ajax error: 검색어 가져오기 실패");
						}
					});
				}
			});
			
			$('#keyword').focusout(function() {
				if($('#searchTxtBox') != null) {
					$('#searchTxtBox').remove();
				}
			});
		});
		
		function setKeyword(keyword) {
			$('#searchTxtBox').remove();
			$('#keyword').attr('value', keyword);
		}
	</script>
</head>
<body>
	<c:choose>
	
	<c:when test="${articleListHelper != null && articleListHelper.list != null}">
	<% 
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
			<td colspan="5" class="text-center">게시글이 없습니다.</td>
		</tr>
		<% } %>
	</table>	<!-- article list table End -->
	
	<% PagingHelper pagingHelper = new PagingHelper(articleListHelper.getTotalPageCount(), 
				articleListHelper.getCurPageNumber(), 10); %>
			<c:set var="pagingHelper" value="<%=pagingHelper%>" />
	<div id="article_list_page">
		<c:if test="${pagingHelper.startPage != 1}">
		<a href="${rootUrl}list?page=1">1</a>
		&nbsp;&nbsp;
		<a href="${rootUrl}list?page=${pagingHelper.startPage-pagingHelper.numberOfPagesDisplyed}">[prev]</a>
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
		<a href="${rootUrl}list?page=${pagingHelper.endPage+pagingHelper.numberOfPagesDisplyed}">[next]</a>
		&nbsp;&nbsp;
		<a href="${rootUrl}list?page=${pagingHelper.totalPage}">${pagingHelper.totalPage}</a>
		</c:if>
	</div>
	
	</c:when>
	
	<c:otherwise>
	<div class="errorMsg">
		오류: 필요 정보가 전달되지 않음
	</div>	
	</c:otherwise>
	
	</c:choose>	<!-- choose tag End -->
	
	<c:url var="writeUrl" value="/write" />
	<div id="bottom-toolbar" class="text-right">
		<button onclick="location.assign('${writeUrl}');">게시글 쓰기</button><br /><br />
		<input type="text" class="bigInput" id="keyword" />&nbsp;
		<button onclick="goSearch();">검색</button>
	</div>
</body>
</html>