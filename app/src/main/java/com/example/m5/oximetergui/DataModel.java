package com.example.m5.oximetergui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;

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

    /**
     * Fetches a reading by ID.
     * @param id
     * @return
     */
    public Data getDataByID(int id) // TODO test
    {
        SQLiteDatabase db = _db.getReadableDatabase();
        String queryString = String.format(SQL_Constants.SELECT_DATA_BY_ID, id);
        Cursor cursor = db.rawQuery(queryString, null);

        Data data = null;
        while (cursor.moveToNext())
        {
            int ID = cursor.getInt(0);
            String startTime = cursor.getString(1);
            String endTime = cursor.getString(2);
            String readingData = cursor.getString(3);
            data = new Data(ID, startTime, endTime, readingData);
        }

        return data;
    }

    /**
     * Creates a new reading.
     * @param data
     * @return
     */
    public boolean AddNewReading(Data data)  // TODO test
    {

        SQLiteDatabase db = _db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SQL_Constants.DATA_STARTDATE_COLUMN, data.StartDate);
        values.put(SQL_Constants.DATA_ENDDATE_COLUMN, data.EndDate);
        values.put(SQL_Constants.DATA_READINGDATA_COLUMN, data.DataString);  // TODO Change names around here. Data class/table should really be Reading. Data column should be DataString
        values.put(SQL_Constants.DATA_ISSYNCED_COLUMN, data.IsSynced ? 1 : 0);
        db.insertOrThrow(SQL_Constants.DATA_TABLE_NAME, null, values);

        return true;
    }

    /**
     * Marks a reading as synced, signifying this reading has already been sent to the cloud.
     * Pass 'id' param for reading to be synced.
     * @param id
     * @return
     */
    public void SyncReading(int id)  // TODO test
    {
        SQLiteDatabase db = _db.getWritableDatabase();
        String queryString = String.format(SQL_Constants.UPDATE_DATA_SYNC, 1, id);
        db.execSQL(queryString);
    }

    /**
     * Assigns an un-owned reading to a patient. Un-owned readings are created when a reading is
     * taken with no patient selected.
     * @param reading_id
     * @param patient_id
     * @return
     */
    public boolean AssignReadingToPatient(int reading_id, int patient_id) // tODO test
    {
        SQLiteDatabase db = _db.getWritableDatabase();
        String queryString = String.format(SQL_Constants.UPDATE_DATA_SYNC, patient_id, reading_id);
        db.execSQL(queryString);
        return true;
    }

}
