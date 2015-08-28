package com.connectutb.xfuel.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.connectutb.xfuel.tools.DbManager;

/**
 * Created by eholst on 28.08.15.
 */
public class AircraftProvider extends ContentProvider implements AircraftContract {

    private DbManager db;
    private final UriMatcher matcher = new UriMatcher(0);

    private final static int URI_ALL_AIRCRAFT = 1;
    private final static int URI_ONE_AIRCRAFT = 2;

    public AircraftProvider(){
        matcher.addURI(AIRCRAFT_AUTHORITY, "/" + AIRCRAFT_ITEM + "/#", URI_ONE_AIRCRAFT);
        matcher.addURI(AIRCRAFT_AUTHORITY, "/" + AIRCRAFT_ITEM, URI_ALL_AIRCRAFT);
    }

    @Override
    public boolean onCreate() {
        db = new DbManager(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;

        switch (matcher.match(uri)){
            case URI_ONE_AIRCRAFT:
                long id = ContentUris.parseId(uri);
                c = db.findAircraftById(id);
                break;
            case URI_ALL_AIRCRAFT:
                c = db.findAllAircraft();
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri result = null;

        SQLiteDatabase database = db.getWritableDatabase();
        long rowID = database.insert(TABLE_AIRCRAFT, null, contentValues);
        if (rowID > 0) {
            result = ContentUris.withAppendedId(AIRCRAFT_CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(result, null);
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase database = db.getWritableDatabase();
        int count;

        switch (matcher.match(uri)){
            case URI_ONE_AIRCRAFT:
                String segment = uri.getLastPathSegment();
                String whereClause = AIRCRAFT_ID + "=" + segment
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : "");
                count = database.delete(TABLE_AIRCRAFT, whereClause, whereArgs);
                break;
            case URI_ALL_AIRCRAFT:
                count = database.delete(TABLE_AIRCRAFT, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (count >0 ){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
