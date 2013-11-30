package com.entities;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 5182097843121903603L;
	private String username;
	private String first_name;
	private String last_name;
	private String type;
	private String password;
	private String email;
	private String department;
	private String campus;
	
	public User(String username, String first_name, String last_name,
			String type, String password, String email, String department,
			String campus) {
		this.username = username;
		this.first_name = first_name;
		this.last_name = last_name;
		this.type = type;
		this.password = password;
		this.email = email;
		this.department = department;
		this.campus = campus;
	}
	public String getUsername() {
		return username;
	}
	public String getFirstName() {
		return first_name;
	}
	public String getLastName() {
		return last_name;
	}
	public String getType() {
		return type;
	}
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public String getDepartment() {
		return department;
	}
	public String getCampus() {
		return campus;
	}

	

}
