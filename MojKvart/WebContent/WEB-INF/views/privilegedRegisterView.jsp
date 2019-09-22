<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register privileged resident account</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>Register privileged resident account</h3>
	</header>
	
	<main>
		<p>${errorMessage}</p>
	
		<form method="POST" action="${pageContext.request.contextPath}/privilegedregister">
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
					<td>Neighborhood</td>
					<td>
						<select name="neighborhood">
							<c:forEach items="${neighborhoods}" var="neighborhood">
								<option value="${neighborhood.name}">${neighborhood.name}</option>
							</c:forEach>
						</select>
					<td>
				</tr>
				<tr>
					<td>Privilege level:</td>
					<td>
						<select name="role">
							<c:forEach items="${privilegedRoles}" var="role">
								<option value="${role}">${role}</option>
							</c:forEach>
						</select>
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