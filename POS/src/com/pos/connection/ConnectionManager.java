package com.pos.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
	public static Connection conn;
	public static String JDBC_URL = "jdbc:sqlserver://127.0.0.1\\sqlexpress;databaseName=POS;user=sa;password=123";
	
	private ConnectionManager() {
		
	}
	
	public static Connection getConnection() {
		try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            if(conn == null) {
            	conn = DriverManager.getConnection(JDBC_URL);
                DatabaseMetaData metaObj = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver Name?= " + metaObj.getDriverName() + ", Driver Version?= " + metaObj.getDriverVersion() + ", Product Name?= " + metaObj.getDatabaseProductName() + ", Product Version?= " + metaObj.getDatabaseProductVersion());
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
		
		return conn;
	}
	
	public static ResultSet queryExecutor(Connection conn,String sql) {
		ResultSet rs = null;
		try {
			Statement stmt=conn.createStatement();
			rs=stmt.executeQuery(sql);
			System.out.println("Sql-----> "+sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			return rs;
		}
			
	}
	
	public static int queryInsert(Connection conn,String sql) {
		int count=0;
		try {
			Statement stmt=conn.createStatement();
			count=stmt.executeUpdate(sql);
			
			System.out.println("Sql-----> "+sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			return count;
		}
			
	}
}
