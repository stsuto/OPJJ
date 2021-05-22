<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session = "true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Background color changer</title>
</head>
<body>
	<body style='background-color: ${sessionScope.pickedBgCol};'>
	<a href="/webapp2">Back to home page</a><p>
	Available background colors:<p>
	<a href="setcolor?color=white">&emsp;WHITE</a><p>
	<a href="setcolor?color=red">&emsp;RED</a><p>
	<a href="setcolor?color=green">&emsp;GREEN</a><p>
	<a href="setcolor?color=cyan">&emsp;CYAN</a>
</body>
</html>