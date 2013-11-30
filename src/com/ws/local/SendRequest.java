package com.ws.local;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;

import android.os.AsyncTask;

import com.ws.Request;
import com.ws.RequestType;
import com.ws.SocketInterface;

/**
 * Uses Async, used for all requestTypes besides UPDATEINSTRUCTOR, UPDATESTUDENT, CLOSE.
 *
 * TODO: Not sure what the parameters of AsyncTack are...
 */
public class SendRequest extends AsyncTask<Void, Void, Void> implements SocketInterface {

	private final String HOST = "http://localhost/LoginServlet";
	private final int PORT = 80;
	private final int MAX_FAIL_COUNT = 5;
	
	private Socket sock;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private RequestType _type;
	private ServerResponseListener _listener;
	private Object[] _raw_obj;
	
	/**
	 * Blank constructor is private, forces outside classes to use constructor with parameters 
	 */
	private SendRequest()
	{
	}
	
	/**
	 * @param type Type of request to send to server
	 * @param listener ServerResponseListener that deals with the response from the server
	 * @param args Arguments to send to server along with the RequestType; could be empty if not sending anything
	 */
	public SendRequest(RequestType type, ServerResponseListener listener, Object[] args)
	{
		_type = type;
		_listener = listener;
		_raw_obj = args;
	}
	

	@Override
	protected Void doInBackground(Void... arg0) {
		openConnection();
		handleSession();
		closeSession();
		return null;
	}
	
	@Override
	public boolean openConnection() {
		
		// Set up socket
		try {
			sock = new Socket(HOST, PORT);
			System.out.println("Config Client has a socket with "+HOST +":" + PORT);
		} catch (IOException e) {
			// TODO: Print error?
			return false;
		}
		
		// Set up streams
		try {
			out = new ObjectOutputStream(sock.getOutputStream());
			out.flush();
			in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			// TODO: Print error?
			return false;
		}
		return true;
	}

	@Override
	public void handleSession() {
		
		int failCount = 0;
		
		Object[] args = getObjects();
		Request request = new Request(_type, args);
		Request response = null;
		
		// Repeat attempts to send; if it succeeds, exit. If not, repeat until MAX_FAIL_COUNT.
		do
		{
			// Send the Request and receive a response (as a Request object)
			try {
				out.writeObject(request);
				response = (Request)in.readObject();
			} catch (Exception e) {
				failCount++;
			}
			// failCount++; Any way for the code to not catch except yet still not communicate properly?
		} while(failCount < MAX_FAIL_COUNT && !_listener.onResponse(response));
		
		
	}

	@Override
	public void closeSession() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			// TODO: Print error?
		}
	}
	
	/**
	 * Helper method, basically just a huge switch statement for each RequestType
	 * 
	 * TODO: Fill each case as appropriate. I'm sure not all of them need to be filled.
	 */
	private Object[] getObjects()
	{
		switch(_type) {
		
			case GET_LOGIN:
			
			break;
			
			case GET_LECTURES:
				
			break;
			
			case GET_ROSTER:
				
			break;
			
			case SEND_START_LECTURE:
				
			break;
			
			case SEND_START_ATTENDANCE:
				
			break;
			
			case GET_ATTENDANCE:
				
			break;
			
			case SEND_END_ATTENDANCE:
				
			break;
			
			case SEND_START_QUIZ:
				
			break;
			
			case GET_QUIZ:
				
			break;
			
			case SEND_END_QUIZ:
				
			break;
			
			case SEND_INSTRUCTOR_NOTE:
				
			break;
			
			case SEND_END_LECTURE:
				
			break;
			
			case GET_LECTURE:
				
			break;
			
			case SEND_JOIN_LECTURE:
				
			break;
			
			case UPDATE_STUDENT:
				
			break;
			
			case SEND_QUESTION:
				
			break;
			
			case SEND_STUDENT_NOTE:
				
			break;
			
			case SEND_QUIZ_ANSWER:
				
			break;
			
			case SEND_LEAVE_LECTURE:
				
			break;
		
		}
		return null;
	}
	
}
