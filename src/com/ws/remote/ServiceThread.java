/**
 * 
 */
package com.ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jasypt.util.password.StrongPasswordEncryptor;

import com.Exception.RaiseYourHandError;
import com.Exception.RaiseYourHandException;
import com.dblayout.DatabaseContract.UserEntry;
import com.dblayout.backend.ManageDatabase;
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
	// Warning, the BuildRaiseYourHand will be shared with other threads and the master server
	ArrayList<String> models = new ArrayList<String>();

	public ServiceThread(Socket conn, ObjectInputStream in, ObjectOutputStream out, ManageDatabase db) {
		this.conn = conn;
		this.in = in;
		this.out = out;
		this.db = db;
	}

	public void run() {
		handleSession();
		closeSession();
	}

	public void handleSession() {
		Object[] args;
		Request request;
		Request response = null;
		boolean runLoop = true;

		try {
			while(runLoop && (request = (Request)in.readObject()) != null) {

				/*Do stuff related to the request type*/
				switch(request.getType()){
				// Universal Requests
				case GET_LOGIN:
					args = request.getArgs();
					if (args.length != 2 ||
							args[0] instanceof String ||
							args[1] instanceof String) {
						response = new Request(RequestType.SEND_RESPONSE,new Object[] {"ERROR"});
						break;
					}
					String username = (String)args[0];
					String password = (String)args[1];
					ResultSet rs = db.queryUserDB(username);
					try {
						if (rs == null || !rs.next()) {
							response = new Request(RequestType.SEND_RESPONSE,new Object[] {"ERROR"});
							break;
						}
						String encryptedPassword = rs.getString(UserEntry.COLUMN_NAME_PASSWORD);
						StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
						if (passwordEncryptor.checkPassword(password, encryptedPassword)) {
							User u = new User(rs.getString(UserEntry.COLUMN_NAME_USERNAME),
									rs.getString(UserEntry.COLUMN_NAME_FIRSTNAME),
									rs.getString(UserEntry.COLUMN_NAME_LASTNAME),
									rs.getString(UserEntry.COLUMN_NAME_USERTYPE),
									"",
									rs.getString(UserEntry.COLUMN_NAME_EMAIL),
									rs.getString(UserEntry.COLUMN_NAME_DEPARTMENT),
									rs.getString(UserEntry.COLUMN_NAME_CAMPUS));
							response = new Request(RequestType.SEND_RESPONSE,new Object[] {u});
						} else {
							response = new Request(RequestType.SEND_RESPONSE,new Object[] {"ERROR"});
						}
					} catch (SQLException e) {
						response = new Request(RequestType.SEND_RESPONSE,new Object[] {"ERROR"});
					}
					break;
				case GET_LECTURES:
					args = request.getArgs();
					break;

					// Instructor Requests
				case GET_ROSTER:
					args = request.getArgs();
					break;
				case SEND_START_LECTURE:
					args = request.getArgs();
					break;
				case UPDATE_INSTRUCTOR:
					args = request.getArgs();
					break;
				case SEND_START_ATTENDANCE:
					args = request.getArgs();
					break;
				case GET_ATTENDANCE:
					args = request.getArgs();
					break;
				case SEND_END_ATTENDANCE:
					args = request.getArgs();
					break;
				case SEND_START_QUIZ:
					args = request.getArgs();
					break;
				case GET_QUIZ:
					args = request.getArgs();
					break;
				case SEND_END_QUIZ:
					args = request.getArgs();
					break;
				case SEND_INSTRUCTOR_NOTE:
					args = request.getArgs();
					break;
				case SEND_END_LECTURE:
					args = request.getArgs();
					break;

					// Student Requests
				case GET_LECTURE:
					args = request.getArgs();
					break;
				case SEND_JOIN_LECTURE:
					args = request.getArgs();
					break;
				case UPDATE_STUDENT:
					args = request.getArgs();
					break;
				case SEND_QUESTION:
					args = request.getArgs();
					break;
				case SEND_STUDENT_NOTE:
					args = request.getArgs();
					break;
				case SEND_QUIZ_ANSWER:
					args = request.getArgs();
					break;
				case SEND_LEAVE_LECTURE:
					args = request.getArgs();
					break;

					// Utility Request
				case SEND_RESPONSE:
					response = new Request(RequestType.SEND_RESPONSE,new Object[] {"ACK"});
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
