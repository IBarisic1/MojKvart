<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Threads list</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/threadsListView.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>Threads list</h3>
	</header>
	
	<main>
		<a href="${pageContext.request.contextPath}/newthread">Create new thread</a>
	
		<table border=1>
			<tr>
				<th>Thread</th>
				<th>Author</th>
				<th>Last posted</th>
			</tr>
	
			<c:forEach items="${threads}" var="thread">
				<tr>
					<td>
						<a href="${pageContext.request.contextPath}/thread?thread=${thread.name}">
							${thread.name} </a>
					</td>
					<td>${thread.username}</td>
					<td>${thread.lastPostTime}</td>
				</tr>
			</c:forEach>
		</table>
	</main>

	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>