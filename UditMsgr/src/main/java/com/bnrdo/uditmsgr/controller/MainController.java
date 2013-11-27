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
		
		//-- test code
		/*String theMockPoint = request.getParameter("userName");
		  
		if(theMockPoint != null){
			String ipAddress = theMockPoint.split("-")[1];
			
			//String ipAddress = request.getRemoteAddr();
			User user = repo.findUserByIp(ipAddress);
			
			if(user != null){
				model.addAttribute("user", theMockPoint);
				repo.onlineSubscriber(user);
				repo.loadOnlineSubscribersForUserView(user);
				//repo.loadAllMessagesForUserView(user);
			}
		}*/
		//-- end test code
		
		String ipAddress = request.getRemoteAddr();
		User user = repo.findUserByIp(ipAddress);
		
		if(user != null){
			if(!user.isOnline()){
				model.addAttribute("user", user.getUserName());
				repo.onlineSubscriber(user);
				repo.loadOnlineSubscribersForUserView(user);
			}else{
				return "you-are-already";
			}
		}
		
		return "main";
	}
	
	@RequestMapping(value = "/fetchUpdates.htm", method = RequestMethod.GET)
    protected @ResponseBody String fetchUpdates(HttpServletRequest request) throws Exception {
		/*String theMockPoint = request.getParameter("userName");
		String userName = theMockPoint.split("-")[0];
		String ipAddress = theMockPoint.split("-")[1];*/
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		System.out.println("|------------------------------------------------ fetch from " + userName);
		
		Update update = repo.getUpdate(new User(userName, ipAddress));
		
		System.out.println("|------------------------------------------------ just got an update : " + update);
		
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
		/*String theMockPoint = request.getParameter("userName");
		String userName = theMockPoint.split("-")[0];
		String ipAddress = theMockPoint.split("-")[1];*/
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		String message = request.getParameter("message");
		
		repo.saveChatMessage(new User(userName, ipAddress), new Message(message));
		
		return "OK";
	}
	//problem is, nagsstretch din ung div pag nag exceed ung content, kasi nga naka width 100% sya, jusko panu kaya un. ahuhuhuhuhuhu
	@RequestMapping(value = "/registerUser.htm", method = RequestMethod.GET)
	protected String registerUser(HttpServletRequest request){
		/*String theMockPoint = request.getParameter("userName");
		String userName = theMockPoint.split("-")[0];
		String ipAddress = theMockPoint.split("-")[1];*/
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		//chatService.saveUser(newUserId, ipAddress);
		repo.subscribe(new User(userName, ipAddress));
		
		//return "redirect:/main.htm?userName=" + theMockPoint;
		return "redirect:/main.htm";
	}
	
	@RequestMapping(value = "/offlineUser.htm", method = RequestMethod.POST)
	protected void offlineUser(HttpServletRequest request){
		/*String theMockPoint = request.getParameter("userName");
		String userName = theMockPoint.split("-")[0];
		String ipAddress = theMockPoint.split("-")[1];*/
		
		String userName = request.getParameter("userName");
		String ipAddress = request.getRemoteAddr();
		
		repo.offlineSubscriber(new User(userName, ipAddress));
	}
}
