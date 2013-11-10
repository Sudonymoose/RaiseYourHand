package com.ws;

/**
 * Interface for Client functions
 *
 * Taken from 18-641 Socket Programming PPT slides
 */
public interface ClientMethods {
	boolean openConnection();
	void handleSession();
	void closeSession();
}
