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
import com.ws.local.ServerResponseListener;

/**
 * Uses Async, used for all requestTypes besides UPDATEINSTRUCTOR, UPDATESTUDENT, CLOSE.
 *
 * TODO: Not sure what the parameters of AsyncTack are...
 */
public class SendRequest extends AsyncTask<Void, Void, Void> implements SocketInterface {

	private final String HOST = "angelshark.ics.cs.cmu.edu";
	private final int PORT = 16976;
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
	@SuppressWarnings("unused")
	private SendRequest()
	{
	}
	
	/**
	 * @param type Type of request to send to server
	 * @param listener ServerResponseListener that deals with the response from the server
	 * @param args Arguments to send to server along with the RequestType; could be empty if not sending anything
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
		
		Request request = new Request(_type, _raw_obj); // do we need to add anything more?
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
}
