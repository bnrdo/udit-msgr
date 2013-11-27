package com.bnrdo.uditmsgr.listener;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bnrdo.uditmsgr.util.DatabaseUtil;

public class CJRListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Connection conn = null;
		Statement stmt = null;
		
    	try {
			conn = DatabaseUtil.getConnection();
			stmt = conn.createStatement();
			stmt.execute("DROP TABLE IF EXISTS CHATTERS_JR;");
			stmt.execute("SET IGNORECASE TRUE;");
			stmt.execute("CREATE MEMORY TABLE User (USERNAME VARCHAR(100) not null, IP_ADDRESS VARCHAR(100) not null)");
			stmt.execute("CREATE MEMORY TABLE UserMessage (USERNAME VARCHAR(100) not null, MESSAGE VARCHAR(10000) not null, TIMESENT BIGINT)");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}

}
