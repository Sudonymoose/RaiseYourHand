package com.ws.local;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.os.AsyncTask;

import com.ws.Request;
import com.ws.RequestType;
import com.ws.SocketInterface;
import com.ws.local.ServerResponseListener;

/**
 * Uses Async, used for all requestTypes besides UPDATEINSTRUCTOR, UPDATESTUDENT, CLOSE.
 * 
 * This class is used by a activities to send messages to the server by constructing it and calling the
 * execute() function inherited by ASyncTask.  The activity that constructs a SendRequest also passes
 * in its own implementation of the SocketResponseListener interface; the onResponse() method
 * in SocketResponseListener is called by SendRequest to use the code customized by the corresponding
 * activity, and this takes in the data response from the server and processes it.
 */
public class SendRequest extends AsyncTask<Void, Void, Void> implements SocketInterface {

	private final String HOST = "angelshark.ics.cs.cmu.edu";
	private final int PORT = 16976;
	private final int MAX_FAIL_COUNT = 5;
	
	// Parameters to open connections
	private Socket sock;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	// Parameters for client-server data
	private RequestType _type;
	private ServerResponseListener _listener;
	private Object[] _raw_obj;
	
	/**
	 * Blank constructor is private, forces outside classes to use constructor with parameters 
	 */
	@SuppressWarnings("unused")
	private SendRequest()
	{
	}
	
	/**
	 * Constructor to set parameters of type of request, message to send, and implementation
	 * of ServerResponseListener.
	 * 
	 * @param Request A Request object that defines the type of Request and the arguments to send
	 * @param listener ServerResponseListener that deals with the response from the server
	 */
	public SendRequest(Request r, ServerResponseListener listener)
	{
		_type = r.getType();
		_listener = listener;
		_raw_obj = r.getArgs();
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
			return false;
		}
		
		// Set up object streams
		try {
			out = new ObjectOutputStream(sock.getOutputStream());
			out.flush();
			in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public void handleSession() {
		
		int failCount = 0;
		
		Request request = new Request(_type, _raw_obj);
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
		} while(failCount < MAX_FAIL_COUNT && !_listener.onResponse(response));
		
		
	}

	@Override
	public void closeSession() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
		}
	}
}
