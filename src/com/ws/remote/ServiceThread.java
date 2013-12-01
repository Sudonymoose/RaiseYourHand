/**
 * 
 */
package com.ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.jasypt.util.password.StrongPasswordEncryptor;

import com.Exception.RaiseYourHandError;
import com.Exception.RaiseYourHandException;
import com.dblayout.DatabaseContract.CourseEntry;
import com.dblayout.DatabaseContract.RosterEntry;
import com.dblayout.DatabaseContract.UserEntry;
import com.dblayout.backend.ManageDatabase;
import com.entities.Course;
import com.entities.Lecture;
import com.entities.Roster;
import com.entities.User;
import com.ws.Request;
import com.ws.RequestType;

/**
 * @author arthurc
 *
 */
public class ServiceThread extends Thread {
	private Socket conn;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private ManageDatabase db;
	private LinkedHashMap<Integer, Lecture> lectures;
	private Request failure;
	private StrongPasswordEncryptor passwordEncryptor;

	public ServiceThread(Socket conn, ObjectInputStream in, ObjectOutputStream out,
			ManageDatabase db, LinkedHashMap<Integer, Lecture> lectures) {
		this.conn = conn;
		this.in = in;
		this.out = out;
		this.db = db;
		this.lectures = lectures;
		this.failure = new Request(RequestType.SEND_RESPONSE,new Object[] {Request.FAILURE});
		this.passwordEncryptor = new StrongPasswordEncryptor();
	}

	public void run() {
		handleSession();
		closeSession();
	}

	public void handleSession() {
		Object[] args;
		Request request;
		Request response = null;
		String username;
		int courseNum;
		ResultSet rs;
		boolean runLoop = true;

		try {
			while(runLoop && (request = (Request)in.readObject()) != null) {

				/*Do stuff related to the request type*/
				switch(request.getType()){
				// Universal Requests
				case GET_LOGIN:
					args = request.getArgs();
					if (args.length != 2 ||
							!(args[0] instanceof String) ||
							!(args[1] instanceof String)) {
						response = failure;
						break;
					}
					username = (String)args[0];
					String password = (String)args[1];
					rs = db.queryUserDB(username);
					try {
						if (rs == null || !rs.next()) {
							response = failure;
							break;
						}
						String encryptedPassword =
								rs.getString(UserEntry.COLUMN_NAME_PASSWORD);
						if (passwordEncryptor.checkPassword(password, encryptedPassword)) {
							User u = new User(rs.getString(UserEntry.COLUMN_NAME_USERNAME),
									rs.getString(UserEntry.COLUMN_NAME_FIRSTNAME),
									rs.getString(UserEntry.COLUMN_NAME_LASTNAME),
									rs.getString(UserEntry.COLUMN_NAME_USERTYPE),
									"",
									rs.getString(UserEntry.COLUMN_NAME_EMAIL),
									rs.getString(UserEntry.COLUMN_NAME_DEPARTMENT),
									rs.getString(UserEntry.COLUMN_NAME_CAMPUS));
							response = new Request(RequestType.SEND_RESPONSE,new Object[] {Request.SUCCESS,u});
						} else {
							response = failure;
						}
					} catch (Exception e) {
						response = failure;
					}
					break;
				case GET_LECTURES:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof String)) {
						response = failure;
						break;
					}
					username = (String)args[0];
					rs = db.queryRosterDB(username);
					try {
						if (rs == null) {
							response = failure;
							break;
						}
						ArrayList<Integer> list = new ArrayList<Integer>();
						while(rs.next()) {
							list.add(rs.getInt(RosterEntry.COLUMN_NAME_COURSE_NUM));
						}
						response = new Request(RequestType.SEND_RESPONSE, 
								new Object[] {Request.SUCCESS, list});
					} catch (Exception e) {
						response = failure;
					}
					break;

					// Instructor Requests
				case GET_ROSTER:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					rs = db.queryRosterDB(courseNum);
					try {
						if (rs == null) {
							response = failure;
							break;
						}
						ArrayList<Roster> list = new ArrayList<Roster>();
						while(rs.next()) {
							Roster roster = new Roster(rs.getInt(RosterEntry.COLUMN_NAME_ROSTER_ID),
									rs.getString(RosterEntry.COLUMN_NAME_USERNAME),
									rs.getInt(RosterEntry.COLUMN_NAME_COURSE_NUM),
									rs.getString(RosterEntry.COLUMN_NAME_USERTYPE));
							list.add(roster);
						}
						response = new Request(RequestType.SEND_RESPONSE, 
								new Object[] {Request.SUCCESS, list});
					} catch (Exception e) {
						response = failure;
					}
					break;
				case SEND_START_LECTURE:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					synchronized(lectures) {
						if (lectures.get(courseNum) == null) {
							response = failure;
						} else {
							Lecture l = new Lecture(courseNum);
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case UPDATE_INSTRUCTOR:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case SEND_START_ATTENDANCE:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							l.startAttendance();
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case SEND_END_ATTENDANCE:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							l.stopAttendance();
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case SEND_START_QUIZ:
					args = request.getArgs();
					if (args.length != 2 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							l.startQuiz();
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case SEND_END_QUIZ:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							l.stopQuiz();
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case SEND_HIDE_QUIZ:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							l.hideQuiz();
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case SEND_END_QUESTION:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							l.endQuestion();
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case SEND_INSTRUCTOR_NOTE:
					args = request.getArgs();
					break;
				case SEND_END_LECTURE:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					try {
						synchronized(lectures) {
							if (lectures.get(courseNum) == null) {
								response = failure;
							} else {
								Lecture l = lectures.remove(courseNum);
								if (l == null) {
									response = failure;
								} else {
									response = new Request(RequestType.SEND_RESPONSE,
											new Object[] {Request.SUCCESS, null});
								}
							}
						}
					} catch (Exception e) {
						response = failure;
					}
					break;
				case SEND_DISMISS_QUESTION:
					args = request.getArgs();
					break;
				case SEND_ANSWER_QUESTION:
					args = request.getArgs();
					break;
				case SEND_ACCEPT_STUDENT_NOTE:
					args = request.getArgs();
					break;
				case SEND_DENY_STUDENT_NOTE:
					args = request.getArgs();
					break;

					// Student Requests
				case GET_LECTURE:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					rs = db.queryCourseDB(courseNum);
					try {
						if (rs == null || !rs.next()) {
							response = failure;
							break;
						}
						Course c = new Course(rs.getInt(CourseEntry.COLUMN_NAME_COURSE_NUM),
								rs.getInt(CourseEntry.COLUMN_NAME_ROSTER_COUNT),
								rs.getString(CourseEntry.COLUMN_NAME_INSTRUCTOR),
								rs.getString(CourseEntry.COLUMN_NAME_DEPARTMENT),
								rs.getString(CourseEntry.COLUMN_NAME_EMAIL),
								rs.getString(CourseEntry.COLUMN_NAME_DATES),
								rs.getString(CourseEntry.COLUMN_NAME_TIMES),
								rs.getString(CourseEntry.COLUMN_NAME_BUILDING),
								rs.getString(CourseEntry.COLUMN_NAME_ROOM));
						response = new Request(RequestType.SEND_RESPONSE,new Object[] {Request.SUCCESS,c});
					} catch (Exception e) {
						response = failure;
					}
					break;
				case SEND_JOIN_LECTURE:
					args = request.getArgs();
					if (args.length != 2 || 
							!(args[0] instanceof String) ||
							!(args[1] instanceof Integer)) {
						response = failure;
						break;
					}
					username = (String)args[0];
					courseNum = ((Integer)args[1]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							l.joinLecture(username);
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case UPDATE_STUDENT:
					args = request.getArgs();
					if (args.length != 1 || !(args[0] instanceof Integer)) {
						response = failure;
						break;
					}
					courseNum = ((Integer)args[0]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case SEND_REQUEST_QUESTION:
					args = request.getArgs();
					if (args.length != 2 || 
							!(args[0] instanceof String) ||
							!(args[1] instanceof Integer)) {
						response = failure;
						break;
					}
					username = (String)args[0];
					courseNum = ((Integer)args[1]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							l.requestQuestion(username);
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case SEND_QUESTION:
					args = request.getArgs();
					break;
				case SEND_STUDENT_NOTE:
					args = request.getArgs();
					break;
				case SEND_QUIZ_ANSWER:
					args = request.getArgs();
					if (args.length != 3 || 
							!(args[0] instanceof String) ||
							!(args[1] instanceof Integer) ||
							!(args[2] instanceof String)) {
						response = failure;
						break;
					}
					username = (String)args[0];
					courseNum = ((Integer)args[1]).intValue();
					String answer = (String)args[2];
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							l.answerQuiz(username, answer);
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;
				case SEND_LEAVE_LECTURE:
					args = request.getArgs();
					if (args.length != 2 || 
							!(args[0] instanceof String) ||
							!(args[1] instanceof Integer)) {
						response = failure;
						break;
					}
					username = (String)args[0];
					courseNum = ((Integer)args[1]).intValue();
					synchronized(lectures) {
						Lecture l = lectures.get(courseNum);
						if (l == null) {
							response = failure;
						} else {
							l.leaveLecture(username);
							lectures.put(courseNum, l);
							response = new Request(RequestType.SEND_RESPONSE,
									new Object[] {Request.SUCCESS, l});
						}
					}
					break;

					// Utility Request
				case SEND_RESPONSE:
					response = new Request(RequestType.SEND_RESPONSE,new Object[] {Request.SUCCESS,"ACK"});
					break;
				case CLOSE:
					runLoop = false;
					break;
				default:
					break;
				}
				// Wasn't close.
				if (runLoop) {
					out.writeObject(response);
				}
			}
		} catch (ClassNotFoundException e) {
			new RaiseYourHandException(RaiseYourHandError.OTHER, e.getMessage());
		} catch (IOException e) {
			new RaiseYourHandException(RaiseYourHandError.OTHER, e.getMessage());
		}
	}

	public void closeSession() {
		/*Close I/O streams*/
		try {
			in.close();
			out.close();
			conn.close();
		} catch (IOException e) {
			new RaiseYourHandException(RaiseYourHandError.OTHER, e.getMessage());
		}
	}

	protected void sendObject(ObjectOutputStream out, Object msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

}
