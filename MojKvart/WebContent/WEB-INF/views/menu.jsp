<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
	<span class="navbar-brand h1">Moj Kvart</span>
	
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
    	<span class="navbar-toggler-icon"></span>
  	</button>
  	<div class="collapse navbar-collapse" id="navbarNavDropdown">
		<ul class="navbar-nav mr-auto">
			<c:if test="${loggedInUser.roles.contains('resident')}">
				<jsp:include page="residentMenu.jsp"></jsp:include>
			</c:if>
			<c:if test="${loggedInUser.roles.contains('moderator')}">
				<jsp:include page="moderatorMenu.jsp"></jsp:include>
			</c:if>
			<c:if test="${loggedInUser.roles.contains('councillor')}">
				<jsp:include page="councillorMenu.jsp"></jsp:include>
			</c:if>
			<c:if test="${loggedInUser.roles.contains('admin')}">
				<jsp:include page="adminMenu.jsp"></jsp:include>
			</c:if>
			<c:if test="${empty loggedInUser.roles}">
				<jsp:include page="nonLoggedInMenu.jsp"></jsp:include>
			</c:if>
		</ul>
		<ul class="navbar-nav ml-auto">
			<c:if test="${not empty loggedInUser.roles}">
				<jsp:include page="loggedInMenu.jsp"></jsp:include>
			</c:if>
		</ul>
	</div>
</nav>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>