package com.dblayout;

public interface UserDatabaseInterface {

	void insert(int userID, String username, String firstName, String lastName, String userType, String password, String email, String department, String campus);
	void remove(int userID);
	
}
