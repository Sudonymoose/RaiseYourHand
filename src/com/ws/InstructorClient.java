package com.ws;

/**
 * Class for if the instructor wants to interact with the students, i.e.
 * send a message to start a quiz, or give a student permission for focus.
 */
public class InstructorClient extends DefaultClient {

	public InstructorClient(String strHost, int iPort) {
		super(strHost, iPort);
	}

}
