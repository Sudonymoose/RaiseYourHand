package com.dblayout;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.Exception.RaiseYourHandError;
import com.Exception.RaiseYourHandException;
import com.foo.DatabaseContract.CourseEntry;
import com.foo.DatabaseContract.RosterEntry;
import com.foo.DatabaseContract.UserEntry;


/**
 * DatabaseHelper class. This should be constructed and is used to start up the entire database.
 * 
 * Code based off of http://developer.android.com/training/basics/data-storage/databases.html
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Reader.db";
	private SQLiteDatabase database;

	// Constructor
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		database = this.getWritableDatabase();
		System.out.println("Opened database successfully");
	}

	protected void executeUpdate(String sql) {
		stmt.executeUpdate(sql);
	}

	protected ResultSet executeQuery(String sql) {
		ResultSet rs = null;
		rs = stmt.executeQuery(sql);
		return rs;
	}

	public void onClose() {
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(UserEntry.SQL_CREATE_ENTRIES);
		db.execSQL(RosterEntry.SQL_CREATE_ENTRIES);
		db.execSQL(CourseEntry.SQL_CREATE_ENTRIES);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(UserEntry.SQL_DELETE_ENTRIES);
		db.execSQL(RosterEntry.SQL_DELETE_ENTRIES);
		db.execSQL(CourseEntry.SQL_DELETE_ENTRIES);		
		onCreate(db);
	}
}
