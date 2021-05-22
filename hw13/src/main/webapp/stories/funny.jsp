<%@page import="java.util.Random"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
body {
        color: 
        <%
        	int num = new Random().nextInt(4);

			switch (num) {
			case 0:
				out.write("gold");
				break;
			
			case 1:
				out.write("pink");
				break;
			
			case 2:
				out.write("white");
				break;
			
			case 3:
				out.write("black");
				break;
			
			default:
				break;
			}
        %>
}
</style>
<meta charset="UTF-8">
<title>The funniest story ever told</title>
</head>
<body>
	<body style='background-color: ${sessionScope.pickedBgCol};'>
	<a href="/webapp2">Back to home page</a><p>
	Igraju dva čavla košarku pa dođe čekić i zakuca.
	Hehe.
</body>
</html>