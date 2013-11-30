package com.ws.local;

import com.ws.Request;

/**
 * ServerResponseListener is an interface to be implemented by a subclass of some Activity.
 * The subclass defines what to do given a Request, and an instance of this subclass is passed
 * by its parent Activity to the SendRequest object, so the SendRequest class can use this.
 */
public interface ServerResponseListener {
	
	public boolean onResponse(Request r) ;
	
}
