<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<li class="nav-item dropdown">
	<a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">Admin</a>
	<div class="dropdown-menu">
		<a class="dropdown-item" href="${pageContext.request.contextPath}/adminregister">Register new administrator</a>
		<a class="dropdown-item" href="${pageContext.request.contextPath}/privilegedregister">Register new privileged resident</a>
		<a class="dropdown-item" href="${pageContext.request.contextPath}/addneighborhood">Add new neighborhood</a>
	</div>
</li>