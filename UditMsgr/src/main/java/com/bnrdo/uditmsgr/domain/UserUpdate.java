package com.bnrdo.uditmsgr.domain;

import com.bnrdo.uditmsgr.util.Constants.UpdateType;


public class UserUpdate extends Update {
	
	private User user;
	
	public UserUpdate(User user){
		this.user = new User(user.getUserName(), user.getIpAddress());
		this.user.setStatus(user.getStatus());
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public UpdateType getUpdateType() {
		return UpdateType.USER_UPDATE;
	}
	
	@Override
	public String toString(){
		return "Update : User update | User : name=" + user.getUserName() + ", ipAddress=" + user.getIpAddress() + ", status=" + user.getStatus();
	}
}
