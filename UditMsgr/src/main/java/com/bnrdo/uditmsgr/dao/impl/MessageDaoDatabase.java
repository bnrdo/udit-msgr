package com.bnrdo.uditmsgr.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bnrdo.uditmsgr.dao.MessageDao;
import com.bnrdo.uditmsgr.domain.Message;
import com.bnrdo.uditmsgr.domain.User;
import com.bnrdo.uditmsgr.util.DatabaseUtil;

@Repository("DatabaseImplementation")
public class MessageDaoDatabase implements MessageDao{

	@Override
	public List<String> getMessages() {

		return null;
	}

	@Override
	public void addMessage(String message) {
		
	}

	@Override
	public void saveUser(User user) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		
		try{
			conn = DatabaseUtil.getConnection();
			stmt = conn.createStatement();
			stmt.execute("INSERT INTO User (USERNAME, IP_ADDRESS) VALUES ('" + user.getUserName() + "','" + user.getIpAddress() + "')");
		}catch(SQLException e){
			throw e;
		}finally{
			try {
				stmt.close();
				conn.close();
				
				stmt = null;
				conn = null;
			} catch (SQLException e) {
				//shh
			}
		}
		
	}

	@Override
	public String findUserByIp(String ipAddress) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = DatabaseUtil.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT USERNAME FROM User WHERE IP_ADDRESS = '" + ipAddress + "'");
			
			while(rs.next()){
				return rs.getString("USERNAME");
			}
		}catch(SQLException e){
			throw e;
		}finally{
			try {
				rs.close();
				stmt.close();
				conn.close();
				
				rs.close();
				stmt = null;
				conn = null;
			} catch (SQLException e) {
				//ignore;
			}
		}
		
		return "";
	}

	@Override
	public void saveUserMessage(User user, Message message) throws SQLException{
		
		Connection conn = null;
		Statement stmt = null;
		
		try{
			conn = DatabaseUtil.getConnection();
			stmt = conn.createStatement();
			stmt.execute("INSERT INTO Message " +
						"(USERNAME, MESSAGE, TIMESENT) " +
						"VALUES (" +
							"'" + user.getUserName() + "'," +
							"'" + message.getContent() + "'," +
							"" + message.getTimeSent() + ")");
		}catch(SQLException e){
			throw e;
		}finally{
			try {
				conn.close();
				stmt.close();
				conn = null;
				stmt = null;
			} catch (SQLException e) {
				//shh
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
	}
}
