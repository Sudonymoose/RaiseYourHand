/**
 * 
 */
package com.ws;

import java.io.Serializable;

/**
 * Requests are passed through the sockets
 * 
 * The type will define which backend method is called
 * the args array contains all arguments to the backend method
 * specified through the type
 * 
 * @author arthurc
 *
 */
public class Request implements Serializable{
	/**
	 * 
	 */
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	private static final long serialVersionUID = 193459061659991600L;
	private RequestType type;
	private Object[] args;
	
	
	public Request(RequestType type, Object[] args){
		this.type = type;
		this.args = args;
	}
	
	public RequestType getType(){
		return type;
	}
	
	public Object[] getArgs(){
		return args;
	}
}
