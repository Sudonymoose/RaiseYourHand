package com.driver;

import com.Exception.RaiseYourHandException;
import com.dblayout.backend.ManageDatabase;

public class populate {
	public static void main(String[] args){
		try {
			ManageDatabase md = new ManageDatabase();
			md.addToUserDB("hanruiz", "Hanrui", "Zhang", "student", "hanrui_18641", 
					"hanruiz@andrew.cmu.edu", "ECE", "Pittsburgh");
			md.addToUserDB("ayoo", "Alex", "Yoo", "student", "ayoo_18641", 
					"ayoo@andrew.cmu.edu", "ECE", "Pittsburgh");
			md.addToUserDB("arthurc", "Arthur", "Chang", "student", "arthurc_18641", 
					"arthurc@andrew.cmu.edu", "ECE", "Silicon Valley");
			md.addToUserDB("bsingh", "Bob", "Singh", "instructor", "bsingh_18641", 
					"bsingh@andrew.cmu.edu", "ECE", "Silicon Valley");
			md.addToUserDB("tom", "Tom", "Sullivan", "instructor", "tom_18100", 
					"tom@ece.cmu.edu", "ECE", "Pittsburgh");
			
			md.addToCourseDB(18641, 80, "bsingh", "ECE", "cis4lab@gmail.com",
					"MW", "10:30 AM", "HH", "1307");
			md.addToCourseDB(18100, 120, "tom", "ECE", "18100-staff@ece.cmu.edu",
					"TR", "1:30 PM", "PH", "100");
			
			md.addToRosterDB("hanruiz", 18641, "student");
			md.addToRosterDB("ayoo", 18641, "student");
			md.addToRosterDB("bsingh", 18641, "instructor");
			
			md.addToRosterDB("arthurc", 18100, "student");
			md.addToRosterDB("tom", 18100, "instructor");
			
			
		} catch (RaiseYourHandException e) {
			e.printStackTrace();
		}
		
		
	}
}
