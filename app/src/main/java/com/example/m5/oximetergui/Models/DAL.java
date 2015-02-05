package com.example.m5.oximetergui.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.m5.oximetergui.Constants.SQL_Constants;

public class DAL extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pulse.db";
    private static final int DATABASE_VERSION = 1;

    public DAL(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_Constants.CREATE_DB_SQL_STRING);
            db.execSQL(SQL_Constants.CREATE_READING);
        }
        catch (Exception e)
        {
            String msg = e.getMessage();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_Constants.UPGRADE);
        onCreate(db);
    }
}
