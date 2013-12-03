<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Messenger</title>
		<link rel="stylesheet" type="text/css" href='<c:out value="${pageContext.request.contextPath}"/>/css/uditmsgr.css' />
		<script src='<c:out value="${pageContext.request.contextPath}"/>/js/jquery.min.js'></script>
		<script type='text/javascript'>

			var d = document;
			var sendMessageTextA;
			var displayMessageTextA;
			var participantsListTextA;
			var userName;
		
			function onloadHook(){
				userName = "${user}";
				
				sendMessageTextA = d.getElementById("chatSendMessageTextArea");
				displayMessageTextA = d.getElementById("chatDisplayMessageTextArea");
				participantsListTextA = d.getElementById("chatParticipantsListTextArea");
				
				sendMessageTextA.onkeydown = sendMessageByKeypress;
				sendMessageTextA.focus();
				
				fetchUpdates();	
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
							var ip = resp["user"]["ipAddress"];
							var name = resp["user"]["userName"];
							var isUserOnline = resp["user"]["online"];
							
							toggleUser(name, isUserOnline);
							
							//do not fetch update if the current update received is the user himself getting offline
							if(!(name == userName && !isUserOnline))
								fetchUpdates();
						}else if(updateType === "MESSAGE_UPDATE"){
							var userNam = resp["userMessage"]["user"]["userName"];
							var message = resp["userMessage"]["message"]["content"];
							displayMessageTextA.innerHTML += "<span class='user-said font-gray'>" + userNam + " : </span><span class='message font-gray'>" + message + "</span><br/>";
							displayMessageTextA.scrollTop = 9999999;
						
							fetchUpdates();
						}else if(updateType === "TERMINATE"){
							return;
						}else{
							alert("Unknown update from the server. Please notify Bernardo. It's a nasty issue.");
						}
					}
				});
			}
			
			function toggleUser(name, isOnline){
				if(isOnline){
					if(isUserAlreadyIntheList(name)){
						onlineUser(name, true);
					}else{
						participantsListTextA.innerHTML += stylizeParticipant(name, isOnline) + "<br/>";
					}
				}else{
					onlineUser(name, false);
				}
			}
			
			function isUserAlreadyIntheList(name){
				var existingContent = participantsListTextA.innerHTML;
				var participants = existingContent.split("<br>");
				
				for(var i = 0; i < participants.length; i++){
					var participant = participants[i];
					if(participant !== ""){
						if(strip(participant) === name){
							return true;	
						}
					}
				}
			}
			
			function stylizeParticipant(name, isOnline){
				return "<table border=0 cellSpacing=0 cellPadding=0 width='100%' style='float:left;'><tr><td width='20px' align='left'><img src='<c:out value="${pageContext.request.contextPath}"/>/images/user_online.png'/></td><td align='left'>" + name + "</td></tr></table>";
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
							alert("An error occured while sending message. Your message might not be saved. Please tell Bernardo of this nasty issue.");
						
						sendMessageTextA.value = "";
					}
				});
			}
			
			function logout(e){
				var choice = confirm("Close?");
				
				if(choice === true){
					$.ajax({
						cache: false,
						type: 'POST',
						url: 'logout.htm',
						data: {
							"userName" : userName
						},
						success : function(data){
							if(data === "OK")
								window.location.href = "showLogoutPage.htm";
							else{
								alert("Something serious happened. Please tell Bernardo.");
							}
						}
					}); 
				}else{
					e.preventDefault();
				}
			}
			
			function onlineUser(name, isOnline){
				var existingContent = participantsListTextA.innerHTML;
				var participants = existingContent.split("<br>");
				var newContent = "";
				
				for(var i = 0; i < participants.length; i++){
					var participant = participants[i];
					
					if(participant !== ""){
						if(strip(participant) === name){
							if(isOnline){
								participant = participant.replace("user_offline.png", "user_online.png");
							}else{
								participant = participant.replace("user_online.png", "user_offline.png");
							}
						}

						newContent += participant + "<br/>";
					}
				}
				
				participantsListTextA.innerHTML = newContent;
			}
			
			function sendMessageByKeypress(e){
				if (e.keyCode == 13){
			        sendMessage();
			    }
			}
			
			function strip(html){
			   var tmp = document.createElement("DIV");
			   tmp.innerHTML = html;
			   return tmp.textContent || tmp.innerText || "";
			}
		</script>
	</head>
	<body onload="onloadHook()">
		<!--  Chat Area -->
		<div id="chatArea">
			<table border=0 cellSpacing=0 cellPadding=0 height="100%" width="100%">
				<tr>
					<td colspan="2">
						<div class="top-options-container" style="padding-top: 5px; padding-right: 5px;">
							<div style="float:left;">
								<table border=0 cellSpacing=0 cellPadding=0 height="100%" width="100%">
									<tr><td rowspan="3"><img src='<c:out value="${pageContext.request.contextPath}"/>/images/logo.png' style="float:right; margin-top:3px; margin-left:10px;"></td></tr>
									<tr><td class="top-options-title"><c:out value="${user}"/></td></tr>
									<tr><td class="top-options-subtitle"><c:out value="${userIp}"/></td></tr>
								</table>
							</div>
							<img src='<c:out value="${pageContext.request.contextPath}"/>/images/logout.png' style="float:right; margin-top:7px;" class="top-options-imgs" onclick="logout()">
							<img src='<c:out value="${pageContext.request.contextPath}"/>/images/settings.png' style="float:right; margin-right: 5px; margin-top:7px;" class="top-options-imgs">
							<img src='<c:out value="${pageContext.request.contextPath}"/>/images/pen.png' style="float:right; margin-right: 8px; margin-top:7px;" class="top-options-imgs">
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<!-- <textarea id="chatDisplayMessageTextArea" class="chatTextArea" readonly></textarea> -->
						<div class="messages-area">
							<div id="chatDisplayMessageTextArea">
							</div>
						</div>
					</td>
					<td>
						<!-- <textarea id="chatParticipantsListTextArea" class="chatTextArea border-none" readonly></textarea> -->
						<div class="participants-area">
							<div id="chatParticipantsListTextArea"></div>
						</div>								
					</td>
				</tr>
				<tr>
					<td height="20%" width="75%" class="compose-area"><textarea id="chatSendMessageTextArea" class="chatTextArea"></textarea></td>
					<td height="20%" width="25%" class="compose-area" align="right">
						<input type="button" id="btnSend" class="white-button" onclick="sendMessage()" value="    Send    "/>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>