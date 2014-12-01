<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>게시글 수정</title>
	<c:url var="rootUrl" value="/" />
	<c:url var="resourceUrl" value="/res" />
	<link rel="stylesheet" type="text/css" href="${resourceUrl}/basic.css" media="all" />
	<style>
		table { width: 500px; margin-top: 30px; margin-left: auto; margin-right: auto; 
			border: 1px solid gray; border-collapse: collapse; padding: 1em; }
		table td { padding: 1em; }
	</style>
</head>
<body>
	<c:choose>
	
	<c:when test="${article != null && article.mask == false}">
	
	<table>
		<sf:form method="post" modelAttribute="article">
		<tr>
			<td>제목</td>
			<td colspan="3">
				<sf:input path="articleTitle" cssClass="bigInput" /><br />
				<span><sf:errors path="articleTitle" cssClass="error" /></span>
			</td>
		</tr>
		
		<tr>
			<td width="25%">작성자</td>
			<td width="25%">
				${article.writerName}
			</td>
			<td width="25%">비밀번호</td>
			<td width="25%">
				<sf:password path="password" cssClass="smallInput" /><br />
				<span><sf:errors path="password" cssClass="error" /></span>
			</td>
		</tr>
		
		<tr>
			<td>홈페이지</td>
			<td colspan="3">
				<sf:input path="homepage" cssClass="bigInput" /><br />
				<span><sf:errors path="homepage" cssClass="error" /></span>
			</td>
		</tr>
		
		<tr>
			<td>내용</td>
			<td colspan="3">
				<sf:textarea path="articleContent" cssClass="bigText" /><br />
				<span><sf:errors path="articleContent" cssClass="error" /></span>
			</td>
		</tr>
		
		<tr>
			<td></td>
			<td colspan="3" class="text-center">
				<input type="submit" value="작성" />
				<input type="button" value="취소" onclick="location.assign('${rootUrl}/read/${article.articleId}');"/>
			</td>
		</tr>
		</sf:form>	<!-- spring form tag End -->
	</table>	<!-- table tag End -->
	
	</c:when>
	
	<c:otherwise>
	<div class="errorMsg">
		게시글이 존재하지 않거나 삭제되었습니다.<br /><br />
		<a href="${rootUrl}">[home]</a>
	</div>	
	</c:otherwise>
	
	</c:choose>		<!-- choose tag End -->
</body>
</html>