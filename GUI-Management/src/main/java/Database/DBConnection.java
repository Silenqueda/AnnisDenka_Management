package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	
	private Connection connection;

	public DBConnection() {

	}

	public void connectToDB() {
		try {
			this.connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/annisdenka_management_db", "dbr", "everythingforyou!");
			System.out.println("Connected to PostgreSQL...");			
			
		} catch (SQLException e) {
			System.out.println("Failed to connect to PostgreSQL");
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return connection;
	}
	
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Failed to close connection");
			e.printStackTrace();
		}
	}
	
	

}
