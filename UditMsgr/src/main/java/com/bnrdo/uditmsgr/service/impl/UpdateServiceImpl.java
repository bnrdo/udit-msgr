package com.bnrdo.uditmsgr.service.impl;

import java.util.concurrent.BlockingQueue;

import org.springframework.stereotype.Service;

import com.bnrdo.uditmsgr.domain.Update;
import com.bnrdo.uditmsgr.domain.User;
import com.bnrdo.uditmsgr.repo.DataStore;
import com.bnrdo.uditmsgr.service.UpdateService;

@Service
public class UpdateServiceImpl implements UpdateService{
	@Override
	public Update getUpdate(User user) throws InterruptedException {
		BlockingQueue<Update> q = DataStore._Q.get(user.getUserName());
		Update update = q.take();
		
		return update;
	}
}
