package com.bnrdo.uditmsgr.domain;

import com.bnrdo.uditmsgr.util.Constants.UpdateType;


public class UserUpdate extends Update {
	
	private User user;
	
	public UserUpdate(User user){
		this.user = new User(user.getUserName(), user.getIpAddress());
		this.user.setOnline(user.isOnline());
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
}
