package com.raiseyourhand;

import android.app.Application;

import com.entities.Lecture;

public class RaiseYourHandApp extends Application {
	private static Lecture lecture;
	private static String username;
	private static boolean isStudent;
	private static int courseNum;
	
	public RaiseYourHandApp() {
		logout();
	}
	
	public static void setLecture(Lecture l) {
		lecture = l;
	}
	
	public static void setUsername(String u) {
		username = u;
	}
	
	public static void setIsStudent(boolean i) {
		isStudent = i;
	}
	
	public static void setCourseNum(int c) {
		courseNum = c;
	}
	
	public static Lecture getLecture() {
		return lecture;
	}
	
	public static String getUsername() {
		return username;
	}
	
	public static boolean getIsStudent() {
		return isStudent;
	}
	
	public static int getCourseNum() {
		return courseNum;
	}
	
	public static void logout() {
		isStudent = true;
		username = null;
		lecture = null;
		courseNum = -1;
	}

}
