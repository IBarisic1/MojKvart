<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add new neighborhood</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>Add new neighborhood</h3>
	</header>
	
	<main>
		<h5>Existing neighborhoods:</h5>
		<ol>
			<c:forEach items="${neighborhoods}" var="neighborhood">
				<li>${neighborhood.name}</li>
			</c:forEach>
		</ol>
		
		<form method="POST" action="${pageContext.request.contextPath}/addneighborhood">
			<label>
				New neighborhood:
				<input type="text" name="neighborhood" value="${neighborhood.name}" />
			</label>
			<input type="submit" value="Submit" />
		</form>
	</main>
	
	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>