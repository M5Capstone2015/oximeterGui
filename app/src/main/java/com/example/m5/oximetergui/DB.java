package com.example.m5.oximetergui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pulse.db";
    private static final int DATABASE_VERSION = 1;

    public DB(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        db.execSQL( "CREATE TABLE " + PATIENT_TABLE_NAME+ " (" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TIME
                + " INTEGER," + TITLE + " TEXT NOT NULL);" );
        */
        db.execSQL(SQL_Constants.CREATE_DB_SQL_STRING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_Constants.UPGRADE);
        onCreate(db);
    }
}