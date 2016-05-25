package kr.ac.snu.ads.facetracking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FaceTrackingDB {
		private Connection connection = null;
		private String currentTable = null;
		
		public FaceTrackingDB(String dbName) throws Exception {
				Class.forName("org.sqlite.JDBC");
				String dbUrl = "jdbc:sqlite:" + dbName + ".db";
				connection = DriverManager.getConnection(dbUrl);
		}
		
		public boolean createTable(String tableName) {
			try {
				currentTable = tableName;
				Statement statement = connection.createStatement();
			    statement.setQueryTimeout(30);  // set timeout to 30 sec.
			    String query = "CREATE TABLE IF NOT EXISTS " + 
			    		tableName +
			    		" (id INT, " +
			    		" snapDate DATETIME, " +
			    		" x INT, " +
			    		" y INT)";	
			    int nRet = statement.executeUpdate(query);	  
			    return true;
			    
			} catch (SQLException ex) {
				ex.printStackTrace();
				return false;
			}
		}
		
		public void setTable(String tableName) {
			currentTable = tableName;
		}
		
		public boolean insert(UserTrace trace) {
			try {
				int id = trace.getUserId();
				long snapped = trace.getSnapDate().getTime();
				int[] axis = trace.getLocation();
				int x = axis[0];
				int y = axis[1];
				Statement statement = connection.createStatement();
			    statement.setQueryTimeout(30);  // set timeout to 30 sec.
			    String query = "INSERT INTO " + 
			    		currentTable + "(id, snapDate, x, y)" +
			    		" VALUES(" + id + ", " +
			    		snapped + ", " + 
			    		x + ", " +
			    		y + ")";
			    int nRet = statement.executeUpdate(query);
			    if (nRet > 0) {
			    	return true;
			    } else {
			    	System.out.println("Insert failed");
			    	return false;
			    }
			    
			} catch (SQLException ex) {
				ex.printStackTrace();
				return false;
			}
		}
		
		public void close() {
			try {
				connection.close();
			} catch (SQLException e) {			
				e.printStackTrace();
			}
		}
}
