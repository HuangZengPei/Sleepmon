package com.apm.sleepmon.Fragments;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "DB.db";
    private static final String TABLE_NAME = "diary_table";
    private static final int DB_VERSION = 1;

    public myDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public myDB(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME
                + "(_id INTEGER PRIMARY KEY autoincrement,date TEXT,month TEXT,week TEXT,time TEXT,diary TEXT,sum_time TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("database update!");
    }
}
