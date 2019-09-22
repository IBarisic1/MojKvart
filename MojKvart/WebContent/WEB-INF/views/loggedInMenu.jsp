<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<li class="nav-item">
	<a class="nav-link" href="${pageContext.request.contextPath}/userinfo">User: ${SESSION_USER.username}</a>
</li>
<li class="nav-item">
	<a class="nav-link" href="${pageContext.request.contextPath}/logout">Log out</a>
</li>