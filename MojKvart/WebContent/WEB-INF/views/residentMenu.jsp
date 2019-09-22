<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<li class="nav-item dropdown">
	<a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">Resident</a>
	<div class="dropdown-menu">
		<a class="dropdown-item" href="${pageContext.request.contextPath}/threadslist">Threads</a>
		<a class="dropdown-item" href="${pageContext.request.contextPath}/eventslist">Events</a>
		<a class="dropdown-item" href="${pageContext.request.contextPath}/councilstatementslist">Council statements</a>
		<a class="dropdown-item" href="${pageContext.request.contextPath}/counciltopicannouncementslist">Council topic announcements</a>
	</div>
</li>