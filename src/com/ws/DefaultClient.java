package com.ws;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Abstract class for client, to be used by InstructorClient and StudentClient
 */
public abstract class DefaultClient extends Thread implements ClientMethods, ClientConstants {

	protected ObjectInputStream reader;
	protected ObjectOutputStream writer;
	protected Socket sock;
	protected String strHost;
	protected int iPort;
	
	public DefaultClient(String strHost, int iPort)
	{
		setPort(iPort);
		setHost(strHost);
	}
	
	@Override
	public void run()
	{
		
	}

	/**
	 * Open the connection with the socket set up. Placeholder.
	 */
	@Override
	public boolean openConnection()
	{	
		try {
			sock = new Socket(strHost, iPort);
		}
		catch(IOException socketError) {
			if(DEBUG) System.err.println("Unable to connect to " + strHost);
			return false;
		}
		
		if(DEBUG) System.out.println("Connection Successful!");
		
		try {
			reader = new ObjectInputStream(sock.getInputStream());
			writer = new ObjectOutputStream(sock.getOutputStream());
		}
		catch(Exception e) {
			if(DEBUG) System.err.println("Unable to obtain stream to/from " + strHost);
			return false;
		}
		
		if(DEBUG) System.out.println("Obtained stream to/from " + strHost + "!");
		
		return true;
	}
	
	/**
	 * Handle the current session
	 */
	@Override
	public void handleSession()
	{
		
	}
	
	/**
	 * Close the connection. Placeholder.
	 */
	@Override
	public void closeSession()
	{
		try {
			writer = null;
			reader = null;
			sock.close();
		}
		catch (IOException e) {
			if(DEBUG) System.err.println("Error closing socket to " + strHost);
		}
	}
	
	/**
	 * Setter for host name
	 * @param strHost
	 */
	public void setHost(String strHost)
	{
		this.strHost = strHost;
	}
	
	/**
	 * Setter for port
	 * @param iPort
	 */
	public void setPort(int iPort)
	{
		this.iPort = iPort;
	}
	
}
