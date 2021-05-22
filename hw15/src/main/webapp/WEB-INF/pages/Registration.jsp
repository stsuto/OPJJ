<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Registration</title>

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
		  <span class="formLabel">First name</span><input type="text" name="firstName" value='<c:out value="${user.firstName}"/>' size="20">
		 </div>
		 <c:if test="${user.hasError('firstName')}">
		 <div class="greska"><c:out value="${user.getError('firstName')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">Last name</span><input type="text" name="lastName" value='<c:out value="${user.lastName}"/>' size="20">
		 </div>
		 <c:if test="${user.hasError('lastName')}">
		 <div class="greska"><c:out value="${user.getError('lastName')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">EMail</span><input type="text" name="email" value='<c:out value="${user.email}"/>' size="50">
		 </div>
		 <c:if test="${user.hasError('email')}">
		 <div class="greska"><c:out value="${user.getError('email')}"/></div>
		 </c:if>
		</div>

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
		  <input type="submit" name="method" value="Register">
		  <input type="submit" name="method" value="Cancel">
		</div>
		
	</form>

</body>
</html>