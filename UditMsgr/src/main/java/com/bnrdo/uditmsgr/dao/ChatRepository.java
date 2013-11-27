package com.bnrdo.uditmsgr.dao;

import com.bnrdo.uditmsgr.domain.Message;
import com.bnrdo.uditmsgr.domain.Update;
import com.bnrdo.uditmsgr.domain.User;

public interface ChatRepository {
	void subscribe(User user);
	User findUserByIp(String user);
	Update getUpdate(User user) throws InterruptedException;
	void saveChatMessage(User user, Message message);
	void onlineSubscriber(User user);
	void offlineSubscriber(User user);
	void loadOnlineSubscribersForUserView(User user);
	void loadAllMessagesForUserView(User user);
}
