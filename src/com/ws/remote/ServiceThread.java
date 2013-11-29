/**
 * 
 */
package ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import ws.Request;
import Exception.RaiseYourHandError;
import Exception.RaiseYourHandException;

/**
 * @author arthurc
 *
 */
public class ServiceThread extends Thread {
	private Socket conn;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	// Warning, the BuildRaiseYourHand will be shared with other threads and the master server
	ArrayList<String> models = new ArrayList<String>();

	public ServiceThread(Socket conn, ObjectInputStream in, ObjectOutputStream out) {
		this.conn = conn;
		this.in = in;
		this.out = out;
	}

	public void run() {
		handleSession();
		closeSession();
	}

	public void handleSession() {
		Object[] args;
		Request request;
		boolean runLoop = true;

		try {
			while(runLoop && (request = (Request)in.readObject()) != null) {

				/*Do stuff related to the request type*/
				switch(request.getType()){
				// Universal Requests
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
				case CLOSE:
					runLoop = false;
					break;
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
