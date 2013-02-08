package com.connectutb.xfuel.util;

import java.util.ArrayList;

import com.connectutb.xfuel.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DbManager extends SQLiteOpenHelper{
	
	/* Our database variables */
	private static final String DATABASE_NAME = "xFuelDB";
	private static final int DATABASE_VERSION = 2;
	/* Our tables and fields */
	private static final String TABLE_HISTORY= "list";
	private static final String HISTORY_ID = "id";
	private static final String HISTORY_AIRCRAFT = "aircraft";
	private static final String HISTORY_ORIG = "origin";
	private static final String HISTORY_DEST = "destination";
	private static final String HISTORY_UNIT = "unit";
	private static final String HISTORY_METAR = "metar";
	private static final String HISTORY_RULES = "rules";
	
	private static final String TABLE_FAV = "favorites";
	private static final String FAV_ID = "id";
	private static final String FAV_AIRCRAFT = "aircraft";
	private static final String FAV_ORIG = "origin";
	private static final String FAV_DEST = "destination";
	private static final String FAV_UNIT = "unit";
	private static final String FAV_METAR = "metar";
	private static final String FAV_RULES = "rules";
	private static final String FAV_NAME = "name";
	
	private Context context;
	
	//constructor
	public DbManager(Context context){
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Create database and tables
		String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
				+ HISTORY_ID + " INTEGER PRIMARY KEY," + HISTORY_AIRCRAFT + " TEXT,"
				+ HISTORY_ORIG + " TEXT," + HISTORY_DEST + " TEXT," + HISTORY_UNIT + " TEXT,"
				+ HISTORY_METAR + " TEXT," + HISTORY_RULES + " TEXT)";
		String CREATE_FAV_TABLE = "CREATE TABLE " + TABLE_FAV + "("
				+ FAV_ID + " INTEGER PRIMARY KEY," + FAV_AIRCRAFT + " TEXT,"
				+ FAV_ORIG + " TEXT," + FAV_DEST + " TEXT," + FAV_UNIT + " TEXT,"
				+ FAV_METAR + " TEXT," + FAV_RULES + " TEXT, " + FAV_NAME + " TEXT)";
		//Execute db queries
		db.execSQL(CREATE_HISTORY_TABLE);
		db.execSQL(CREATE_FAV_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop tables and create a new ones
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
		//The create a new one
		onCreate(db);
		
	}
	
	public void addFavorite(String aircraft, String orig, String dest, boolean metar, String rules, String units, String name){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FAV_AIRCRAFT, aircraft);
		values.put(FAV_ORIG, orig);
		values.put(FAV_DEST, dest);
		values.put(FAV_UNIT, units);
		if (metar){
			values.put(FAV_METAR, "YES");
		}else{
			values.put(FAV_METAR, "NO");
		}
		values.put(FAV_RULES, rules);
		values.put(FAV_NAME, name);
		
		//Inserting the record
		db.insert(TABLE_FAV, null, values);
		db.close();
		
		//Toast
		Toast.makeText(context, context.getString(R.string.added_favorite), Toast.LENGTH_SHORT).show();
	}
	
	public void deleteFavorite(String id){
		//Delete item from favorites 
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		String sql = "DELETE FROM " + TABLE_FAV + " WHERE " + FAV_ID + "=" + id;
		db.execSQL(sql);
	}
	
	public void deleteHistory(){
		SQLiteDatabase db = this.getWritableDatabase();
		
		String sql = "DELETE FROM " + TABLE_HISTORY;
		db.execSQL(sql);
	}
    
	public void addToHistory(String aircraft, String orig, String dest, boolean metar, String rules, String units){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(HISTORY_AIRCRAFT, aircraft);
		values.put(HISTORY_ORIG, orig);
		values.put(HISTORY_DEST, dest);
		values.put(HISTORY_UNIT, units);
		if (metar){
			values.put(HISTORY_METAR, "YES");
		}else{
			values.put(HISTORY_METAR, "NO");
		}
		values.put(HISTORY_RULES, rules);
		
		//Inserting the record
		db.insert(TABLE_HISTORY, null, values);
		db.close();
	}
	
	public String[] listFavorites(){
		//Retrieve a string array with the history
		ArrayList temp_array = new ArrayList();
		String[] fav_array = new String[0];
		//SQL 
		String sqlQuery = "SELECT * FROM " + TABLE_FAV;
		//Define database and cursor
		SQLiteDatabase db = this.getWritableDatabase(); 
		Cursor c = db.rawQuery(sqlQuery, null);
		
		//Loop through the results and add it to the temp_array
		if (c.moveToFirst()){
			do{
				temp_array.add(c.getString(c.getColumnIndex(FAV_ID)) + ";" + c.getString(c.getColumnIndex(FAV_ORIG))+ ";" 
						+ c.getString(c.getColumnIndex(FAV_DEST)) +  ";" + c.getString(c.getColumnIndex(FAV_AIRCRAFT)) + ";" 
						+ c.getString(c.getColumnIndex(FAV_RULES)) + ";"
						+ c.getString(c.getColumnIndex(FAV_UNIT)) + ";"
						+ c.getString(c.getColumnIndex(FAV_METAR)) + ";"
						+ c.getString(c.getColumnIndex(FAV_NAME)));
			}while(c.moveToNext());
		}
		
		//Close cursor
		c.close();
		//Transfer from arraylist to string array
		fav_array = (String[]) temp_array.toArray(fav_array);
		//Return the string array
		return fav_array;
	}
	
	public String[] listHistory(){
		//Retrieve a string array with the history
		ArrayList temp_array = new ArrayList();
		String[] history_array = new String[0];
		//SQL 
		String sqlQuery = "SELECT * FROM " + TABLE_HISTORY;
		//Define database and cursor
		SQLiteDatabase db = this.getWritableDatabase(); 
		Cursor c = db.rawQuery(sqlQuery, null);
		
		//Loop through the results and add it to the temp_array
		if (c.moveToFirst()){
			do{
				temp_array.add(c.getString(c.getColumnIndex(HISTORY_ID)) + ";" + c.getString(c.getColumnIndex(HISTORY_ORIG))+ ";" 
						+ c.getString(c.getColumnIndex(HISTORY_DEST)) +  ";" + c.getString(c.getColumnIndex(HISTORY_AIRCRAFT)) + ";" 
						+ c.getString(c.getColumnIndex(HISTORY_RULES)) + ";"
						+ c.getString(c.getColumnIndex(HISTORY_UNIT)) + ";"
						+ c.getString(c.getColumnIndex(HISTORY_METAR)));
			}while(c.moveToNext());
		}
		
		//Close cursor
		c.close();
		//Transfer from arraylist to string array
		history_array = (String[]) temp_array.toArray(history_array);
		//Return the string array
		return history_array;
	}


}
