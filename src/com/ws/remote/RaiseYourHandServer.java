package com.ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.Exception.RaiseYourHandError;
import com.Exception.RaiseYourHandException;
import com.dblayout.backend.ManageDatabase;
import com.ws.SocketInterface;

public class RaiseYourHandServer extends Thread implements SocketInterface {
	private int port;
	private ServerSocket servSock = null;
	private ManageDatabase db;

	public RaiseYourHandServer() throws RaiseYourHandException {
		setPort(port);
		try {
			db = new ManageDatabase();
		} catch (Exception e) {
			throw new RaiseYourHandException(RaiseYourHandError.SQL_FAILURE, e.getMessage());
		}
	}

	public void run() {
		if (openConnection()) {
			handleSession();
			closeSession();
		}
	}

	@Override
	public boolean openConnection() {
		try {
			servSock = new ServerSocket(port);
			System.out.println("Config Server has a socket and is listening on port "+port);
		} catch (IOException e) {
			new RaiseYourHandException(RaiseYourHandError.SOCKET_FAILURE, e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void handleSession() {
		while(true) {
			try {
				Socket connection = servSock.accept();
				ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
				out.flush();
				ObjectInputStream in = new ObjectInputStream(connection.getInputStream());

				new ServiceThread(connection, in, out, db).start();
			} catch (IOException e) {
				new RaiseYourHandException(RaiseYourHandError.OTHER, e.getMessage());			
			}
		}
	}

	@Override
	public void closeSession() {
		try {
			servSock.close();
		} catch (IOException e) {
			// Log that the thread was interrupted.
			new RaiseYourHandException(RaiseYourHandError.OTHER, e.getMessage());		
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}
}
