<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Entry list</title>

	<style type="text/css">
		
		body {
		  max-width: 960px;
		  margin: auto;
		  background-color: #9fffdf;
		  font-size: 22px;
		}
		
		header {
			font-size: 26px;
		}
		
	</style>
	
</head>
<body>

	<header>
		<c:choose>
			<c:when test="${sessionScope['current.user.id'] ne null}">
				<h3>${sessionScope['current.user.ln']} ${sessionScope['current.user.fn']}</h3>
				<h3 align=right><a href="${pageContext.request.contextPath}/servleti/logout">Log out</a></h3>
			</c:when>
			<c:otherwise>
				<h3>No user is logged in.</h3>
			</c:otherwise>
		</c:choose>
	</header>
			
	<br><hr><br>	
	
	<c:choose>
		<c:when test="${entries.size() != 0}">
			<p style="text-decoration: underline; font-size: 26px;"><b>Blog entries:</b></p>
			<ul><c:forEach items="${entries}" var="entry">
				<!-- {pageContext.request.contextPath} -->
				<li><a href="${author.nick}/${entry.id}">${entry.title}</a></li>
			</c:forEach></ul>
		</c:when>
		<c:otherwise>
			<p>No entries at this moment.</p>
		</c:otherwise>
	</c:choose>
	
	<br><hr><br>
	
	<c:choose>
		<c:when test="${author.nick.equals(sessionScope['current.user.nick'])}">
			<p><a href="${author.nick}/new">New entry</a></p>
		</c:when>
	</c:choose>
	
	<p><a href="${pageContext.request.contextPath}">Back to main page</a></p>
	
</body>
</html>