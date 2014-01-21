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
public class MainController {
	
	@Autowired
	private ChatService chatSvc;
	
	@Autowired
	private UpdateService updateSvc;
	
	@RequestMapping(value = "/main.htm", method = RequestMethod.GET)
    protected String showChat(HttpServletRequest request, ModelMap model) throws Exception {
		String ipAddress = request.getRemoteAddr();
		User user = chatSvc.findUserByIp(ipAddress);
		
		model.addAttribute("userIp", ipAddress);
		
		if(user != null){
			
			model.addAttribute("user", user.getUserName());
			Status status = user.getStatus();
			
			if(status.equals(Status.ONLINE)){
				chatSvc.loadOnlineSubscribersForUserView(user);
				
				return "chatbox";
			}else{
				model.addAttribute("preLoginMessage", "Your IP address is already registered.");
			}
		}
		
		return "login";
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
		String ipAddress = request.getRemoteAddr();
		
		String message = request.getParameter("message");
		
		chatSvc.saveChatMessage(new User(userName, ipAddress), new Message(message));
		
		return "OK";
	}
	
	@RequestMapping(value = "/registerUser.htm", method = RequestMethod.POST)
	protected String registerUser(@ModelAttribute User user, HttpServletRequest request, ModelMap model){
		
		String userName = user.getUserName();
		String ipAddress = request.getRemoteAddr();
		
		user.setIpAddress(ipAddress);
		
		boolean isUsernameTaken = chatSvc.isUsernameTaken(userName);
		
		model.addAttribute("registrationForm", user);
		
		if(!isUsernameTaken){
			chatSvc.registerUser(user);
			chatSvc.onlineUser(user);
			
			model.addAttribute("pageState", PageState.VALID);
			
			return "register";
		}else{
			model.addAttribute("pageState", PageState.INVALID);
			model.addAttribute("errorMessage", "Username is already taken.");
			
			return "register";
		}
	}
	
	@RequestMapping(value = "/logout.htm", method = RequestMethod.POST)
	protected @ResponseBody String logout(HttpServletRequest request){
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		chatSvc.offlineUser(new User(userName, ipAddress));
		
		return "OK";
	}
	
	@RequestMapping(value = "/login.htm", method = RequestMethod.POST)
	protected @ResponseBody String login(HttpServletRequest request){
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		boolean isLoginValid = chatSvc.isLoginValid(userName, ipAddress);
		
		if(!isLoginValid){
			return "Invalid login.";
		}else{
			chatSvc.onlineUser(new User(userName, ipAddress));
			return "OK";
		}
	}
	
	@RequestMapping(value = "/showLogoutPage.htm", method = RequestMethod.GET)
	protected String showLogoutPage(HttpServletRequest request){
		return "logout";
	}
	
	@RequestMapping(value = "/showRegistrationPage.htm", method = RequestMethod.GET)
	protected String showRegistrationPage(HttpServletRequest request, ModelMap model){
		
		String ipAddress = request.getRemoteAddr();
		
		//if there is a user already registered for this ip
		if(chatSvc.findUserByIp(ipAddress) != null){
			return "redirect:main.htm";
		}
		
		model.addAttribute("pageState", PageState.INIT);
		model.addAttribute("registrationForm", new User());
		
		return "register";
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
