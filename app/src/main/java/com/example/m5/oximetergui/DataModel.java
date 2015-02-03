package com.example.m5.oximetergui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Hunt on 2/2/2015.
 */
public class DataModel {

    DB _db;

    public DataModel(Context context)
    {
        _db = new DB(context);
    }

    public List<Data> getDataByID(int id)
    {
        return null;
    }

}
