package com.connectutb.xfuel.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        SQLiteDatabase database = db.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_HISTORY);
        switch (matcher.match(uri)){
            case URI_ONE_HISTORY:
                long id = ContentUris.parseId(uri);
                c = db.findHistoryById(id);
                break;
            case URI_ALL_HISTORY:
                c = qb.query(database, projection, selection, selectionArgs, null, null, sortOrder, null);
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
        long rowID = database.insert(TABLE_HISTORY, null, contentValues);
        if (rowID > 0) {
            result = ContentUris.withAppendedId(HISTORY_CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(result, null);
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase database = db.getWritableDatabase();
        int count;

        switch (matcher.match(uri)){

            case URI_ALL_HISTORY:
                count = database.delete(TABLE_HISTORY, where, whereArgs);
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
