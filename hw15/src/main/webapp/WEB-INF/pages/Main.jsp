<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Main page</title>

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
	
	<c:choose>
		<c:when test="${sessionScope['current.user.id'] ne null}">
			<header>
				<h3>${sessionScope['current.user.ln']} ${sessionScope['current.user.fn']}</h3>
				<h3 align=right><a href="logout">Log out</a></h3>
			</header>
		</c:when>
			
		<c:otherwise>
			<header><h3>No user is logged in.</h3></header>
			<br><hr><br>
			<p style="text-decoration: underline; font-size: 26px;">Log in here:</p>

			<form action="main" method="post">
		
				<div>
				 <div>
				  <span class="formLabel">Nickname</span><input type="text" name="nick" value='<c:out value="${user.nick}"/>' size="20">
				 </div>
				 <c:if test="${user.hasError('nick')}">
				 <div class="greska"><c:out value="${user.getError('nick')}"/></div>
				 </c:if>
				</div>
				
				<div>
				 <div>
				  <span class="formLabel">Password</span><input type="password" name="password" size="20">
				 </div>
				 <c:if test="${user.hasError('password')}">
				 <div class="greska"><c:out value="${user.getError('password')}"/></div>
				 </c:if>
				</div>
		
				<div class="formControls">
				  <span class="formLabel">&nbsp;</span>
				  <input type="submit" name="method" value="login">
				</div>
				
			</form>
		
			<br><hr><p><a href="register">Register (new user)</a></p>
			
		</c:otherwise>
	</c:choose>
	
	<hr>

	<c:choose>
		<c:when test="${authors.size() ne 0}">
			<p style="text-decoration: underline; font-size: 26px;">Registered authors:</p>
			<ul>
			<c:forEach items="${authors}" var="author">
				<li><a href="author/${author.nick}">${author.lastName} ${author.firstName} - ${author.nick}</a></li>
			</c:forEach>
			</ul>
		</c:when>
		
		<c:otherwise>
			<p>No registered authors at this moment.</p>
		</c:otherwise>
	</c:choose>

</body>
</html>