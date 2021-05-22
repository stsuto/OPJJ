<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Home page</title>
</head>
<body>
	<body style='background-color: ${sessionScope.pickedBgCol};'>
	<a href="colors.jsp">Background color chooser</a><p>
	<a href="trigonometric?a=0&b=90">Trigonometric functions table</a>
	<form action="trigonometric" method="GET">
		Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
	</form><p>
	<a href="stories/funny.jsp">The funniest story ever told</a><p>
	<a href="report.jsp">OS usage report</a><p>
	<a href="powers?a=1&b=100&n=3">Powers</a><p>
	<a href="appinfo.jsp">Application informations</a><p>
	<a href="glasanje">Glasanje</a>
</body>
</html>