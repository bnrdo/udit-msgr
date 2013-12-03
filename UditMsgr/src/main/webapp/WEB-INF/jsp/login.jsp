<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Login</title>
		<link rel="stylesheet" type="text/css" href='<c:out value="${pageContext.request.contextPath}"/>/css/uditmsgr.css' />
		<script src='<c:out value="${pageContext.request.contextPath}"/>/js/jquery.min.js'></script>
		<script type='text/javascript'>
		
			var d = document;
			var newUserTextB;
			
			function onloadHook(){
				newUserTextB = d.getElementById("txtUserID");
				newUserTextB.onkeydown = registerUserByKeyPress;
				newUserTextB.focus();
				
				if("${isUsernameTaken}" !== "" && 
						"${isUsernameTaken}" !== null){
					alert("Username ${user} is already taken. Please choose another.");
				}
			}
			
			function isUserNew(){
				if(userName === "")
					return true;
				
				return false;
			}
		
			function registerUser(){
				
				var newUserID = newUserTextB.value;
				
				if(newUserID.trim() === ''){
					alert("Username should not be blank");
					return;
				}
				
				window.location.href = 'registerUser.htm?userName=' + newUserID;
			}
			
			function registerUserByKeyPress(e){
				if (e.keyCode == 13){
					registerUser();
			    }
			}
			
		</script>
	</head>
	<body onload="onloadHook()">
		<!--  Register Area -->
		<div id="registerArea">
			<table width="100%" height="100%">
				<tr>
					<td align="center">
						<table>
							<tr><td width="100%" align="center" valign="middle"><input type="text" id="txtUserID" class="magin-bottom-10"/></td></tr>
							<tr><td width="100%" align="center" valign="middle"><input type="button" class="white-button" onclick="registerUser()" value="Register"/></td></tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
