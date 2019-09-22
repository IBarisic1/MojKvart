<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${thread}</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/threadView.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>${thread}</h3>
	</header>
	
	<main>
		<p>Thread author: ${author}</p>
	
		<table border=1>
			<tr>
				<th>User</th>
				<th>Post</th>
				<th>Creation time</th>
			</tr>
			<c:forEach items="${posts}" var="post">
				<tr>
					<td>${post.username}</td>
					<td>${post.text}</td>
					<td>${post.creationTime}</td>
					<c:if test="${post.username==loggedInUser.username}">
						<td><a href="${pageContext.request.contextPath}/editpost?thread=${thread}&id=${post.id}">Edit</a></td>
					</c:if>
					<c:if test="${post.username==loggedInUser.username || loggedInUser.roles.contains('moderator')}">
						<td><a href="${pageContext.request.contextPath}/deletepost?thread=${thread}&id=${post.id}">Delete</a></td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
		
		<form method="POST" action="${pageContext.request.contextPath}/newpost">
			<input type="hidden" name="thread" value="${thread}" />
			<label>New post:
				<textarea name="post"></textarea>
			</label>
			<input type="submit" value="Submit" />
		</form>
	</main>

	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>