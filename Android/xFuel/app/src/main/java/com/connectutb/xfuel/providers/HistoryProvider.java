package com.connectutb.xfuel.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.connectutb.xfuel.tools.DbManager;

public class HistoryProvider extends ContentProvider implements HistoryContract {

    private DbManager db;
    private final UriMatcher matcher = new UriMatcher(0);

    private final static int URI_ALL_HISTORY= 1;
    private final static int URI_ONE_HISTORY = 2;

    public HistoryProvider() {
        matcher.addURI(HISTORY_AUTHORITY, "/" + HISTORY_ITEM + "/#", URI_ONE_HISTORY);
        matcher.addURI(HISTORY_AUTHORITY, "/" + HISTORY_ITEM, URI_ALL_HISTORY);
    }

    @Override
    public boolean onCreate() {
        db = new DbManager(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri result = null;

        SQLiteDatabase database = db.getWritableDatabase();
        long rowID = database.insert(TABLE_HISTORY, null, contentValues);
        if (rowID > 0) {
            result = ContentUris.withAppendedId(HISTORY_CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(result, null);
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
