<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>새 게시글 작성</title>
	<style>
		table { width: 500px; margin-top: 30px; margin-left: auto; margin-right: auto; 
			border: 1px solid black; border-collapse: collapse; }
		table td { }
		.error { color: red; }
	</style>
</head>
<body>
	<table>
		<c:url var="postUrl" value="/write" />
		<sf:form action="${postUrl}" method="post" modelAttribute="article">
		<tr>
			<td width="30%">제목</td>
			<td width="70%">
				<sf:input path="articleTitle" cssClass="" />
				<span><sf:errors path="articleTitle" cssClass="error" /></span>
			</td>
			
			<td>작성자</td>
			<td>
				<sf:input path="writerName" cssClass="" />
				<span><sf:errors path="writerName" cssClass="error" /></span>
			</td>
		</tr>
		</sf:form>
	</table>
</body>
</html>