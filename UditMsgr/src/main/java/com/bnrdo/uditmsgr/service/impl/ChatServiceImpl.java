package com.bnrdo.uditmsgr.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import com.bnrdo.uditmsgr.domain.Message;
import com.bnrdo.uditmsgr.domain.TerminateUpdate;
import com.bnrdo.uditmsgr.domain.Update;
import com.bnrdo.uditmsgr.domain.User;
import com.bnrdo.uditmsgr.domain.UserMessage;
import com.bnrdo.uditmsgr.domain.UserMessageUpdate;
import com.bnrdo.uditmsgr.domain.UserUpdate;
import com.bnrdo.uditmsgr.repo.DataStore;
import com.bnrdo.uditmsgr.service.ChatService;
import com.bnrdo.uditmsgr.util.Constants.Status;

@Service
public class ChatServiceImpl implements ChatService{

	@Override
	public void onlineUser(String userName) {
		User user = findUser(userName);
		user.setStatus(Status.ONLINE);
		
		//update the subscribers repo
		for(User subscriber : DataStore.subscribers){
			if(subscriber.getUserName().equals(userName)){
				subscriber.setStatus(Status.ONLINE);
			}
		}
		
		//broadcast to subscribed user's message q. 
		//Should not be published to offline subscriber's q.
		for(Entry<String, BlockingQueue<Update>> s : DataStore._Q.entrySet()){
			if(!getOfflineSubcribers().contains(s.getKey())){
				BlockingQueue<Update> q = s.getValue();
				
				Update userUpdate = new UserUpdate(user);
				
				q.add(userUpdate);
			}
		}
	}

	@Override
	public void offlineUser(String userName) {
		User user = findUser(userName);
		user.setStatus(Status.OFFLINE);
		
		//update the subscribers repo
		for(User subscriber : DataStore.subscribers){
			if(subscriber.getUserName().equals(userName)){
				subscriber.setStatus(Status.OFFLINE);
			}
		}
		
		//broadcast to other subscribed user's message q
		for(Entry<String, BlockingQueue<Update>> s : DataStore._Q.entrySet()){
			BlockingQueue<Update> q = s.getValue();
			String qOwner = s.getKey();
			
			//if this current q owner is the user, that should add the poison pill to 
			//his q, so that fetching of updates will stop.
			if(qOwner.equals(userName)){
				q.add(new TerminateUpdate());
				System.out.println("******************Added a Terminate Update for " + userName);
			}else{
				if(!getOfflineSubcribers().contains(qOwner)){
					Update userUpdate = new UserUpdate(user);
					q.add(userUpdate);
				}
			}
			//if(!s.getKey().equals(user)){
				
			//}
		}
	}

	@Override
	public void registerUser(User user) {
		DataStore.subscribers.add(user);
		
		//make sure to defensive copy the user to be used as the key of the user's q to so that when user in subscribers list is modified, the key in _Q is not modified
		DataStore._Q.put(user.getUserName(), new LinkedBlockingQueue<Update>()); 
		
		//DatabaseUtil.executeStatement("INSERT INTO USER(USERNAME, IP_ADDRESS) VALUES('" + user.getUserName() + "','" + user.getIpAddress() + "')");
	}

	@Override
	public void changeUserName(String currentUserName, String newUserName) {
		
		for(User user : DataStore.subscribers){
			if(user.getUserName().equals(currentUserName)){
				user.setUserName(newUserName);
				break;
			}
		}
		
		//transfer the user's current queue with its new key
		BlockingQueue<Update> userQ = null;
		
		for(Entry<String, BlockingQueue<Update>> s : DataStore._Q.entrySet()){
			if(s.getKey().equals(currentUserName)){
				userQ = s.getValue();
				break;
			}
		}
		
		if(userQ != null){
			DataStore._Q.remove(currentUserName);
			DataStore._Q.put(newUserName, userQ);
		}
		
	}

	@Override
	public User findUserByIp(String userIp) {
		for(User subscriber : DataStore.subscribers){
			if(subscriber.getIpAddress().equals(userIp))
				return subscriber;
		}
		
		return null;
	}
	
	@Override
	public User findUser(String userName) {
		for(User subscriber : DataStore.subscribers){
			if(subscriber.getUserName().equals(userName))
				return subscriber;
		}
		
		return null;
	}

	@Override
	public List<String> getOnlineSubcribers() {
		List<String> retVal = new ArrayList<String>();
		
		for(User user : DataStore.subscribers){
			if(user.getStatus().equals(Status.ONLINE)){
				retVal.add(user.getUserName());
			}
		}
		
		return retVal;
	}

	@Override
	public List<String> getOfflineSubcribers() {
		List<String> retVal = new ArrayList<String>();
		
		for(User user : DataStore.subscribers){
			if(user.getStatus().equals(Status.OFFLINE)){
				retVal.add(user.getUserName());
			}
		}
		
		return retVal;
	}

	@Override
	public void saveChatMessage(String userName, Message message) {
		User user = findUser(userName);
		DataStore.userMessages.add(new UserMessage(user, message));
		
		//broadcast to subscribed user's message q
		for(Entry<String, BlockingQueue<Update>> s : DataStore._Q.entrySet()){
			BlockingQueue<Update> q = s.getValue();
			
			Update messageUpdate = new UserMessageUpdate(new UserMessage(user, message));
			
			q.add(messageUpdate);
		}
	}

	@Override
	public void loadOnlineSubscribersForUserView(String userName) {
		for(Entry<String, BlockingQueue<Update>> s : DataStore._Q.entrySet()){
			if(s.getKey().equals(userName)){
				BlockingQueue<Update> q = s.getValue();
				
				for(User u : DataStore.subscribers){
					if(u.getStatus().equals(Status.ONLINE) && !u.getUserName().equals(userName)){
						Update userUpdate = new UserUpdate(u);
						
						q.add(userUpdate);
					}
				}
				
				break;
			}
		}
	}

	@Override
	public boolean isUsernameTaken(String userName) {
		for(User subscriber : DataStore.subscribers){
			if(subscriber.getUserName().trim().equals(userName.trim())){
				return true;
			}
				
		}
		
		return false;
	}
}
