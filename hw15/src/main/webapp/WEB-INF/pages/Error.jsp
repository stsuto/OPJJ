<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>

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

	<h3>An error happened. Click <a href="${pageContext.request.contextPath}">here</a> to return to the main page.</h3>

</body>
</html>