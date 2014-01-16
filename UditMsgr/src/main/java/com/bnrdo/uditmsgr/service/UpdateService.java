package com.bnrdo.uditmsgr.service;

import com.bnrdo.uditmsgr.domain.Update;
import com.bnrdo.uditmsgr.domain.User;

public interface UpdateService{
	Update getUpdate(User user) throws InterruptedException;
}
