<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>New announcement</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>New announcement</h3>
	</header>
	
	<main>
		<p>${errorMessage}</p>
	
		<form method="POST" action="${pageContext.request.contextPath}/newcounciltopicannouncement">
			<table>
				<tr>
					<td>Announcement:<td>
					<td><input type="text" name="name" value="${announcement.name}"/></td>
				</tr>
				<tr>
					<td>Start<td>
					<td><input type="datetime-local" name="start" value="${announcement.start}"/></td>
				</tr>
				<tr>
					<td><input type="submit" value="Submit" /></td>
				</tr>
			</table>
		</form>
	</main>
	
	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>