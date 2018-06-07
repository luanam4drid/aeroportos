package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	private String database;
	private String username;
	private String password;
	public Connection con;
	private long lastInsertId;
	
	public Database(String database, String username, String password){
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public void connect(){
		try {
	        Class.forName("org.h2.Driver");
	        con = DriverManager.getConnection("jdbc:h2:~/" + database, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		try {
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void execute(String sql){
		try {
			
			Statement statement = con.createStatement();
			statement.execute(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				lastInsertId = generatedKeys.getLong(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String sql){
		try {
			return con.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void rollback(){
		try {
			con.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void commit(){
		try {
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public long getLastInsertId(){
		return lastInsertId;
	}
}