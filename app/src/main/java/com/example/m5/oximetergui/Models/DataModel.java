package com.example.m5.oximetergui.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Constants.SQL_Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hunt on 2/2/2015.
 */
public class DataModel {

    DAL _dal;

    public DataModel(Context context)
    {
        _dal = new DAL(context);
    }

    /**
     * Fetches a reading by ID. Takes Reading ID as parameter.
     * @param id
     * @return
     */
    public Reading getDataByID(int id) // TODO test
    {
        SQLiteDatabase db = _dal.getReadableDatabase();
        String queryString = String.format(SQL_Constants.SELECT_DATA_BY_ID, id);
        Cursor cursor = db.rawQuery(queryString, null);

        Reading data = null;
        while (cursor.moveToNext())
        {
            int ID = cursor.getInt(0);
            String startTime = cursor.getString(1);
            String endTime = cursor.getString(2);
            String readingData = cursor.getString(3);
            data = new Reading(ID, startTime, endTime, readingData);
        }

        return data;
    }

    /**
     * Fetches all readings corresponding to a patient. Takes targets patient's ID
     * as parameter.
     * @param id
     * @return
     */
    public List<Reading> GetDataByPatientID(int id)
    {
        SQLiteDatabase db = _dal.getReadableDatabase();
        String queryString = String.format(SQL_Constants.SELECT_DATA_BY_PATIENT, id);
        Cursor cursor = db.rawQuery(queryString, null);

        List<Reading> readings = new ArrayList<>();
        while (cursor.moveToNext())
        {
            int ID = cursor.getInt(0);
            String startTime = cursor.getString(1);
            String endTime = cursor.getString(2);
            int patientID = cursor.getInt(3);
            String dataString = cursor.getString(4);
            readings.add(new Reading(ID, startTime, endTime, dataString));
        }

        return readings;
    }

    public List<Reading> GetAllData()
    {
        SQLiteDatabase db = _dal.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_Constants.SELECT_ALL_READINGS_, null);

        List<Reading> readings = new ArrayList<>();
        while (cursor.moveToNext())
        {
            int ID = cursor.getInt(0);
            String startTime = cursor.getString(1);
            String endTime = cursor.getString(2);
            int patient_id = cursor.getInt(3);
            String dataString = cursor.getString(4);
            Boolean synced = cursor.getInt(5) > 0 ? true : false;
            readings.add(new Reading(ID, startTime, endTime, dataString));
        }

        return readings;
    }

    /**
     * Creates a new reading.
     * @param data
     * @return
     */
    public boolean AddNewReading(Reading data)  // TODO test
    {

        SQLiteDatabase db = _dal.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(SQL_Constants.DATA_STARTDATE_COLUMN, data.StartDate);
        //values.put(SQL_Constants.DATA_ENDDATE_COLUMN, data.EndDate);
        values.put(SQL_Constants.DATA_READINGDATA_COLUMN, data.DataString);
        values.put(SQL_Constants.DATA_ISSYNCED_COLUMN, data.IsSynced ? 1 : 0);
        values.put("patient_id", data.PatientID);
        values.put("end_date", data.EndDate);
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
        SQLiteDatabase db = _dal.getWritableDatabase();
        String sqlString = String.format(SQL_Constants.UPDATE_DATA_SYNC, 1, id);
        db.execSQL(sqlString);
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
        SQLiteDatabase db = _dal.getWritableDatabase();
        String queryString = String.format(SQL_Constants.UPDATE_DATA_SYNC, patient_id, reading_id);
        db.execSQL(queryString);
        return true;
    }

}
