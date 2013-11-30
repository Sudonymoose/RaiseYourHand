package com.entities;

import java.io.Serializable;

public class Roster implements Serializable {
	private static final long serialVersionUID = -2893127945420023717L;
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
