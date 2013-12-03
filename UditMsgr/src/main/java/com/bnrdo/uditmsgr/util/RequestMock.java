package com.bnrdo.uditmsgr.util;

import javax.servlet.http.HttpServletRequest;

public class RequestMock {
	
	public String getRemoteAddr(HttpServletRequest req){
		return req.getParameter("ip");
	}
}
