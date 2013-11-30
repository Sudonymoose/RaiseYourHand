package com.dblayout.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Exception.RaiseYourHandError;
import com.Exception.RaiseYourHandException;
import com.dblayout.DatabaseContract.CourseEntry;
import com.dblayout.DatabaseContract.RosterEntry;
import com.dblayout.DatabaseContract.UserEntry;

/**
 * Primary class to manage the SQL database. Wrapper for everything else in this package?
 */
public class ManageDatabase {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	public ManageDatabase(Context c) {
		dbHelper = new DatabaseHelper(c);
		db = dbHelper.getWritableDatabase();
	}

	// Functions for managing UserDatabase
	public void addToUserDB(String username, String firstName, String lastName,
			String userType, String password, String email, String department, String campus) {
		// Check that the user doesn't exist.
		if (exists(queryUserDB(username))) {
			return;
		}
		ContentValues cv = new ContentValues();
		cv.put(UserEntry.COLUMN_NAME_USERNAME, username);
		cv.put(UserEntry.COLUMN_NAME_FIRSTNAME, firstName);
		cv.put(UserEntry.COLUMN_NAME_LASTNAME, lastName);
		cv.put(UserEntry.COLUMN_NAME_USERTYPE, userType);
		cv.put(UserEntry.COLUMN_NAME_PASSWORD, password);
		cv.put(UserEntry.COLUMN_NAME_EMAIL, email);
		cv.put(UserEntry.COLUMN_NAME_DEPARTMENT, department);
		cv.put(UserEntry.COLUMN_NAME_CAMPUS, campus);
		db.insert(UserEntry.TABLE_NAME, null, cv);
	}

	public void removeFromUserDB(String username) {
		// Check that the user exists.
		if (!exists(queryUserDB(username))) {
			return;
		}
		String where = UserEntry.COLUMN_NAME_USERNAME + "=" + username;
		db.delete(UserEntry.TABLE_NAME, where, null);
	}

	public Cursor queryUserDB(String username) {
		String where = UserEntry.COLUMN_NAME_USERNAME + "=" + username;
		return db.query(UserEntry.TABLE_NAME, null, where, null, null,
				null, null);
	}

	public Cursor getAllUserDB(String username) {
		return db.query(UserEntry.TABLE_NAME, null, null, null, null,
				null, UserEntry.COLUMN_NAME_USERNAME);
	}

	// Functions for managing RosterDatabase
	public void addToRosterDB(String username, int courseNum, String userType)	{
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
		ContentValues cv = new ContentValues();
		cv.put(RosterEntry.COLUMN_NAME_USERNAME, username);
		cv.put(RosterEntry.COLUMN_NAME_COURSE_NUM, courseNum);
		cv.put(RosterEntry.COLUMN_NAME_USERTYPE, userType);
		db.insert(RosterEntry.TABLE_NAME, null, cv);
	}

	public void removeFromRosterDB(int courseNum) {
		// Check that the class roster exists.
		if (!exists(queryRosterDB(courseNum))) {
			return;
		}
		String where = RosterEntry.COLUMN_NAME_COURSE_NUM + "=" + courseNum;
		db.delete(RosterEntry.TABLE_NAME, where, null);
	}

	public void removeFromRosterDB(String username) {
		// Check that the user is in the roster.
		if (!exists(queryRosterDB(username))) {
			return;
		}
		String where = RosterEntry.COLUMN_NAME_USERNAME + "=" + username;
		db.delete(UserEntry.TABLE_NAME, where, null);
	}

	public Cursor queryRosterDB(String username) {
		String where = RosterEntry.COLUMN_NAME_USERNAME + "=" + username;
		return db.query(RosterEntry.TABLE_NAME, null, where, null, null,
				null, null);
	}

	public Cursor queryRosterDB(int courseNum) {
		String where = RosterEntry.COLUMN_NAME_COURSE_NUM + "=" + courseNum + " AND " +
				RosterEntry.COLUMN_NAME_USERTYPE + "=student;";
		return db.query(RosterEntry.TABLE_NAME, null, where, null, null,
				null, null);
	}

	public Cursor queryRosterDB(String username, int courseNum) {
		String where = RosterEntry.COLUMN_NAME_USERNAME + "=" + username + " AND " +
				RosterEntry.COLUMN_NAME_COURSE_NUM + "=" + courseNum;
		return db.query(RosterEntry.TABLE_NAME, null, where, null, null,
				null, null);
	}

	// Functions for managing CourseDatabase
	public void addToCourseDB(int courseNum, int rosterCount, int instructorID, String department,
			String email, String dates, String times, String building, String room) {
		// Check that the class doesn't exists.
		if (exists(queryCourseDB(courseNum))) {
			return;
		}
		ContentValues cv = new ContentValues();
		cv.put(CourseEntry.COLUMN_NAME_COURSE_NUM, courseNum);
		cv.put(CourseEntry.COLUMN_NAME_ROSTER_COUNT, rosterCount);
		cv.put(CourseEntry.COLUMN_NAME_INSTRUCTOR, instructorID);
		cv.put(CourseEntry.COLUMN_NAME_DEPARTMENT, department);
		cv.put(CourseEntry.COLUMN_NAME_EMAIL, email);
		cv.put(CourseEntry.COLUMN_NAME_DATES, dates);
		cv.put(CourseEntry.COLUMN_NAME_TIMES, times);
		cv.put(CourseEntry.COLUMN_NAME_BUILDING, building);
		cv.put(CourseEntry.COLUMN_NAME_ROOM, room);
		db.insert(RosterEntry.TABLE_NAME, null, cv);
	}

	public void removeFromCourseDB(int courseNum) {
		// Check that the class exists.
		if (!exists(queryCourseDB(courseNum))) {
			return;
		}
		String where = CourseEntry.COLUMN_NAME_COURSE_NUM + "=" + courseNum;
		db.delete(UserEntry.TABLE_NAME, where, null);
	}

	public Cursor queryCourseDB(int courseNum) {
		String where = CourseEntry.COLUMN_NAME_COURSE_NUM + "=" + courseNum;
		return db.query(CourseEntry.TABLE_NAME, null, where, null, null,
				null, null);
	}

	private boolean exists(Cursor c) {
		try {
			return (c != null && c.moveToFirst());
		} catch (Exception e) {
			new RaiseYourHandException(RaiseYourHandError.SQL_FAILURE, e.getMessage());
		}
		return false;
	}

}
