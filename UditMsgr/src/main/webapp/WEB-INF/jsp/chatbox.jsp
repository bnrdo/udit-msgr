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
		
			window.onresize = function(){
			    //window.resizeTo(408,491);
			};

			var d = document;
			var ctxPath;
			var sendMessageTextA;
			var displayMessageTextA;
			var participantsListTextA;
			var userNameContainer;
			var userName;
			
			var displayUserDiv;
			var changeUserDiv;
		
			function onloadHook(){
				userName = "${user}";
				ctxPath = '<c:out value="${pageContext.request.contextPath}"/>';
				userNameContainer = d.getElementById("userNameContainer");
				sendMessageTextA = d.getElementById("chatSendMessageTextArea");
				displayMessageTextA = d.getElementById("chatDisplayMessageTextArea");
				participantsListTextA = d.getElementById("chatParticipantsListTextArea");
				
				displayUserDiv = d.getElementById('display-user');
				changeUserDiv = d.getElementById('change-user');
				
				sendMessageTextA.onkeydown = sendMessageByKeypress;
				sendMessageTextA.focus();
				
				$(window).bind('beforeunload', function(){
					//note matitrigger din toh pag nag logout via confirmation
				});
				
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
							var status = resp["user"]["status"];
							
							toggleUser(name, status);
							
							//do not fetch update if the current update received is the user himself getting offline
							if(!(name == userName && status == "OFFLINE"))
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
			
			function toggleUser(name, status){
				
				if(status === "ONLINE"){
					if(isUserAlreadyIntheList(name)){
						onlineUser(name, true);
					}else{
						participantsListTextA.innerHTML += stylizeParticipant(name, status) + "<br/>";
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
				return "<table border=0 cellSpacing=0 cellPadding=0 width='100%' style='float:left;'><tr><td width='20px' align='left'><img src='" + ctxPath + "/images/user_online.png'/></td><td align='left' class='font-black'>" + name + "</td></tr></table>";
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
			
			function confirmLogout(e){
				var choice = confirm("Close?");
				
				if(choice === true){
					logout(userName, 
						function(data){
							if(data === "OK")
								window.location.href = "showLogoutPage.htm";
							else{
								alert("Something serious happened. Please tell Bernardo.");
							}
						}
					);
				}else{
					e.preventDefault();
				}
			}
			
			function logout(userName, successCallback){
				$.ajax({
					cache: false,
					type: 'POST',
					url: 'logout.htm',
					data: {
						"userName" : userName
					},
					success : successCallback
				}); 
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
								participant = participant.replace("user_offline.png", "user_online.png")
															.replace("class=\"font-gray\"", "class=\"font-black\"");
							}else{
								participant = participant.replace("user_online.png", "user_offline.png")
															.replace("class=\"font-black\"", "class=\"font-gray\"");
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
			
			function editName(){
				displayUserDiv.style.display = 'none';
				changeUserDiv.style.display = 'block';
				
				d.getElementById("txtEditName").select();
			}
			
			function cancelEditName(){
				displayUserDiv.style.display = 'block';
				changeUserDiv.style.display = 'none';
			}
			
			function saveEditedName(){
				var newName = d.getElementById("txtEditName").value;
				
				if(userName === newName){
					alert("Nothing has changed.");
				}else{
					
					var choice = confirm("Warning. On success, system will log you out for the username change to take effect. Continue?");
					
					if(choice){
						$.ajax({
							cache: false,
							type: 'POST',
							url: 'changeName.htm',
							data: {
								"userName" : userName,
								"newName" : newName
							},
							success : function(data){
								if(data === "OK"){
									logout(newName, function(data){
										if(data === "OK"){
											if(reason === "name change")
											alert("Name successfully changed. Please login again for the changes to take effect.");
											window.location.href = "showLogoutPage.htm";
										}
										else{
											alert("Something serious happened. Please tell Bernardo.");
										}
									});
								}
								else{
									
									var txt = d.getElementById("txtEditName");
									
									txt.value = userName; 
									txt.select();
								}
							}
						});
					}else{
						return;
					}
				}
			}
		</script>
	</head>
	<body onload="onloadHook()">
		<!--  Chat Area -->
		<div id="chatArea">
			<table border=0 cellSpacing=0 cellPadding=0 height="100%" width="100%">
				<tr>
					<td colspan="2">
						<div class="top-options-container" style="padding-top: 7px; padding-right: 5px;">
							<div style="float:left;" id="display-user">
								<table border=0 cellSpacing=0 cellPadding=0 height="100%" width="100%">
									<tr><td rowspan="3"><img src='<c:out value="${pageContext.request.contextPath}"/>/images/logo.png' class='main-logo'></td></tr>
									<tr><td class="top-options-title" align="left"><c:out value="${user}"/></td></tr>
									<tr><td class="top-options-subtitle" align="left"><c:out value="${userIp}"/></td></tr>
								</table>
							</div>
							<div style="float:left; display:none;" id="change-user">
								<table border=0 cellSpacing=0 cellPadding=0 height="100%" width="100%">
									<tr><td rowspan="3"><img src='<c:out value="${pageContext.request.contextPath}"/>/images/logo.png' class='main-logo'></td></tr>
									<tr><td class="top-options-title" align="left" valign="bottom">
										<input type='text' id='txtEditName' value='<c:out value="${user}"/>' class='change-user-text'/>
										<img onclick='saveEditedName()' class='float-left top-options-imgs' src='<c:out value="${pageContext.request.contextPath}"/>/images/check.png' class='top-options-imgs'/>
										<img onclick='cancelEditName()' class='float-left top-options-imgs' src='<c:out value="${pageContext.request.contextPath}"/>/images/cancel.png' class='top-options-imgs'/>
									</td></tr>
									<tr><td class="top-options-subtitle" align="left"><c:out value="${userIp}"/></td></tr>
								</table>
							</div>
							<img src='<c:out value="${pageContext.request.contextPath}"/>/images/logout.png' style="margin-top:7px;" class="top-options-imgs float-right" onclick="confirmLogout()">
							<img src='<c:out value="${pageContext.request.contextPath}"/>/images/settings.png' style="margin-right: 5px; margin-top:7px;" class="top-options-imgs float-right">
							<img src='<c:out value="${pageContext.request.contextPath}"/>/images/attach.png' style="margin-right: 8px; margin-top:7px;" class="top-options-imgs float-right">
							<img src='<c:out value="${pageContext.request.contextPath}"/>/images/smiley.png' style="margin-right: 8px; margin-top:7px;" class="top-options-imgs float-right">
							<img src='<c:out value="${pageContext.request.contextPath}"/>/images/pen.png' style="margin-right: 8px; margin-top:7px;" class="top-options-imgs float-right" onclick="editName()">
						</div>
					</td>
				</tr>
				<tr>
					<td style="padding-left:1px;">
						<!-- <textarea id="chatDisplayMessageTextArea" class="chatTextArea" readonly></textarea> -->
						<div class="messages-area">
							<div id="chatDisplayMessageTextArea">
							</div>
						</div>
					</td>
					<td style="padding-right:1px;">
						<!-- <textarea id="chatParticipantsListTextArea" class="chatTextArea border-none" readonly></textarea> -->
						<div class="participants-area">
							<div id="chatParticipantsListTextArea"></div>
						</div>								
					</td>
				</tr>
				<tr>
					<td height="20%" width="75%" class="compose-area"><textarea id="chatSendMessageTextArea" class="chatTextArea"></textarea></td>
					<td height="20%" width="25%" style="padding-top: 4px; padding-left: 2px;" align="center" valign="top">
						<input type="button" id="btnSend" class="white-button" onclick="sendMessage()" value="Send"/>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>