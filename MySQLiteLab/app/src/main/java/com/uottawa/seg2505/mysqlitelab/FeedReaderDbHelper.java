package com.uottawa.seg2505.mysqlitelab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uottawa.seg2505.mysqlitelab.FeedReaderContract.FeedEntry;

public class FeedReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // add a row
    public long addEntry(String title, String subtitle) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);
        return db.insert(FeedEntry.TABLE_NAME, null, values);
    }

    // find the subtitle for a title (null if not found)
    public String findSubtitle(String title) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,
                new String[]{FeedEntry.COLUMN_NAME_SUBTITLE},
                FeedEntry.COLUMN_NAME_TITLE + " = ?",
                new String[]{title},
                null, null, null);
        String result = null;
        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
        }
        cursor.close();
        return result;
    }

    // delete rows with this title, returns how many were deleted
    public int deleteEntry(String title) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(FeedEntry.TABLE_NAME,
                FeedEntry.COLUMN_NAME_TITLE + " = ?",
                new String[]{title});
    }
}