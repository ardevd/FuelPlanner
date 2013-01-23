package com.connectutb.xfuel.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper{
	
	/* Our database variables */
	private static final String DATABASE_NAME = "xFuelDB";
	private static final int DATABASE_VERSION = 1;
	/* Our tables and fields */
	private static final String TABLE_HISTORY= "list";
	private static final String HISTORY_ID = "id";
	private Context context;
	
	//constructor
	public DbManager(Context context){
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
    


}
