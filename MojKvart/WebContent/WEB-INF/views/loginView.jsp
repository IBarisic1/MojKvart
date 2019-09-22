<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>Login</h3>
	</header>

	<main>
		<p style="color: red;">${errorMessage}</p>

		<form method="POST" action="${pageContext.request.contextPath}/login">
			<table>
				<tr>
					<td>Username:</td>
					<td><input type="text" name="username" value="${user.username}" /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type="password" name="password" value="${user.password}" /></td>
				</tr>
				<tr>
					<td>Remember me:</td>
					<td><input type="checkbox" name="rememberMe" value="Y" /></td>
				</tr>
				<tr>
					<td colspan=2>
						<input type="submit" value="Submit" />
						<a href="${pageContext.request.contextPath}/" class="btn">Cancel</a>
					</td>
				</tr>
			</table>
		</form>
	</main>

	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>