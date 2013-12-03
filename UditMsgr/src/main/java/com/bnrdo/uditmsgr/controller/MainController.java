package com.bnrdo.uditmsgr.controller;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bnrdo.uditmsgr.dao.ChatRepository;
import com.bnrdo.uditmsgr.domain.Message;
import com.bnrdo.uditmsgr.domain.Update;
import com.bnrdo.uditmsgr.domain.User;
import com.bnrdo.uditmsgr.domain.UserMessageUpdate;
import com.bnrdo.uditmsgr.domain.UserUpdate;

@Controller
public class MainController {
	
	@Autowired
	private ChatRepository repo;
	
	@RequestMapping(value = "/main.htm", method = RequestMethod.GET)
    protected String showChat(HttpServletRequest request, ModelMap model) throws Exception {
		String ipAddress = request.getRemoteAddr();
		User user = repo.findUserByIp(ipAddress);
		
		if(user != null){
			if(!user.isOnline()){
				model.addAttribute("user", user.getUserName());
				model.addAttribute("userIp", user.getIpAddress());
				repo.onlineSubscriber(user);
				repo.loadOnlineSubscribersForUserView(user);
				
				return "chatbox";
			}else{
				//return "you-are-already";
				return "chatbox";
			}
		}
		
		return "login";
	}
	
	@RequestMapping(value = "/fetchUpdates.htm", method = RequestMethod.GET)
    protected @ResponseBody String fetchUpdates(HttpServletRequest request) throws Exception {
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		System.out.println("|------------------------------------------------ fetch from " + userName);
		
		Update update = repo.getUpdate(new User(userName, ipAddress));
		
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
		}
	
		return response;
	}
	
	@RequestMapping(value = "/sendMessage.htm", method = RequestMethod.POST)
    protected @ResponseBody String sendMessage(HttpServletRequest request) throws Exception {
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		String message = request.getParameter("message");
		
		repo.saveChatMessage(new User(userName, ipAddress), new Message(message));
		
		return "OK";
	}
	
	@RequestMapping(value = "/registerUser.htm", method = RequestMethod.GET)
	protected String registerUser(HttpServletRequest request, ModelMap model){
		
		String userName = request.getParameter("userName").trim();
		String ipAddress = request.getRemoteAddr();
		boolean isUsernameTaken = repo.isUsernameExisting(userName);
		
		if(!isUsernameTaken){
			repo.subscribe(new User(userName, ipAddress));
			return "redirect:/main.htm";
		}else{
			model.addAttribute("isUsernameTaken", true);
			model.addAttribute("user", userName);
			return "login";
		}
	}
	
	@RequestMapping(value = "/logout.htm", method = RequestMethod.POST)
	protected @ResponseBody String logout(HttpServletRequest request){
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		repo.offlineSubscriber(new User(userName, ipAddress));
		
		return "OK";
	}
	
	@RequestMapping(value = "/showLogoutPage.htm", method = RequestMethod.GET)
	protected String showLogoutPage(HttpServletRequest request){
		return "logout";
	}
}
