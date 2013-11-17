package com.dblayout;

import com.dblayout.DatabaseContract.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DatabaseHelper class. This should be constructed and is used to start up the entire database.
 * 
 * Code based off of http://developer.android.com/training/basics/data-storage/databases.html
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Reader.db";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    // Creating the tables in the database
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL(UserEntry.SQL_CREATE_ENTRIES);
    	db.execSQL(RosterEntry.SQL_CREATE_ENTRIES);
    	db.execSQL(CourseEntry.SQL_CREATE_ENTRIES);
    	db.execSQL(StudentEntry.SQL_CREATE_ENTRIES);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserEntry.SQL_DELETE_ENTRIES);
        db.execSQL(RosterEntry.SQL_DELETE_ENTRIES);
        db.execSQL(CourseEntry.SQL_DELETE_ENTRIES);
        db.execSQL(StudentEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}