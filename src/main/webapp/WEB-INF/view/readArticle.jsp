<%@page import="com.mamascode.smallcloud.utils.DateFormatUtil"%>
<%@page import="com.mamascode.smallcloud.utils.SecurityUtil"%>
<%@page import="com.mamascode.smallcloud.model.Article"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>게시글 읽기</title>
	<c:url var="resourceUrl" value="/res" />
	<c:url var="rootUrl" value="/" />
	<c:url var="ajaxUrl" value="/delete" />
	<link rel="stylesheet" type="text/css" href="${resourceUrl}/basic.css" media="all" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script type="text/javascript" src="${resourceUrl}/basic.js"></script>
	<style>
		table { width: 600px; margin-top: 50px; margin-left: auto; margin-right: auto; 
			border: 1px solid gray; border-collapse: collapse; padding: 1em; }
		table td { padding: 1em; border-bottom: dotted 1px gray;}
		tr.content { }
		td.label { background-color: #F8F8FF; text-align: center;}
	</style>
	
	<script>
	function deleteArticle(articleId) {
		var ajaxUrl = '${ajaxUrl}';		
		var requestData = {"articleId" : articleId};
		var password = prompt("비밀번호를 입력해주세요", '');
		
		var requestDataChck = {
				"articleId" : articleId,
				"password" : password };
		
		$.ajaxSetup("async", false);
		$.get('${rootUrl}/check', requestDataChck, function(result) {
			if(result == true) {
				$.ajaxSetup("async", true);
				
				if(confirm("정말로 게시글을 삭제하시겠습니까?")) {
					$.post(ajaxUrl, requestData, function(result) {
						if(result) {
							alert('게시글 삭제 성공!');
							location.assign('${rootUrl}list?openedId=${ancestorId}');
						} else {
							alert('게시글 삭제 실패!');
							location.assign('${rootUrl}read/' + articleId);
						}
					});
				}
			} else {
				alert('비밀번호가 틀렸습니다!');
			}
			
		});
	}
	</script>
</head>
<body>
	<c:choose>
	
	<c:when test="${article != null && article.mask == false}">
	<%
		Article article = (Article) request.getAttribute("article");
	%>
	<div>
		<h2 class="text-center">게시글 읽기</h2>
		
		<table>
			<tr>
				<td class="label">글 제목</td>
				<td colspan="3">${article.articleTitle}</td>
			</tr>
			
			<tr>
				<td width="25%" class="label">작성자</td>
				<td width="25%">${article.writerName}</td>
				<td width="25%" class="label">작성날짜</td>
				<td width="25%" class="font-size-small">
					<%=DateFormatUtil.getDatetimeFormat(article.getWriteTime())%>
				</td>
			</tr>
			
			<tr class="content">
				<td class="label">내용</td>
				<td colspan="3">
					<%=article.getArticleContent().replaceAll("\n", "<br />")%>
				</td>
			</tr>
			
			<tr>
				<td class="label">홈페이지</td>
				<td colspan="3"><a href="${article.homepage}">${article.homepage}</a></td>
			</tr>
			
			<tr>
				<td colspan="4" class="text-right">
					<a href="#" onclick="deleteArticle(${article.articleId});">[삭제]</a>
					<a href="${rootUrl}set/${article.articleId}">[수정]</a>
					<a href="${rootUrl}write?parentId=${article.articleId}">[답글]</a>
				</td>
			</tr>
		</table>
	</div>
	
	<div class="text-center" style="margin-top: 50px;">
		<a href="${rootUrl}list?openedId=${ancestorId}">[home]</a>
	</div>
	
	</c:when>
	
	<c:otherwise>
	<div class="errorMsg">
		게시글이 존재하지 않거나 삭제되었습니다.<br /><br />
		<a href="${rootUrl}">[home]</a>
	</div>	
	</c:otherwise>
	
	</c:choose>	<!-- choose tag End -->
</body>
</html>