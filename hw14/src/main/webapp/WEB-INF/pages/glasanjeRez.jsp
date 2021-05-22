<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<style type="text/css">
			table.rez td {text-align: center;}
		</style>
	</head>
<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" class="rez">
		<thead><tr><th>Opcija</th><th>Broj glasova</th></tr></thead>
		<tbody>
			<c:forEach items="${voteValues}" var="voteInfo">
				<tr><td>${voteInfo.optionTitle}</td><td>${voteInfo.votesCount}</td></tr>			
			</c:forEach>
		</tbody>
	</table>
	
	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="glasanje-grafika?pollID=${pollID}" width="400" height="400" />
	
	<h2>Rezultati u XLS formatu</h2>
	<p>Rezultati u XLS formatu dostupni su <a href="glasanje-xls?pollID=${pollID}">ovdje</a></p>

	<h2>Razno</h2>
	<p>Primjeri linkova pobjednika:</p>
	<ul>
		<c:forEach items="${best}" var="option">
			<li><a href="${option.optionLink}" target="_blank">${option.optionTitle}</a></li>		
		</c:forEach>
	</ul>
</body>
</html>