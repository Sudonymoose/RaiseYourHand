package com.dblayout;

/**
 * Primary class to manage the SQL database. Wrapper for everything else in this package?
 */
public class ManageDatabase {

	DatabaseHelper dbHelper;
	
	UserDatabase userDB;
	RosterDatabase rosterDB;
	CourseDatabase courseDB;
	StudentDatabase studentDB;
	
	public ManageDatabase()
	{
		// Context? Where do we get this?
		// dbHelper = new DatabaseHelper();
		
	}
	
	// Functions for managing UserDatabase
	public void addToUserDB(int userID, String username, String firstName, String lastName,
			String userType, String password, String email, String department, String campus)
	{
		// TODO: Call function in userDB to add to database
	}
	
	public void removeFromUserDB(int userID)
	{
		// TODO: Call function in userDB to remove from database
	}
	
	// Functions for managing RosterDatabase
	public void addToRosterDB(int userID, int courseNum, boolean isInstructor)
	{
		//TODO: Call function in rosterDB to add to database
	}
	
	// Functions for managing CourseDatabase
	// TODO: Figure out type for dates and times
	public void addToCourseDB(int courseNum, int rosterCount, int instructorID, String department,
			String email, String dates, String times, String building, String room)
	{
		//TODO: Call function in courseDB to add to database
	}
	
	public void removeFromCourseDB(int courseNum)
	{
		// TODO: Call function in courseDB to remove from database
	}
	
	// Functions for managing StudentDatabase
	public void addToStudentDB(int userID, String year)
	{
		//TODO: Call function in studentDB to add to database
	}
	
	public void removeFromStudentDB(int userID)
	{
		//TODO: Call function in studentDB to remove from database
	}
	
}
