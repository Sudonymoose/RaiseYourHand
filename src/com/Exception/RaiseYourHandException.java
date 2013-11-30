/**
 * 
 */
package com.Exception;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author arthurc
 *
 */
public class RaiseYourHandException extends Exception {
	private static final long serialVersionUID = -630101487975021196L;
	private RaiseYourHandError _errorNum = RaiseYourHandError.OTHER;
	private String message = null;
	private static String logfile = "LogFileUnit4.txt";

	public RaiseYourHandException() {
		super();
		printProblem();
	}

	public RaiseYourHandException(RaiseYourHandError errorNum) {
		super();
		this._errorNum = errorNum;
		logProblem();
		printProblem();
	}

	public RaiseYourHandException(String message) {
		super(message);
		this.message = message;
		logProblem();
		printProblem();
	}
	
	public RaiseYourHandException(RaiseYourHandError errorNum, String message) {
		super(message);
		this._errorNum = errorNum;
		this.message = message;
		logProblem();
		printProblem();
	}

	public RaiseYourHandError getErrorNum() {
		return _errorNum;
	}

	public void setErrorNum(RaiseYourHandError errorNum) {
		this._errorNum = errorNum;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void printProblem() {
		System.out.println(toString());
	}
	
	public void logProblem() {
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logfile, true)));
		    out.println(toString());
		    out.close();
		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
			System.out.println("Unable to log exception because either the file " + logfile + " cannot be opened/read " +
					"or it is a directory.");
			System.out.println("Please inspect file permissions or try another file.");
		}
	}

	@Override
	public String toString() {
		return "RaiseYourHandException [_errorNum=" + _errorNum + ", message=" + message;
	}
}
