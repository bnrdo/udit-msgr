package com.bnrdo.uditmsgr.service;

import java.util.List;

import com.bnrdo.uditmsgr.domain.Message;
import com.bnrdo.uditmsgr.domain.User;

public interface ChatService {
	void onlineUser(String userName);
	void offlineUser(String userName);
	void registerUser(User user);
	void changeUserName(String currentUserName, String newUserName);
	void saveChatMessage(String userName, Message message);
	boolean isUsernameTaken(String userName);
	User findUserByIp(String userIp);
	User findUser(String userName);
	void loadOnlineSubscribersForUserView(String userName);
	List<String> getOnlineSubcribers();
	List<String> getOfflineSubcribers();
}
