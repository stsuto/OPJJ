<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Index glasanja</title>
</head>
<body>
	<h1>${requestScope.poll.title}</h1>
	<p>${requestScope.poll.message}</p>
	<ol>
		<c:forEach items="${requestScope.pollOptions}" var="option">
			<li><a href="glasanje-glasaj?id=${option.id}&pollID=${poll.id}">${option.optionTitle}</a></li>
		</c:forEach>
	</ol>
</body>
</html>