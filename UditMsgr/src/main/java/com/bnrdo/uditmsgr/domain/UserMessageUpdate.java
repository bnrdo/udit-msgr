package com.bnrdo.uditmsgr.domain;

import com.bnrdo.uditmsgr.util.Constants.UpdateType;

public class UserMessageUpdate extends Update{

	private UserMessage userMessage;
	
	public UserMessageUpdate(UserMessage userMessage){
		this.userMessage = new UserMessage(userMessage.getUser(), userMessage.getMessage());
	}
	
	public UserMessage getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(UserMessage userMessage) {
		this.userMessage = userMessage;
	}

	@Override
	public UpdateType getUpdateType() {
		return UpdateType.MESSAGE_UPDATE;
	}
}
