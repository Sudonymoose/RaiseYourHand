package com.asdf;

public interface RosterDatabaseInterface {

	void insert(int userID, int courseNum, boolean isInstructor);
	void remove(int userID);
	
}
