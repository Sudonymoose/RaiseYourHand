package com.driver;

import com.Exception.RaiseYourHandException;
import com.ws.remote.RaiseYourHandServer;


public class RunServer {
	public static void main(String[] args){
		int port = 4240;
	
		// Start the server.
		try {
			new RaiseYourHandServer(port).start();
		} catch (RaiseYourHandException e) {
			e.printStackTrace();
		}
	}
}
