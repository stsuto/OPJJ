<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

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
		
		input {
			font-size: 22px;
		}
		
		.greska {
		   font-family: fantasy;
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF0000;
		   padding-left: 110px;
		}
		
		.formLabel {
		   display: inline-block;
		   width: 150px;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
		}
		
		.formControls {
		  margin-top: 10px;
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

	<form action="register" method="post">
		
		<div>
		 <div>
		  <span class="formLabel">Title</span><input type="text" name="title" value='<c:out value="${entry.title}"/>' size="20">
		 </div>
		 <c:if test="${form.hasError('title')}">
		 <div class="greska"><c:out value="${user.getError('title')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">Text</span><input type="text" name="text" value='<c:out value="${entry.text}"/>' size="80">
		 </div>
		 <c:if test="${form.hasError('text')}">
		 <div class="greska"><c:out value="${form.getError('text')}"/></div>
		 </c:if>
		</div>

		<div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <input type="submit" name="method" value="Submit">
		  <input type="submit" name="method" value="Cancel">
		</div>
		
	</form>

</body>
</html>