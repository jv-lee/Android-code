package com.gionee.gnservice.sdk.member.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Created by caocong on 11/25/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "privilege.db";

    private static final int DB_BASE_VERSION = 100;

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createCateTable(db);
        createContentTable(db);
        createVersionTable(db);
        createUpdateTimeTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createCateTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + Columns.CATE_TABLE
                + " (" + Columns.CateColumns._ID + " INTEGER PRIMARY KEY, "
                + Columns.CateColumns.ID + " LONG,"
                + Columns.CateColumns.NAME + " TEXT,"
                + Columns.CateColumns.DESC + " TEXT,"
                + Columns.CateColumns.ICON + " TEXT,"
                + Columns.CateColumns.ICON_2 + " TEXT,"
                + Columns.CateColumns.IMG + " TEXT,"
                + Columns.CateColumns.MEMBER_LEVEL + " TEXT)";

        db.execSQL(sql);
    }

    private void createContentTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + Columns.CONTENT_TABLE
                + " (" + Columns.CateColumns._ID + " INTEGER PRIMARY KEY, "
                + Columns.ContentColumns.ID + " LONG,"
                + Columns.ContentColumns.NAME + " TEXT,"
                + Columns.ContentColumns.CONTENT + " TEXT,"
                + Columns.ContentColumns.CID + " LONG)";
        db.execSQL(sql);
    }

    private void createVersionTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + Columns.VERSION_TABLE
                + " (" + Columns.VersionColumns.VERSION + " TEXT)";
        db.execSQL(sql);
    }

    private void createUpdateTimeTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + Columns.UPDATE_TIME_TABLE
                + " (" + Columns.UpdateTimeColumns.UPDATE_TIME + " LONG)";
        db.execSQL(sql);
    }

}
