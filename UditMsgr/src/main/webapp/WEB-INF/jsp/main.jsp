<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Judith Messenger!</title>
		<script src='<c:out value="${pageContext.request.contextPath}"/>/js/jquery.min.js'></script>
		<script type='text/javascript'>
		
			var d = document;
			var sendMessageTextA;
			var displayMessageTextA;
			var participantsListTextA;
			var newUserTextB;
			var userName;
			
			function onloadHook(){
				 userName = "${user}";
				
				if(!isUserNew()){
					sendMessageTextA = d.getElementById("chatSendMessageTextArea");
					displayMessageTextA = d.getElementById("chatDisplayMessageTextArea");
					participantsListTextA = d.getElementById("chatParticipantsListTextArea");
					
					sendMessageTextA.onkeydown = sendMessageByKeypress;
					window.onbeforeunload = offlineUser;
					sendMessageTextA.focus();
					
					fetchUpdates();	
				}else{
					newUserTextB = d.getElementById("txtUserID");
					newUserTextB.onkeydown = registerUserByKeyPress;
					newUserTextB.focus();
				}
					
			}
			
			function isUserNew(){
				if(userName === "")
					return true;
				
				return false;
			}
		
			function fetchUpdates(){
				$.ajax({
					cache: false,
					type: 'GET',
					data : {
						"userName" : userName
					},
					url: 'fetchUpdates.htm',
					success: function(data){
						var resp = $.parseJSON(data);
						var updateType = resp["updateType"];
						
						if(updateType === "USER_UPDATE"){
							var userIp = resp["user"]["ipAddress"];
							var userNam = resp["user"]["userName"];
							var userAndIp = userNam + "[" + userIp + "]";
							var isUserOnline = resp["user"]["online"];
							
							if(isUserOnline === true){
								participantsListTextA.innerHTML += userAndIp + "<br/>";

								fetchUpdates();
							}
							else
								removeUserFromParticipantsList(userNam, userIp);
						}else{
							var userNam = resp["userMessage"]["user"]["userName"];
							var message = resp["userMessage"]["message"]["content"];
							displayMessageTextA.innerHTML += "<span class='user-said font-gray'>" + userNam + " : </span><span class='message font-gray'>" + message + "</span><br/>";
							displayMessageTextA.scrollTop = 9999999;
						
							fetchUpdates();
						}
					}
				});
			}
			
			function sendMessage(){
				var message = sendMessageTextA.value;
				
				$.ajax({
					cache: false,
					type: 'POST',
					url: 'sendMessage.htm',
					data: {
						"message" : message,
						"userName" : userName
					},
					success: function(data){
						if(data === "NOK")
							alert("An error occured while sending message. Your message might not be saved.")
						
						sendMessageTextA.value = "";
					}
				});
			}
			
			function registerUser(){
				
				var newUserID = newUserTextB.value;
				
				if(newUserID.trim() === ''){
					alert("Username should not be blank");
					return;
				}
				
				window.location.href = 'registerUser.htm?userName=' + newUserID;
			}
			
			function sendMessageByKeypress(e){
				if (e.keyCode == 13){
			        sendMessage();
			    }
			}
			
			function registerUserByKeyPress(e){
				if (e.keyCode == 13){
					registerUser();
			    }
			}
			
			function offlineUser(e){
				var choice = confirm("Close?");
				
				if(choice === true){
					$.ajax({
						cache: false,
						type: 'POST',
						url: 'offlineUser.htm',
						data: {
							"userName" : userName
						}
					});
					
					alert("Bye");
				}else{
					e.preventDefault();
				}
			}
			
			function removeUserFromParticipantsList(userNam, ip){
				var existingContent = participantsListTextA.innerHTML;
				var newContent = "";
				var users = existingContent.split("<br>");
				
				for(var i = 0; i < users.length; i++){
					if(users[i].indexOf("[") !== -1){
						if(users[i].split("[")[0] === userNam){
							continue;
						}else{
							newContent += users[i] + "<br/>";
						}
					}
				}
				
				participantsListTextA.innerHTML = newContent;
			}
			
		</script>
		
		<style type="text/css">
			* {margin: 0px; padding: 0px;}
			.border-none{
				border: none;
			}
			.chatTextArea {
				resize:none;
			    width: 100%;
			    height: 100%;
			    box-sizing: border-box;
			}
			.font-gray{
				color: 			#5A696B !important;
			}
			.message{
				color: 			#00000;
				font-family: 	Verdana;
				font-size: 		10px;
				height: 		16px;
				text-decoration: none;
			}
			.user-said{
				color: 			#00000;
				font-family: 	Verdana;
				font-size: 		10px;
				font-weight: 	bold;
				height: 		16px;
				text-decoration: none;
				
			}
			.user-online{
				color: 			#00000;
				font-family: 	Verdana;
				font-size: 		10px;
				height: 		16px;
				text-decoration: none;
			}
			#chatDisplayMessageTextArea{
				border: 1px solid #2C6700;
				display:inline-block;
				height: 300px;
				overflow-x: hidden;
				overflow-y: auto;
				/* padding: 5px 5px 5px 5px; */
				width: 100%;
			}
			#chatParticipantsListTextArea{
				border: 1px solid #2C6700;
				display:inline-block;
				height: 300px;
				overflow-x: hidden;
				overflow-y: auto;
				width: 100%;
			}
		 	#btnSend{
		 		width: 100%;
			    height: 100%;
			    box-sizing: border-box;
			}
		 	#chatArea{position: absolute; top:0; bottom: 0; left: 0; right: 0; margin: auto;}
			#registerArea{border: 1px solid green;position: absolute; top:0; bottom: 0; left: 0; right: 0; margin: auto;}
			.display-none{display: none;}
			.magin-bottom-10{margin-bottom: 10px;}
			.padding-10{ padding: 10px 10px 10px 10px;}
			.padding-top-10{ padding-top: 10px;}
			.padding-bottom-10{ padding-bottom: 10px;}
			.padding-top-right-10{ padding: 10px 10px 0px 0px;}
			.padding-top-right-bottom-10{padding: 10px 10px 10px 0px;}
			.padding-top-right-left-10{padding: 10px 10px 0px 10px;}
			.white-button {background-color: white; color: #888888; 
							border-spacing: 0; border-collapse: collapse; 
							border: ridge 1px #888888;}
		</style>
	</head>
	<body onload="onloadHook()">
			<c:if test="${empty user}">
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
			</c:if>
			
			<c:if test="${not empty user}">
				<!--  Chat Area -->
				<div id="chatArea">
					<table border=0 cellSpacing=0 cellPadding=0 height="100%" width="100%">
						<tr>
							<td height="80%" class="padding-top-right-left-10">
								<!-- <textarea id="chatDisplayMessageTextArea" class="chatTextArea" readonly></textarea> -->
								<div id="chatDisplayMessageTextArea"></div>
							</td>
							<td class="padding-top-right-10">
								<!-- <textarea id="chatParticipantsListTextArea" class="chatTextArea border-none" readonly></textarea> -->
								<div id="chatParticipantsListTextArea" class="user-online"></div>								
							</td>
						</tr>
						<tr>
							<td height="20%" width="75%" class="padding-10"><textarea id="chatSendMessageTextArea" class="chatTextArea"></textarea></td>
							<td height="20%" width="25%" class="padding-top-right-bottom-10" align="right"><input type="button" id="btnSend" class="white-button" onclick="sendMessage()" value="    Send    "/></td>
						</tr>
					</table>
				</div>
			</c:if>
	</body>
</html>
