<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit post</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>Edit post</h3>
	</header>
	
	<main>	
		<form method="POST" action="${pageContext.request.contextPath}/editpost">
			<input type="hidden" name="id" value="${post.id}" />
			<input type="hidden" name="thread" value="${post.thread}" />
			<label>
				New text:
				<textarea name="text">${post.text}</textarea>
			</label>
			<input type="submit" value="Submit" />
			<a href="${pageContext.request.contextPath}/thread?thread=${post.thread}">Cancel</a>
		</form>
	</main>
	
	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>