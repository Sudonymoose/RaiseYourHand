package com.entities;


public class Course {
	private int courseNum;
	private int rosterCount;
	private String instructor;
	private String department;
	private String email;
	private String dates;
	private String times;
	private String building;
	private String room;

	public Course(int courseNum, int rosterCount, String instructor,
			String department, String email, String dates, String times,
			String building, String room) {
		this.courseNum = courseNum;
		this.rosterCount = rosterCount;
		this.instructor = instructor;
		this.department = department;
		this.email = email;
		this.dates = dates;
		this.times = times;
		this.building = building;
		this.room = room;
	}
	public int getCourseNum() {
		return courseNum;
	}
	public int getRosterCount() {
		return rosterCount;
	}
	public String getInstructor() {
		return instructor;
	}
	public String getDepartment() {
		return department;
	}
	public String getEmail() {
		return email;
	}
	public String getDates() {
		return dates;
	}
	public String getTimes() {
		return times;
	}
	public String getBuilding() {
		return building;
	}
	public String getRoom() {
		return room;
	}
}
