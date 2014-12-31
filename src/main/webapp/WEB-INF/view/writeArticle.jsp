<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>새 게시글 작성</title>
	<c:url var="resourceUrl" value="/res" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="${resourceUrl}/basic.css" media="all" />
	<style>
		table { width: 500px; margin-top: 30px; margin-left: auto; margin-right: auto; 
			border: 1px solid gray; border-collapse: collapse; padding: 1em; }
		table td { padding: 1em; }
	</style>
	
	<script>
		$(document).ready(function() {
			$('#btnFileUpload').click(function() {
				$('#uploadFiles').trigger("click");
			});
			
			$('#uploadFiles').change(function() {
				var $fakeFileUploader = $('#fakeFileUploader');
				var $spanUploadFileList = $('#spanUploadFileList');
				var spanUploadFileList = $spanUploadFileList.get(0);
				
				$spanUploadFileList.empty();
				$fakeFileUploader.attr('value', '');
				
				var files = $('#uploadFiles').get(0).files;
				var txtFileList = "";
				
				if(files.length != 0) {
					for(var i = 0; i < files.length; i++) {
						var file = files[i];
						var txtFileNameNode = document.createTextNode(file.name);
						txtFileList += file.name;
						
						spanUploadFileList.appendChild(txtFileNameNode);
						
						if(i != files.length - 1) {
							txtFileList += ';';
							$('<br />').appendTo($spanUploadFileList);
						}
					}
				} else {
					txtFileList = "선택된 파일이 없습니다.";
					var txtFileNameNode = document.createTextNode("선택된 파일이 없습니다.");
					
					spanUploadFileList.appendChild(txtFileNameNode);
				}
				
				$fakeFileUploader.attr('value', txtFileList);
				$fakeFileUploader.attr('title', txtFileList);
			});
		});
	</script>
</head>
<body>
	<table>
		<c:url var="postUrl" value="/write" />
		<c:url var="rootUrl" value="/" />
		<sf:form action="${postUrl}" method="post" modelAttribute="article">
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
				<sf:input path="writerName" cssClass="mediumInput" /><br />
				<span><sf:errors path="writerName" cssClass="error" /></span>
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
			<td colspan="3">
				<input type="text" id="fakeFileUploader" value="선택된 파일이 없습니다." style="width:250px;height:1.5em;" />&nbsp;
				<span id="btnFileUpload" style="cursor:pointer;">[+파일추가]</span>
				<input type="file" id="uploadFiles" name="uploadFiles" multiple style="display:none;"/><br />
				<p class="font-size-small">
					[첨부파일]<br />
					<span id="spanUploadFileList" style="color:gray;">선택된 파일이 없습니다.</span>
				</p>
				
			</td>
		</tr>
		
		<tr>
			<td colspan="4" class="text-center">
				<input type="submit" value="작성" />
				<input type="button" value="취소" onclick="location.assign('${rootUrl}');"/>
			</td>
		</tr>
		
		</sf:form>	<!-- spring form tag End -->
	</table>	<!-- table tag End -->
</body>
</html>