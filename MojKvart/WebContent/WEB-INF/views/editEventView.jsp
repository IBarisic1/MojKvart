<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit event</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>Edit event</h3>
	</header>
	
	<main>
		<p>${errorMessage}</p>
	
		<form method="POST" action="${pageContext.request.contextPath}/editevent">
			<input type="hidden" name="id" value="${event.id}" />
			<table>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name" value="${event.name}" /></td>
				</tr>
				<tr>
					<td>Place:</td>
					<td><input type="text" name="place" value="${event.place}" /></td>
				</tr>
				<tr>
					<td>Start:</td>
					<td><input type="datetime-local" name="start" value="${start}" /></td>
				</tr>
				<tr>
					<td>Duration:</td>
					<td><input type="text" name="duration" value="${event.duration}" /></td>
				</tr>
				<tr>
					<td>Description:</td>
					<td><input type="text" name="description" value="${event.description}" /></td>
				</tr>
				<tr>
					<td>Username:</td>
					<td>${event.username}</td>
				</tr>
				<tr>
					<td><input type="submit" value="Update and approve" /></td>
					<td><a href="${pageContext.request.contextPath}/pendingeventslist">Cancel</a></td>
				</tr>
			</table>
		</form>
	</main>
	
	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>