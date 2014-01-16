package com.bnrdo.uditmsgr.repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.bnrdo.uditmsgr.domain.Update;
import com.bnrdo.uditmsgr.domain.User;
import com.bnrdo.uditmsgr.domain.UserMessage;

public class DataStore {
	public static Map<String, BlockingQueue<Update>> _Q;
	public static List<User> subscribers;
	public static List<UserMessage> userMessages;
	
	static{
		_Q = new HashMap<String, BlockingQueue<Update>>();
		subscribers = new ArrayList<User>();
		userMessages = new ArrayList<UserMessage>();
	}
}
