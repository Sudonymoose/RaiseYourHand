package com.entities;

public class Roster {
	private int rosterId;
	private String username;
	private int courseNum;
	private String userType;
	
	public Roster(int rosterId, String username, int courseNum, String userType) {
		this.rosterId = rosterId;
		this.username = username;
		this.courseNum = courseNum;
		this.userType = userType;
	}
	public int getRosterId() {
		return rosterId;
	}
	public String getUsername() {
		return username;
	}
	public int getCourseNum() {
		return courseNum;
	}
	public String getUserType() {
		return userType;
	}
}
