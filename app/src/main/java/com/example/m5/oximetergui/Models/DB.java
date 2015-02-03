package com.example.m5.oximetergui.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.m5.oximetergui.Constants.SQL_Constants;

public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pulse.db";
    private static final int DATABASE_VERSION = 1;

    public DB(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_Constants.CREATE_DB_SQL_STRING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_Constants.UPGRADE);
        onCreate(db);
    }
}
