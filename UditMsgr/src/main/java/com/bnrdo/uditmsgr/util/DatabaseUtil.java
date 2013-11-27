package com.bnrdo.uditmsgr.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			con = DriverManager.getConnection("jdbc:hsqldb:mem:chatters-jr", "sa", "");
			// con =
			// DriverManager.getConnection("jdbc:hsqldb:file:C:\\Users\\ut1p98\\Desktop\\db\\data-browser",
			// "sa", "");
			// con =
			// DriverManager.getConnection("jdbc:hsqldb:file:C:\\Users\\ut1p98\\Desktop\\db\\data-browser",
			// "sa", "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}
	public static void executeStatement(String statement){
		Connection conn = getConnection();
		Statement stmt = null;
		
		try{
			conn = DatabaseUtil.getConnection();
			stmt = conn.createStatement();
			stmt.execute(statement);
		}catch(SQLException e){
			e.printStackTrace();
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
}
