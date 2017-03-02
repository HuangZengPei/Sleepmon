package com.apm.sleepmon.Fragments;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class myDB2 extends SQLiteOpenHelper {
    private static final String DB_NAME = "DB2.db";
    private static final String TABLE_NAME = "grade_table";
    private static final int DB_VERSION = 1;

    public myDB2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public myDB2(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME
                    + "(_id INTEGER PRIMARY KEY autoincrement,time TEXT," +
                    "sumOfSleep INTEGER," +
                    "timeOfSleep DOUBLE,grade DOUBLE)";
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
            Log.i("e", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("database update!");
    }
}
