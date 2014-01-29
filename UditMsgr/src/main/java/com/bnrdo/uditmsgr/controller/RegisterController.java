package com.bnrdo.uditmsgr.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bnrdo.uditmsgr.domain.User;
import com.bnrdo.uditmsgr.service.ChatService;
import com.bnrdo.uditmsgr.util.Constants.PageState;

@Controller
public class RegisterController {
	
	@Autowired
	private ChatService chatSvc;

	@RequestMapping(value = "/registerUser.htm", method = RequestMethod.POST)
	protected String registerUser(@ModelAttribute User user, HttpServletRequest request, ModelMap model){
		
		String userName = user.getUserName();
		String ipAddress = request.getRemoteAddr();
		
		user.setIpAddress(ipAddress);
		
		boolean isUsernameTaken = chatSvc.isUsernameTaken(userName);
		
		model.addAttribute("registrationForm", user);
		
		if(!isUsernameTaken){
			chatSvc.registerUser(user);
			chatSvc.onlineUser(userName);
			
			model.addAttribute("pageState", PageState.VALID);
			
			return "register";
		}else{
			model.addAttribute("pageState", PageState.INVALID);
			model.addAttribute("errorMessage", "Username is already taken.");
			
			return "register";
		}
	}
	
	@RequestMapping(value = "/showRegistrationPage.htm", method = RequestMethod.GET)
	protected String showRegistrationPage(HttpServletRequest request, ModelMap model){
		
		String ipAddress = request.getRemoteAddr();
		
		//if there is a user already registered for this ip
		if(chatSvc.findUserByIp(ipAddress) != null){
			return "redirect:showLoginPage.htm";
		}
		
		model.addAttribute("pageState", PageState.INIT);
		model.addAttribute("registrationForm", new User());
		
		return "register";
	}
	
}
