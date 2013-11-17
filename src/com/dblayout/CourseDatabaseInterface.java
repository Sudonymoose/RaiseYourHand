package com.dblayout;

public interface CourseDatabaseInterface {

	void insert(int courseNum, int rosterCount, int instructorID, String department, String email, String dates, String times, String building, String room);
	void remove(int courseNum);
	
}
