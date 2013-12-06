package com.ws.local;

import ws.Request;

/**
 * ServerResponseListener is an interface to be implemented by a subclass of some Activity.
 * The subclass defines what to do given a Request, and an instance of this subclass is passed
 * by its parent Activity to the SendRequest object, so the SendRequest class can use this.
 * 
 * This is an interface that allows an Activity to communicate with the Server while providing
 * a convenient way to customize how the program should respond to the server's response
 */
public interface ServerResponseListener {
	
	public boolean onResponse(Request r) ;
	
}
