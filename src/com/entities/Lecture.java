package com.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Lecture implements Serializable{
	private static final long serialVersionUID = 2110167116581440442L;
	private int courseNum;
	private ArrayList<String> presentStudents;
	private String activeQuestion;
	private AttendanceState attendance_state;
	private QuizState quiz_state;
	private ArrayList<String> attendance;
	private ArrayList<String> questions;
	private ArrayList<String> instructorShared;
	private ArrayList<String> studentShared;
	private ArrayList<String> pendingStudentShared;
	
	public Lecture() {
		// TODO Auto-generated constructor stub
	}

}
