package com.bnrdo.uditmsgr.domain;

public class Message implements Comparable<Message>{
	
	private String content;
	private long timeSent;
	
	public Message(String content){
		this.content = content;
		this.timeSent = System.currentTimeMillis();
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTimeSent() {
		return timeSent;
	}
	public void setTimeSent(long timeSent) {
		this.timeSent = timeSent;
	}
	@Override
	public int compareTo(Message message) {
		int retVal = (int) (timeSent - message.getTimeSent());

	    return retVal;
	}
	
}
