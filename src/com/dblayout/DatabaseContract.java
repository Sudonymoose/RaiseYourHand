package com.dblayout;

import android.provider.BaseColumns;

/**
 * 
 * Code based off of http://developer.android.com/training/basics/data-storage/databases.html
 */
public final class DatabaseContract {
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	
	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	public DatabaseContract() {}

	/* Inner class that defines the table contents for User Table */
	 public static abstract class UserEntry implements BaseColumns {
	    public static final String TABLE_NAME = "users";
	    public static final String COLUMN_NAME_USER_ID = "user id";
	    public static final String COLUMN_NAME_USERNAME = "username";
	    public static final String COLUMN_NAME_FIRSTNAME = "first name";
	    public static final String COLUMN_NAME_LASTNAME = "last name";
	    public static final String COLUMN_NAME_USERTYPE = "type";
	    public static final String COLUMN_NAME_PASSWORD = "password";
	    public static final String COLUMN_NAME_EMAIL = "e-mail";
	    public static final String COLUMN_NAME_DEPARTMENT = "department";
	    public static final String COLUMN_NAME_CAMPUS = "campus";
	    
	    // TODO: Create command for UserEntry
	    //public static final String SQL_CREATE_ENTRIES;

	    // TODO: Delete command for UserEntry
	    //public static final String SQL_DELETE_ENTRIES;
	}
	 
	 /* Inner class that defines the table contents for Roster Table */
	public static abstract class RosterEntry implements BaseColumns {
		public static final String TABLE_NAME = "rosters";
		public static final String COLUMN_NAME_USER_ID = "user id";
		public static final String COLUMN_NAME_COURSE_NUM = "course number";
		public static final String COLUMN_NAME_INSTRUCTOR = "instructor?";
		
	    // TODO: Create command for RosterEntry
	    //public static final String SQL_CREATE_ENTRIES;

	    // TODO: Delete command for RosterEntry
	    //public static final String SQL_DELETE_ENTRIES;
	}
	
	/* Inner class that defines the table contents for Course Table */
	public static abstract class CourseEntry implements BaseColumns {
		public static final String TABLE_NAME = "courses";
		public static final String COLUMN_NAME_COURSE_NUM = "course number";
		public static final String COLUMN_NAME_ROSTER_COUNT = "roster count";
		public static final String COLUMN_NAME_INSTRUCTOR_ID = "instructor id";
		public static final String COLUMN_NAME_DEPARTMENT = "department";
		public static final String COLUMN_NAME_EMAIL = "e-mail";
		public static final String COLUMN_NAME_DATES = "dates";
		public static final String COLUMN_NAME_TIMES = "times";
		public static final String COLUMN_NAME_BUILDING = "building";
		public static final String COLUMN_NAME_ROOM = "room";
		
	    // TODO: Create command for CourseEntry
	    //public static final String SQL_CREATE_ENTRIES;

	    // TODO: Delete command for CourseEntry
	    //public static final String SQL_DELETE_ENTRIES;
	}
	
	/* Inner class that defines the table contents for Student Table */
	public static abstract class StudentEntry implements BaseColumns {
		public static final String TABLE_NAME = "students";
		public static final String COLUMN_NAME_USER_ID = "user id";
		public static final String COLUMN_NAME_YEAR = "year";
		
	    // TODO: Create command for StudentEntry
	    //public static final String SQL_CREATE_ENTRIES;

	    // TODO: Delete command for StudentEntry
	    //public static final String SQL_DELETE_ENTRIES;
	}
}
