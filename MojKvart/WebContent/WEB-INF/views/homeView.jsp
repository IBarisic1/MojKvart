<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/homeView.css" type="text/css">
</head>
<body>
	<main>
		<div class="center">
			<jsp:include page="header.jsp"></jsp:include>
			<a class="btn btn-info" href="${pageContext.request.contextPath}/register">Register</a>
			<a class="btn btn-info" href="${pageContext.request.contextPath}/login">Login</a>
		</div>
	</main>
	
	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>