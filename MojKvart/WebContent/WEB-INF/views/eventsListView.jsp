<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Events list</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eventsListView.css" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="menu.jsp"></jsp:include>
		<h3>Events list</h3>
	</header>
	
	<main>	
		<a href="${pageContext.request.contextPath}/proposeevent">Propose new event</a>

		<table border=1>
			<tr>
				<th>Event</th>
			</tr>
	
			<c:forEach items="${events}" var="event">
				<tr>
					<td>
						<ul>
							<li>Event: ${event.name}</li>
							<li>Author: ${event.username}</li>
							<li>Place: ${event.place}</li>
							<li>Start: ${event.start}</li>
							<li>Duration: ${event.duration}</li>
							<li>Description: ${event.description}</li>
						</ul>
					</td>
				</tr>
			</c:forEach>
		</table>
	</main>
	
	<footer>
		<jsp:include page="footer.jsp"></jsp:include>
	</footer>
</body>
</html>