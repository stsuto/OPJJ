<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Entry page</title>

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
	
	<p style="text-decoration: underline; font-size: 26px;"><b>${entry.title}</b></p>
	<p>${entry.text}</p>
	
	<c:choose>
		<c:when test="${author.nick.equals(sessionScope['current.user.nick'])}">
			<br><a href="${pageContext.request.contextPath}/servleti/author/${author.nick}/edit?id=${entry.id}">Edit entry</a>
		</c:when>
	</c:choose>
	
	<br><hr><br>

	<c:choose>
		<c:when test="${entry.comments.size() != 0}">
			<p style="text-decoration: underline; font-size: 26px;"><b>Comments:</b></p>
			<ul><c:forEach items="${entry.comments}" var="comment">
				<!-- {pageContext.request.contextPath} -->
				<br><li><p><b>User</b>: ${comment.usersEMail}</p>
				<p><b>Message</b>: ${comment.message}</p></li>
			</c:forEach></ul>
		</c:when>
		<c:otherwise>
			<p>No comments at this moment.</p>
		</c:otherwise>
	</c:choose>
	
	<br><hr><br>
	
	<p style="text-decoration: underline; font-size: 26px;"><b>Add new comment:</b></p>
	
	<form action="" method="post">
		
		<c:choose>
			<c:when test="${!author.nick.equals(sessionScope['current.user.nick'])}">
				<div>
				 <div>
				  <span class="formLabel">Email</span><input type="text" name="email" value='<c:out value="${form.email}"/>' size="50">
				 </div>
				 <c:if test="${form.hasError('email')}">
				 <div class="greska"><c:out value="${form.getError('email')}"/></div>
				 </c:if>
				</div>
			</c:when>
		</c:choose>
		
		<div>
		 <div>
		  <span class="formLabel">Message</span><input type="text" name="message" value='<c:out value="${form.message}"/>' size="50">
		 </div>
		 <c:if test="${form.hasError('message')}">
		 <div class="greska"><c:out value="${form.getError('message')}"/></div>
		 </c:if>
		</div>

		<div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <input type="submit" name="method" value="Submit">
		  <input type="submit" name="method" value="Cancel">
		</div>
		
	</form>
	
	<br><hr><br>
	
	<p><a href="${pageContext.request.contextPath}">Back to main page</a></p>

</body>
</html>