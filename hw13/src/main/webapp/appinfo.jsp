<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Application informations</title>
</head>
<body>
	<body style='background-color: ${sessionScope.pickedBgCol};'>
	<a href="/webapp2">Back to home page</a><p>
	 Application has been running for: 
	 <%
	 	long creation = (Long) request.getServletContext().getAttribute("time");
	 	long current = System.currentTimeMillis();
	 	
	 	long ms = current - creation;
	 	long s = ms / 1000;
	 	long m = s / 60;
	 	long h = m / 60;
	 	long d = h / 24;
	 		
	 	out.write(
	 		String.format("%d days %d hours %d minutes %d seconds %d miliseconds", 
	 			d, h % 24, m % 60, s % 60, ms % 1000)
	 	);
	 %>
</body>
</html>