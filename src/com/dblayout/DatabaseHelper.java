package com.dblayout;

/**
 * 
 * Code based off of http://developer.android.com/training/basics/data-storage/databases.html
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Reader.db";

    // Constructor
    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    // Creating the tables in the database
    public void onCreate(SQLiteDatabase db) {
        // TODO: Create all four tables here
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Delete all the tables before onCreate()
        onCreate(db);
    }
}