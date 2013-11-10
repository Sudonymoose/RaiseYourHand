package com.ws;

/**
 * Class for if the students wants to interact with the instructor, i.e.
 * send a message to start a quiz, or give a student permission for focus.
 */
public class StudentClient extends DefaultClient {

	public StudentClient(String strHost, int iPort) {
		super(strHost, iPort);
	}
}
