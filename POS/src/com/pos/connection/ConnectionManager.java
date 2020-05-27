package com.pos.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class ConnectionManager {
	public static Connection conn;
	public static String JDBC_URL = "jdbc:sqlserver://127.0.0.1\\sqlexpress;databaseName=PHARMACY;user=sa;password=123";
	
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
}
