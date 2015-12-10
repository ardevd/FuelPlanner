package com.connectutb.xfuel.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.connectutb.xfuel.providers.AircraftContract;
import com.connectutb.xfuel.providers.HistoryContract;

public class DbManager extends SQLiteOpenHelper implements AircraftContract, HistoryContract{

    public static final String DB_NAME = "xfuel.db";
    public static final int DB_VERSION = 21;
    private final static String TAG = "xFuel";

    public DbManager(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql_aircraft = String.format("CREATE TABLE %s ( %s INT PRIMARY KEY,"
                        + "%s TEXT, %s TEXT);", TABLE_AIRCRAFT,
                AIRCRAFT_ID, AIRCRAFT_CODE, AIRCRAFT_NAME);

        Log.i(TAG, "Created aircraft database");
        db.execSQL(sql_aircraft);

        String sql_history = String.format("CREATE TABLE %s ( %s INT PRIMARY KEY,"
                        + "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INT, %s INT, %s INTEGER);", TABLE_HISTORY,
                HISTORY_ID, HISTORY_DEPARTURE, HISTORY_ARRIVAL, HISTORY_AIRCRAFT, HISTORY_OEW, HISTORY_MTANK, HISTORY_TTL, HISTORY_TANKER, HISTORY_UNIT, HISTORY_FAVORITE, HISTORY_TIMESTAMP);

        Log.i(TAG, "Created history database");
        db.execSQL(sql_history);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Legacy tables
        db.execSQL("DROP TABLE IF EXISTS list");
        db.execSQL("DROP TABLE IF EXISTS favorites");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AIRCRAFT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);

    }

    public Cursor findAircraftById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_AIRCRAFT, null, AIRCRAFT_ID + "=?", new String[] { Long.toString(id) }, null, null, null);
    }

    public Cursor findAllAircraft(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_AIRCRAFT, null, null, null, null, null, null);
    }

    public Cursor findHistoryById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_HISTORY, null, HISTORY_ID + "=?", new String[] { Long.toString(id) }, null, null, null);
    }
}
