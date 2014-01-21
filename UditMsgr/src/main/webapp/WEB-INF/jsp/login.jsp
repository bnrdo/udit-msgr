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
		<link rel="stylesheet" type="text/css" href='<c:out value="${pageContext.request.contextPath}"/>/css/uditmsgr.css?t=<?= time(); ?>' />
		<script src='<c:out value="${pageContext.request.contextPath}"/>/js/jquery.min.js'></script>
		<script type='text/javascript'>
		
			var d = document;
			var userNameTextB;
			
			function onloadHook(){
				userNameTextB = d.getElementById("txtUserID");
				userNameTextB.onkeydown = loginUserByKeyPress;
				userNameTextB.focus();
			}
			
			function isUserNew(){
				if(userName === "")
					return true;
				
				return false;
			}
		
			function registerUser(){
				
				var newUserID = userNameTextB.value;
				
				if(newUserID.trim() === ''){
					alert("Username should not be blank");
					return;
				}
				
				$.ajax({
					cache: false,
					type: 'POST',
					url: 'registerUser.htm',
					data: {
						"userName" : newUserID
					},
					success : function(data){
						if(data === "OK"){
							alert("Successfully registered! You will now be logged in as " + newUserID);
							window.location.href = 'main.htm';
						}else{
							alert(data);
						}
					}
				});
			}
			
			function loginUser(){
				var userID = userNameTextB.value;
				
				if(userID.trim() === ''){
					alert("Username should not be blank");
					return;
				}
				
				$.ajax({
					cache: false,
					type: 'POST',
					url: 'login.htm',
					data: {
						"userName" : userID
					},
					success : function(data){
						if(data === "OK"){
							window.location.href = 'main.htm';
						}else{
							alert(data);
						}
					}
				});
			}
			
			function registerUserByKeyPress(e){
				if (e.keyCode == 13){
					registerUser();
			    }
			}
			
			function loginUserByKeyPress(e){
				if (e.keyCode == 13){
					loginUser();
			    }
			}
			
		</script>
	</head>
	<body onload="onloadHook()">
		<!--  Register Area -->
		<div id="login-area">
			<table width="100%" height="100%">
				<tr>
					<td align="center">
						<table class="gray-cute-outline">
							<tr>
								<td align="left" class="blue-title-14">
									Already Registered?
								</td>
							</tr>
							<tr>
								<td align="center" class="prelogin-message">
									<c:if test="${not empty preLoginMessage}">
										<div>
											<c:out value="${preLoginMessage}"/>
										</div>
									</c:if>
								</td>
							</tr>
							<tr>
								<td>
									<div class="float-left" id="display-user">
									<table border=0 cellSpacing=0 cellPadding=0 height="100%" width="100%">
										<tr><td rowspan="3" valign="top"><img src='<c:out value="${pageContext.request.contextPath}"/>/images/login64.png' class='main-logo'></td></tr>
										<tr><td class="padding-left-7" align="left"><input type="text" id="txtUserID" class='margin-left-neg-30'/></td></tr>
										<tr><td class="top-options-subtitle" align="right"><span class='margin-left-neg-30'>Your IP : <c:out value="${userIp}"/></span></td></tr>
									</table>
									</div>
								</td>
							</tr>
							<tr>
								<td class="magin-bottom-10">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td width="100%" align="right" valign="middle">
									<table width="100%">
										<tr>
											<td align="left" valign="bottom">
												<span id="register-here">No? Register <a href="showRegistrationPage.htm" class="font-blue">here</a></span>
											</td>
											<td align="right" valign="bottom">
												<input type="button" class="white-button" onclick="loginUser()" value="   Login   "/>			
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
