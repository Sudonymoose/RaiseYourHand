package com.raiseyourhand;

import android.app.Application;

import com.entities.Lecture;

public class RaiseYourHandApp extends Application {
	private Lecture lecture;
	private String username;
	private int courseNum;
	
	public RaiseYourHandApp() {
		lecture = null;
		username = null;
		courseNum = -1;
	}
	
	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}
	
	public Lecture getLecture() {
		return lecture;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getCourseNum() {
		return courseNum;
	}

}
