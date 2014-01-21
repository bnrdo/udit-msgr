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
		<style type="text/css">
			.input-field{
				background-attachment: scroll;
				background-clip: border-box;
				background-color: rgb(252, 252, 252);
				background-image: none;
				background-origin: padding-box;
				background-position: 0% 0%;
				background-repeat: repeat;
				background-size: auto auto;
				border-bottom-color: rgb(221, 221, 221);
				border-bottom-left-radius: 3px;
				border-bottom-right-radius: 3px;
				border-bottom-style: solid;
				border-bottom-width: 1px;
				border-image-outset: 0 0 0 0;
				border-image-repeat: stretch stretch;
				border-image-slice: 100% 100% 100% 100%;
				border-image-source: none;
				border-image-width: 1 1 1 1;
				border-left-color: rgb(221, 221, 221);
				border-left-style: solid;
				border-left-width: 1px;
				border-right-color: rgb(221, 221, 221);
				border-right-style: solid;
				border-right-width: 1px;
				border-top-color: rgb(221, 221, 221);
				border-top-left-radius: 3px;
				border-top-right-radius: 3px;
				border-top-style: solid;
				border-top-width: 1px;
				box-shadow: rgb(255, 255, 255) 1px 1px 0px 0px;
				color: rgb(51, 51, 51);
				font-family: Helvetica,arial,freesans,clean,sans-serif;
				font-size: 12px;
				font-size-adjust: none;
				font-stretch: normal;
				font-style: normal;
				font-variant: normal;
				font-weight: 400;
				line-height: 15px;
				margin-bottom: 0px;
				margin-left: 0px;
				margin-right: 0px;
				margin-top: 0px;
				min-height: 28px;
				outline-color: rgb(51, 51, 51);
				outline-style: none;
				outline-width: 0px;
				padding-bottom: 4px;
				padding-left: 8px;
				padding-right: 20px;
				padding-top: 4px;
				position: relative;
				text-shadow: none;
				vertical-align: middle;
				width: 220px;
				-moz-border-bottom-colors: none;
				-moz-border-left-colors: none;
				-moz-border-right-colors: none;
				-moz-border-top-colors: none;
				-moz-box-sizing: border-box;
				-moz-font-feature-settings: normal;
				-moz-font-language-override: normal;
			}
		</style>
		<script type="text/javascript">

			var d = document;
			var userNameTextB;
			
			function onloadHook(){
				
				userNameTextB = d.getElementById("userName");
				userNameTextB.focus();
				
				var pageState = "<c:out value='${pageState}'/>";
				
				if(pageState === 'VALID'){
					doValidState();
				}else if(pageState === 'INVALID'){
					doInvalidState();
				}else{
					doInitState();
				}
			}
			
			function doValidState(){
				
				alert("User successfully registered. You will be logged in automatically.");
				
				window.location.href = "main.htm";
			}
			
			function doInvalidState(){
				
			}
			
			function doInitState(){
			}
		
			function registerUser(){
				d.forms[0].submit();
			}
		</script>
	</head>
	<body onload="onloadHook()">
		<!-- Register Area -->
		<div id="login-area">
			<table width="100%" height="100%"><tr><td align="center" valign="middle">
				<form:form method="post" action="registerUser.htm" commandName="registrationForm" id="registrationForm">
					<table class="gray-cute-outline padding-bottom-15">
						<tr>
							<td colspan="2" align="left" class="blue-title-14 padding-bottom-10">Registration Form</td>
						</tr>
						<tr>
							<td align="left" valign="middle" class="register-label">User Name</td>
							<td align="left" class="padding-bottom-2"><form:input path="userName" id="userName" cssClass="input-field"/></td>
						</tr>
						<tr>
							<td align="left" valign="middle" class="register-label">Full Name</td>
							<td align="left" class="padding-bottom-2"><form:input path="fullName" id="fullName" cssClass="input-field"/></td>
						</tr>
						<tr>
							<td align="left" valign="middle" class="register-label padding-right-10">Mother's Name</td>
							<td align="left" class="padding-bottom-2"><form:input path="motherName" id="motherName" cssClass="input-field"/></td>
						</tr>
						<tr>
							<td align="left" valign="middle" class="register-label padding-right-10">Father's Name</td>
							<td align="left" class="padding-bottom-2"><form:input path="fatherName" id="fatherName" cssClass="input-field"/></td>
						</tr>
						<tr>
							<td align="left" valign="middle" class="register-label">Status</td>
							<td align="left" class="padding-bottom-2"><form:input path="personalStatus" id="personalStatus" cssClass="input-field"/></td>
						</tr>
						<tr>
							<td align="left" valign="middle" class="register-label">Hobbies</td>
							<td align="left" class="padding-bottom-2"><form:input path="hobby" id="hobby" cssClass="input-field"/></td>
						</tr>
						<tr>
							<td align="left" valign="middle" class="register-label">Motto</td>
							<td align="left" class="padding-bottom-2"><form:input path="motto" id="motto" cssClass="input-field"/></td>
						</tr>
						<tr>
							<td align="right" valign="middle" colspan="2" class="padding-top-5">
								<table width="100%">
									<tr>
										<td align="right" valign="bottom"><input type="button" class="white-button" onclick="registerUser()" value="  Register "/></td>
										<td align="right" valign="bottom" width="15%"><input type="button" class="white-button" onclick="window.location.href = 'main.htm';" value="  Back "/></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</form:form>
			</td></tr></table>
		</div>
	</body>
</html>
