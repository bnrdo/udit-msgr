package com.bnrdo.uditmsgr.controller;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bnrdo.uditmsgr.domain.Message;
import com.bnrdo.uditmsgr.domain.TerminateUpdate;
import com.bnrdo.uditmsgr.domain.Update;
import com.bnrdo.uditmsgr.domain.User;
import com.bnrdo.uditmsgr.domain.UserMessageUpdate;
import com.bnrdo.uditmsgr.domain.UserUpdate;
import com.bnrdo.uditmsgr.repo.DataStore;
import com.bnrdo.uditmsgr.service.ChatService;
import com.bnrdo.uditmsgr.service.UpdateService;
import com.bnrdo.uditmsgr.util.Constants.PageState;
import com.bnrdo.uditmsgr.util.Constants.Status;

@Controller
public class ChatboxController {
	
	@Autowired
	private UpdateService updateSvc;
	
	@Autowired
	private ChatService chatSvc;
	
	@RequestMapping(value = "/showChatbox.htm", method = RequestMethod.GET)
    protected String showChatbox(HttpServletRequest request, ModelMap model) throws Exception {
		String ipAddress = request.getRemoteAddr();
		User user = chatSvc.findUserByIp(ipAddress);
		
		//means the user has trigger refresh in illegal way. logout is trigger when the user navigates away from the chatbox
		if(user.getStatus().equals(Status.OFFLINE)){
			return "redirect:showLoginPage.htm";
		}else{
			String userName = user.getUserName();
			
			model.addAttribute("userIp", ipAddress);
			model.addAttribute("user", userName);
				
			chatSvc.loadOnlineSubscribersForUserView(userName);
					
			return "chatbox";
		}
	}
	
	@RequestMapping(value = "/fetchUpdates.htm", method = RequestMethod.GET)
    protected @ResponseBody String fetchUpdates(HttpServletRequest request) throws Exception {
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		System.out.println("|------------------------------------------------ fetch from " + userName);
		
		System.out.println("DataStore before the take : " + DataStore._Q);
		
		Update update = updateSvc.getUpdate(new User(userName, ipAddress));
		
		System.out.println("DataStore after the take : " + DataStore._Q);
		
		System.out.println("|------------------------------------------------ just got an update for " + userName + " : " + update);
		
		ObjectMapper mapper = new ObjectMapper();
		String response = "";
		
		switch(update.getUpdateType()){
			case USER_UPDATE:
				UserUpdate userUpdate = (UserUpdate) update;
				response = mapper.writeValueAsString(userUpdate);
				break;
				
			case MESSAGE_UPDATE:
				UserMessageUpdate userMessageUpdate = (UserMessageUpdate) update;
				response = mapper.writeValueAsString(userMessageUpdate);
				break;
				
			case TERMINATE:
				TerminateUpdate terminateUpdate = (TerminateUpdate) update;
				response = mapper.writeValueAsString(terminateUpdate);
				break;
		}
	
		return response;
	}
	
	@RequestMapping(value = "/sendMessage.htm", method = RequestMethod.POST)
    protected @ResponseBody String sendMessage(HttpServletRequest request) throws Exception {
		
		String userName = request.getParameter("userName");
		
		String message = request.getParameter("message");
		
		chatSvc.saveChatMessage(userName, new Message(message));
		
		return "OK";
	}
	
	@RequestMapping(value = "/changeName.htm", method = RequestMethod.POST)
	protected @ResponseBody String changeName(HttpServletRequest request){
		String userName = request.getParameter("userName");
		String newName = request.getParameter("newName");
		
		if(chatSvc.isUsernameTaken(newName)){
			return "User name already exists!";
		}else{
			chatSvc.changeUserName(userName, newName);
		}
		
		return "OK";
	}
}
