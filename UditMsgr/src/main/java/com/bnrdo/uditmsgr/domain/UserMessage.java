package com.bnrdo.uditmsgr.domain;

public class UserMessage{
	
	private User user;
	private Message message;
	
	public UserMessage(User user, Message message){
		this.user = user;
		this.message = message;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
}
