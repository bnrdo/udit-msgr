package com.bnrdo.uditmsgr.service;

import java.util.List;

import com.bnrdo.uditmsgr.domain.Message;
import com.bnrdo.uditmsgr.domain.User;

public interface ChatService {
	void onlineUser(User user);
	void offlineUser(User user);
	void registerUser(User user);
	void changeUserName(String currentUserName, String newUserName);
	void saveChatMessage(User user, Message message);
	boolean isLoginValid(String userName, String ipAddress);
	boolean isUsernameTaken(String userName);
	User findUserByIp(String userIp);
	void loadOnlineSubscribersForUserView(User user);
	List<String> getOnlineSubcribers();
	List<String> getOfflineSubcribers();
}
