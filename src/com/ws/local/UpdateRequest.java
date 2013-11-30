package com.ws.local;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;

import com.ws.Request;
import com.ws.RequestType;
import com.ws.SocketInterface;

public class UpdateRequest extends IntentService implements SocketInterface  {


	private final String HOST = "http://localhost/LoginServlet";
	private final int PORT = 80;
	
	private Socket sock;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private RequestType _type;
	private ServerResponseListener _listener;
	
	public UpdateRequest(String name) {
		super(name);
		// TODO Figure out if our RequestType is UpdateInstructor or UpdateStudent
	}
	
    @Override
    protected void onHandleIntent(Intent workIntent) {
    	openConnection();
    	handleSession()
    	closeSession();
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
		
		// Only send stuff if UPDATE_INSTRUCTOR or UPDATE_STUDENT
		if(_type == RequestType.UPDATE_INSTRUCTOR || _type == RequestType.UPDATE_STUDENT)
		{
			// Object[] is null since we're just telling server to update, not sending any information
			Request request = new Request(_type, null);
			Request response = null;
			
			// Continuously send message to update appropriately, nonstop
			while(true)
			{
				// Send the Request and receive a response (as a Request object)
				try {
					out.writeObject(request);
					response = (Request)in.readObject();
				} catch (Exception e) {
					// TODO - error message? Or wait?
				}
			}
			
			// TODO: Process the response?
		}

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
