package com.dblayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.Exception.RaiseYourHandError;
import com.Exception.RaiseYourHandException;
import com.dblayout.DatabaseContract.CourseEntry;
import com.dblayout.DatabaseContract.RosterEntry;
import com.dblayout.DatabaseContract.UserEntry;


/**
 * DatabaseHelper class. This should be constructed and is used to start up the entire database.
 * 
 * Code based off of http://developer.android.com/training/basics/data-storage/databases.html
 */
public class DatabaseHelper {

	private static final String DATABASE_NAME = "Reader.db";
	private Connection c = null;
	private Statement stmt = null;

	// Constructor
	public DatabaseHelper() throws RaiseYourHandException {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			c.setAutoCommit(false);
			stmt = c.createStatement();
		} catch ( Exception e ) {
			throw new RaiseYourHandException(RaiseYourHandError.SQL_FAILURE, e.getMessage());
		}
		System.out.println("Opened database successfully");
	}

	// Creating the tables in the database
	public void onCreate() {
		try {
			stmt.executeUpdate(UserEntry.SQL_CREATE_ENTRIES);
			stmt.executeUpdate(RosterEntry.SQL_CREATE_ENTRIES);
			stmt.executeUpdate(CourseEntry.SQL_CREATE_ENTRIES);
		} catch (SQLException e) {
			new RaiseYourHandException(RaiseYourHandError.SQL_FAILURE, e.getMessage());
		}
	}

	public void onUpgrade() {
		try {
			stmt.executeUpdate(UserEntry.SQL_DELETE_ENTRIES);
			stmt.executeUpdate(RosterEntry.SQL_DELETE_ENTRIES);
			stmt.executeUpdate(CourseEntry.SQL_DELETE_ENTRIES);
		} catch (SQLException e) {
			new RaiseYourHandException(RaiseYourHandError.SQL_FAILURE, e.getMessage());
		}
		onCreate();
	}

	protected void executeUpdate(String sql) {
		try {
			stmt.executeUpdate(sql);
			c.commit();
		} catch (SQLException e) {
			new RaiseYourHandException(RaiseYourHandError.SQL_FAILURE, e.getMessage());
		}
	}
	
	protected ResultSet executeQuery(String sql) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			new RaiseYourHandException(RaiseYourHandError.SQL_FAILURE, e.getMessage());
		}
		return rs;
	}

	public void onClose() {
		try {
			stmt.close();
			c.close();
			System.out.println("Closed database successfully");
		} catch ( Exception e ) {
			new RaiseYourHandException(RaiseYourHandError.SQL_FAILURE, e.getMessage());
		}
	}
}
