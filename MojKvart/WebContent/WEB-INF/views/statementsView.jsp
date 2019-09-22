<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Council statements</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/statementsView.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>Council statements</h3>
	</header>
	
	<main>
		<table border=1>
			<tr>
				<th>Councillor</th>
				<th>Statement</th>
			</tr>
			<c:forEach items="${statements}" var="statement">
				<tr>
					<td>${statement.councillor}</td>
					<td>${statement.statement}</td>
				</tr>
			</c:forEach>
		</table>
	</main>
	
	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>