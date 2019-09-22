<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register administrator account</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>Register administrator account</h3>
	</header>
	
	<main>
		<p>${errorMessage}</p>
	
		<form method="POST" action="${pageContext.request.contextPath}/adminregister">
			<table>
				<tr>
					<td>Username:</td>
					<td><input type="text" name="username" value="${user.username}" /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td>
						<input type="password" name="password" value="${user.password}" />
					</td>
				</tr>
				<tr>
					<td><input type="submit" value="Submit"></td>
					<td><a href="${pageContext.request.contextPath}/home">Cancel</a></td>
				</tr>
			</table>
		</form>
	</main>

	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>