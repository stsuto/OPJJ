<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trigonometric functions table</title>
</head>
<body>
	<body style='background-color: ${sessionScope.pickedBgCol};'>
	<a href="/webapp2">Back to home page</a><p>
	<table border = 1>
		<tr>
		    <th>x</th>
		    <th>sin(x)</th>
		    <th>cos(x)</th>
	  	</tr>
	  	<c:forEach items="${angleValues}" var="values">
	  		<tr>
				<td>${values.angle}</td>
			    <td>${values.sin}</td>
		    	<td>${values.cos}</td>
			</tr>
	  	</c:forEach>
	</table>
</body>
</html>
