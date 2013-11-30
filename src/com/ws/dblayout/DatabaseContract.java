package com.ws.dblayout;


/**
 * 
 * Code based off of http://developer.android.com/training/basics/data-storage/databases.html
 */
public final class DatabaseContract {

	
	public static final String CREATE_TABLE = "CREATE TABLE ";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
	public static final String INSERT_RECORD = "INSERT INTO ";
	public static final String DELETE_RECORD = "DELETE FROM ";
	public static final String SELECT_RECORD = "SELECT * FROM ";
	public static final String INTEGER_TYPE = " INTEGER";
	public static final String TEXT_TYPE = " TEXT";
	public static final String BIT_TYPE = " BIT";
	public static final String COMMA_SEP = ",";

	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	public DatabaseContract() {}

	/* Inner class that defines the table contents for User Table */
	public static abstract class UserEntry {
		public static final String TABLE_NAME = "users";
		public static final String COLUMN_NAME_USERNAME = "username";
		public static final String COLUMN_NAME_FIRSTNAME = "first_name";
		public static final String COLUMN_NAME_LASTNAME = "last_name";
		public static final String COLUMN_NAME_USERTYPE = "type";
		public static final String COLUMN_NAME_PASSWORD = "password";
		public static final String COLUMN_NAME_EMAIL = "e-mail";
		public static final String COLUMN_NAME_DEPARTMENT = "department";
		public static final String COLUMN_NAME_CAMPUS = "campus";

		// Create command for UserEntry
		public static final String SQL_CREATE_ENTRIES = CREATE_TABLE + UserEntry.TABLE_NAME + " ( " +
				UserEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + " PRIMARY KEY," +
				UserEntry.COLUMN_NAME_FIRSTNAME + TEXT_TYPE + COMMA_SEP +
				UserEntry.COLUMN_NAME_LASTNAME + TEXT_TYPE + COMMA_SEP +
				UserEntry.COLUMN_NAME_USERTYPE + TEXT_TYPE + COMMA_SEP +
				UserEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
				UserEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
				UserEntry.COLUMN_NAME_DEPARTMENT + TEXT_TYPE + COMMA_SEP +
				UserEntry.COLUMN_NAME_CAMPUS + TEXT_TYPE + " )";

		// Delete command for UserEntry
		public static final String SQL_DELETE_ENTRIES = DROP_TABLE + UserEntry.TABLE_NAME;
		
		// Insert command for UserEntry
		public static final String SQL_INSERT_RECORD = INSERT_RECORD + UserEntry.TABLE_NAME + " ( " +
				UserEntry.COLUMN_NAME_USERNAME + COMMA_SEP +
				UserEntry.COLUMN_NAME_FIRSTNAME + COMMA_SEP +
				UserEntry.COLUMN_NAME_LASTNAME + COMMA_SEP +
				UserEntry.COLUMN_NAME_USERTYPE + COMMA_SEP +
				UserEntry.COLUMN_NAME_PASSWORD + COMMA_SEP +
				UserEntry.COLUMN_NAME_EMAIL + COMMA_SEP +
				UserEntry.COLUMN_NAME_DEPARTMENT + COMMA_SEP +
				UserEntry.COLUMN_NAME_CAMPUS + " ) VALUES ( ";
		
		// Remove command for UserEntry
		public static final String SQL_DELETE_RECORD = DELETE_RECORD + UserEntry.TABLE_NAME;
		
		// Select command for UserEntry
		public static final String SQL_SELECT_RECORD = SELECT_RECORD + UserEntry.TABLE_NAME;
	}

	/* Inner class that defines the table contents for Roster Table */
	public static abstract class RosterEntry {
		public static final String TABLE_NAME = "rosters";
		public static final String COLUMN_NAME_ROSTER_ID = "roster_id";
		public static final String COLUMN_NAME_USERNAME = "username";
		public static final String COLUMN_NAME_COURSE_NUM = "course_number";
		public static final String COLUMN_NAME_INSTRUCTOR = "instructor?";

		// Create command for RosterEntry
		public static final String SQL_CREATE_ENTRIES = CREATE_TABLE + RosterEntry.TABLE_NAME + " (" +
				RosterEntry.COLUMN_NAME_ROSTER_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
				RosterEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
				RosterEntry.COLUMN_NAME_COURSE_NUM + INTEGER_TYPE + COMMA_SEP +
				RosterEntry.COLUMN_NAME_INSTRUCTOR + BIT_TYPE +	" )";

		// Delete command for RosterEntry
		public static final String SQL_DELETE_ENTRIES = DROP_TABLE + RosterEntry.TABLE_NAME;

		// Insert command for RosterEntry
		public static final String SQL_INSERT_RECORD = INSERT_RECORD + RosterEntry.TABLE_NAME + " ( " + 
				RosterEntry.COLUMN_NAME_ROSTER_ID + COMMA_SEP +
				RosterEntry.COLUMN_NAME_USERNAME + COMMA_SEP + 
				RosterEntry.COLUMN_NAME_COURSE_NUM + COMMA_SEP + 
				RosterEntry.COLUMN_NAME_INSTRUCTOR + " ) VALUES ( ";
		
		// Remove command for RosterEntry
		public static final String SQL_DELETE_RECORD = DELETE_RECORD + RosterEntry.TABLE_NAME;
		
		// Select command for RosterEntry
		public static final String SQL_SELECT_RECORD = SELECT_RECORD + RosterEntry.TABLE_NAME;
	}

	/* Inner class that defines the table contents for Course Table */
	public static abstract class CourseEntry {
		public static final String TABLE_NAME = "courses";
		public static final String COLUMN_NAME_COURSE_NUM = "course_number";
		public static final String COLUMN_NAME_ROSTER_COUNT = "roster_count";
		public static final String COLUMN_NAME_INSTRUCTOR = "instructor";
		public static final String COLUMN_NAME_DEPARTMENT = "department";
		public static final String COLUMN_NAME_EMAIL = "e-mail";
		public static final String COLUMN_NAME_DATES = "dates";
		public static final String COLUMN_NAME_TIMES = "times";
		public static final String COLUMN_NAME_BUILDING = "building";
		public static final String COLUMN_NAME_ROOM = "room";

		// Create command for CourseEntry
		public static final String SQL_CREATE_ENTRIES = CREATE_TABLE + CourseEntry.TABLE_NAME + " ( " +
				CourseEntry.COLUMN_NAME_COURSE_NUM + INTEGER_TYPE + " PRIMARY KEY,"+
				CourseEntry.COLUMN_NAME_ROSTER_COUNT + INTEGER_TYPE + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_INSTRUCTOR + TEXT_TYPE + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_DEPARTMENT + TEXT_TYPE + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_DATES + TEXT_TYPE + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_TIMES + TEXT_TYPE + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_BUILDING + TEXT_TYPE + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_ROOM + TEXT_TYPE + " )";

		// Delete command for CourseEntry
		public static final String SQL_DELETE_ENTRIES = DROP_TABLE + CourseEntry.TABLE_NAME;
		
		// Insert command for CourseEntry
		public static final String SQL_INSERT_RECORD = INSERT_RECORD + CourseEntry.TABLE_NAME + " ( " + 
				CourseEntry.COLUMN_NAME_COURSE_NUM + COMMA_SEP +
				CourseEntry.COLUMN_NAME_ROSTER_COUNT + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_INSTRUCTOR + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_DEPARTMENT + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_EMAIL + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_DATES + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_TIMES + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_BUILDING + COMMA_SEP + 
				CourseEntry.COLUMN_NAME_ROOM + " ) VALUES ( ";
		
		// Remove command for CourseEntry
		public static final String SQL_DELETE_RECORD = DELETE_RECORD + CourseEntry.TABLE_NAME;
		
		// Select command for CourseEntry
		public static final String SQL_SELECT_RECORD = SELECT_RECORD + CourseEntry.TABLE_NAME;
	}
}
