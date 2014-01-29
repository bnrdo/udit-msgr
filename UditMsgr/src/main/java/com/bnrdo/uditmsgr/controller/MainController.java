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
	
	@RequestMapping(value = "/showLogoutPage.htm", method = RequestMethod.GET)
	protected String showLogoutPage(HttpServletRequest request){
		return "logout";
	}
	
	@RequestMapping(value = "/showLoginPage.htm", method = RequestMethod.GET)
	protected String showLoginPage(HttpServletRequest request, ModelMap model){
		String ipAddress = request.getRemoteAddr();
		User user = chatSvc.findUserByIp(ipAddress);
		
		model.addAttribute("userIp", ipAddress);
		
		if(user != null){
			if(user.getStatus().equals(Status.OFFLINE)){
				model.addAttribute("preLoginMessage", "Your IP address is already registered.");
			}
			else if(user.getStatus().equals(Status.ONLINE)){
				model.addAttribute("preLoginMessage", "You are already logged in.");
			}
		}
		
		return "login";
	}
	
	@RequestMapping(value = "/logout.htm", method = RequestMethod.POST)
	protected @ResponseBody String logout(HttpServletRequest request){
		
		String userName = request.getParameter("userName");
		
		chatSvc.offlineUser(userName);
		
		return "OK";
	}
	
	@RequestMapping(value = "/login.htm", method = RequestMethod.POST)
	protected @ResponseBody String login(HttpServletRequest request){
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		User user = chatSvc.findUserByIp(ipAddress);
		
		if(user == null || !user.getUserName().equals(userName)){
			return "Invalid login.";
		}else{
			if(user.getStatus().equals(Status.ONLINE)){
				return "You are already logged in.";
			}else{
				chatSvc.onlineUser(userName);
				return "OK";
			}
		}
	}

}


