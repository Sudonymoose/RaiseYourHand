package com.ws.dblayout;

import java.sql.ResultSet;

import com.Exception.RaiseYourHandError;
import com.Exception.RaiseYourHandException;
import com.ws.dblayout.DatabaseContract.CourseEntry;
import com.ws.dblayout.DatabaseContract.RosterEntry;
import com.ws.dblayout.DatabaseContract.UserEntry;

/**
 * Primary class to manage the SQL database. Wrapper for everything else in this package?
 */
public class ManageDatabase {

	private DatabaseHelper db;

	public ManageDatabase() throws RaiseYourHandException	{
		db = new DatabaseHelper();
	}

	// Functions for managing UserDatabase
	public void addToUserDB(String username, String firstName, String lastName,
			String userType, String password, String email, String department, String campus) {
		// Check that the user doesn't exist.
		if (exists(queryUserDB(username))) {
			return;
		}
		String sql = UserEntry.SQL_INSERT_RECORD + 
				username + DatabaseContract.COMMA_SEP +
				firstName + DatabaseContract.COMMA_SEP +
				lastName + DatabaseContract.COMMA_SEP +
				userType + DatabaseContract.COMMA_SEP +
				password + DatabaseContract.COMMA_SEP +
				email + DatabaseContract.COMMA_SEP +
				department + DatabaseContract.COMMA_SEP +
				campus + " ); ";
		db.executeUpdate(sql);
	}

	public void removeFromUserDB(String username) {
		// Check that the user exists.
		if (!exists(queryUserDB(username))) {
			return;
		}
		String sql = UserEntry.SQL_DELETE_RECORD + " WHERE " + 
				UserEntry.COLUMN_NAME_USERNAME + "=" + username + ";";
		db.executeUpdate(sql);
	}

	public ResultSet queryUserDB(String username) {
		String sql = UserEntry.SQL_SELECT_RECORD + " WHERE " + 
				UserEntry.COLUMN_NAME_USERNAME + "=" + username + ";";
		return db.executeQuery(sql);
	}

	// Functions for managing RosterDatabase
	public void addToRosterDB(String username, int courseNum, boolean isInstructor)	{
		// Check that the student isn't already added to the roster.
		if (exists(queryRosterDB(username,courseNum))) {
			return;
		}
		// Check that the user exists.
		if (!exists(queryUserDB(username))) {
			return;
		}
		// Check that the class exists.
		if (!exists(queryCourseDB(courseNum))) {
			return;
		}

		// Insert into the roster.
		String sql = RosterEntry.SQL_INSERT_RECORD +
				username + DatabaseContract.COMMA_SEP +
				courseNum + DatabaseContract.COMMA_SEP +
				isInstructor + " );";
		db.executeUpdate(sql);
	}

	public void removeFromRosterDB(int courseNum) {
		// Check that the class roster exists.
		if (!exists(queryRosterDB(courseNum))) {
			return;
		}
		String sql = RosterEntry.SQL_DELETE_RECORD + " WHERE " + 
				RosterEntry.COLUMN_NAME_COURSE_NUM + "=" + courseNum + ";";
		db.executeUpdate(sql);
	}

	public void removeFromRosterDB(String username) {
		// Check that the user is in the roster.
		if (!exists(queryRosterDB(username))) {
			return;
		}
		String sql = RosterEntry.SQL_DELETE_RECORD + " WHERE " + 
				RosterEntry.COLUMN_NAME_USERNAME + "=" + username + ";";
		db.executeUpdate(sql);
	}

	public ResultSet queryRosterDB(String username) {
		String sql = RosterEntry.SQL_SELECT_RECORD + " WHERE " + 
				RosterEntry.COLUMN_NAME_USERNAME + "=" + username + ";";
		return db.executeQuery(sql);
	}

	public ResultSet queryRosterDB(int courseNum) {
		String sql = RosterEntry.SQL_SELECT_RECORD + " WHERE " + 
				RosterEntry.COLUMN_NAME_COURSE_NUM + "=" + courseNum + ";";
		return db.executeQuery(sql);
	}

	public ResultSet queryRosterDB(String username, int courseNum) {
		String sql = RosterEntry.SQL_SELECT_RECORD + " WHERE " + 
				RosterEntry.COLUMN_NAME_USERNAME + "=" + username + " AND " +
				RosterEntry.COLUMN_NAME_COURSE_NUM + "=" + courseNum + ";";
		return db.executeQuery(sql);
	}

	// Functions for managing CourseDatabase
	public void addToCourseDB(int courseNum, int rosterCount, int instructorID, String department,
			String email, String dates, String times, String building, String room) {
		// Check that the class doesn't exists.
		if (exists(queryCourseDB(courseNum))) {
			return;
		}
		String sql = CourseEntry.SQL_INSERT_RECORD +
				courseNum + DatabaseContract.COMMA_SEP +
				rosterCount + DatabaseContract.COMMA_SEP +
				instructorID + DatabaseContract.COMMA_SEP +
				department + DatabaseContract.COMMA_SEP +
				email + DatabaseContract.COMMA_SEP +
				dates + DatabaseContract.COMMA_SEP +
				times + DatabaseContract.COMMA_SEP +
				building + DatabaseContract.COMMA_SEP +
				room + " );";
		db.executeUpdate(sql);
	}

	public void removeFromCourseDB(int courseNum) {
		// Check that the class exists.
		if (!exists(queryCourseDB(courseNum))) {
			return;
		}
		String sql = CourseEntry.SQL_DELETE_RECORD + " WHERE " + 
				CourseEntry.COLUMN_NAME_COURSE_NUM + "=" + courseNum + ";";
		db.executeUpdate(sql);
	}

	public ResultSet queryCourseDB(int courseNum) {
		String sql = CourseEntry.SQL_SELECT_RECORD + " WHERE " + 
				CourseEntry.COLUMN_NAME_COURSE_NUM + "=" + courseNum + ";";
		return db.executeQuery(sql);
	}

	private boolean exists(ResultSet rs) {
		try {
			return (rs != null && rs.next());
		} catch (Exception e) {
			new RaiseYourHandException(RaiseYourHandError.SQL_FAILURE, e.getMessage());
		}
		return false;
	}

}
