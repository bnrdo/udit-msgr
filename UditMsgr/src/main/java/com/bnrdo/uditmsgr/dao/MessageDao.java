package com.bnrdo.uditmsgr.dao;

import java.sql.SQLException;
import java.util.List;

import com.bnrdo.uditmsgr.domain.Message;
import com.bnrdo.uditmsgr.domain.User;

public interface MessageDao{
	List<String> getMessages();
	void addMessage(String message);
	void saveUser(User user) throws SQLException;
	void saveUserMessage(User user, Message chatMessage) throws SQLException;
	String findUserByIp(String ipAddress) throws SQLException;
}
