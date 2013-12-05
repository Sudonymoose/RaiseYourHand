/**
 * SocketInterface implemented by client to communicate with server.
 * Based off of the class's code.
 */

package com.ws;

public interface SocketInterface {
	boolean openConnection();
	void handleSession();
	void closeSession();
}
