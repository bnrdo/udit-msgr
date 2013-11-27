package com.bnrdo.uditmsgr.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Repository;

import com.bnrdo.uditmsgr.dao.ChatRepository;
import com.bnrdo.uditmsgr.domain.Message;
import com.bnrdo.uditmsgr.domain.Update;
import com.bnrdo.uditmsgr.domain.User;
import com.bnrdo.uditmsgr.domain.UserMessage;
import com.bnrdo.uditmsgr.domain.UserMessageUpdate;
import com.bnrdo.uditmsgr.domain.UserUpdate;
import com.bnrdo.uditmsgr.util.DatabaseUtil;

@Repository
public class ChatRepositoryImpl implements ChatRepository{
	
	private Map<User, BlockingQueue<Update>> _Q;
	private List<User> subscribers;
	private List<UserMessage> userMessages;
	
	ChatRepositoryImpl(){
		subscribers = new ArrayList<User>();
		userMessages = new ArrayList<UserMessage>();
		_Q = new HashMap<User, BlockingQueue<Update>>();
	}

	@Override
	public void subscribe(User user) {
		subscribers.add(user);
		_Q.put(user, new LinkedBlockingQueue<Update>());
		
		DatabaseUtil.executeStatement("INSERT INTO USER(USERNAME, IP_ADDRESS) VALUES('" + user.getUserName() + "','" + user.getIpAddress() + "')");
	}

	@Override
	public User findUserByIp(String ip) {
		
		for(User subscriber : subscribers){
			if(subscriber.getIpAddress().equals(ip))
				return subscriber;
		}
		
		return null;
	}

	@Override
	public Update getUpdate(User user) throws InterruptedException {
		BlockingQueue<Update> q = _Q.get(user);
		Update update = q.take();
		
		return update;
	}

	@Override
	public void saveChatMessage(User user, Message message) {
		userMessages.add(new UserMessage(user, message));
		
		//broadcast to subscribed user's message q
		for(Entry<User, BlockingQueue<Update>> s : _Q.entrySet()){
			BlockingQueue<Update> q = s.getValue();
			
			Update messageUpdate = new UserMessageUpdate(new UserMessage(user, message));
			
			q.add(messageUpdate);
		}
	}

	@Override
	public void onlineSubscriber(User user) {
		user.setOnline(true);
		//broadcast to subscribed user's message q
		for(Entry<User, BlockingQueue<Update>> s : _Q.entrySet()){
			BlockingQueue<Update> q = s.getValue();
			
			Update userUpdate = new UserUpdate(user);
			
			q.add(userUpdate);
		}
	}

	@Override
	public void offlineSubscriber(User user) {
		user.setOnline(false);
		
		//update the subscribers repo
		for(User subscriber : subscribers){
			if(subscriber.equals(user)){
				subscriber.setOnline(false);
			}
				
		}
		
		//broadcast to subscribed user's message q
		for(Entry<User, BlockingQueue<Update>> s : _Q.entrySet()){
			BlockingQueue<Update> q = s.getValue();
			
			Update userUpdate = new UserUpdate(user);
			q.add(userUpdate);
		}
	}

	@Override
	public void loadOnlineSubscribersForUserView(User user) {
		for(Entry<User, BlockingQueue<Update>> s : _Q.entrySet()){
			if(s.getKey().equals(user)){
				BlockingQueue<Update> q = s.getValue();
				
				for(User u : subscribers){
					if(u.isOnline() && !u.equals(user)){
						Update userUpdate = new UserUpdate(u);
						
						q.add(userUpdate);
					}
				}
				
				break;
			}
		}
	}

	@Override
	public void loadAllMessagesForUserView(User user) {
		for(Entry<User, BlockingQueue<Update>> s : _Q.entrySet()){
			if(s.getKey().equals(user)){
				BlockingQueue<Update> q = s.getValue();
				
				for(UserMessage um : userMessages){
					Update messageUpdate = new UserMessageUpdate(um);
					q.add(messageUpdate);
				}
				
				break;
			}
		}
	}
}
