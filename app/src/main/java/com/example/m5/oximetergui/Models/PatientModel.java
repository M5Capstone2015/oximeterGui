package com.example.m5.oximetergui.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Constants.SQL_Constants;
import com.example.m5.oximetergui.Helpers.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PatientModel {

    DAL _dal;

    public PatientModel(Context context)
    {
        _dal = new DAL(context);
    }


    /**
     * Queries patient names for PatientList activity.
     *
     * @return
    */
    public List<Patient> LoadPatientNames()
    {
        SQLiteDatabase db = _dal.getReadableDatabase();
        Cursor cursor = db.query(SQL_Constants.PATIENT_TABLE_NAME,
                                 SQL_Constants.FROM, null, null, null, null, ""); // TODO add ORDERBY param later.

        List<Patient> patients = new ArrayList<>();  // TODO consider only having search and recents section?

        while (cursor.moveToNext())
        {
            int ID = cursor.getInt(0);  // TODO fill out rest of paramters
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            String dob = cursor.getString(3);
            Boolean isOpen = cursor.getInt(4) < 1 ? false : true;
            Patient p = new Patient(ID, firstName, lastName, dob, isOpen);
            patients.add(p);
        }
        return patients;
    }

    /**
     * @param patient
     * @param errorMessage
     * @return
     */
    public boolean AddPatient(Patient patient, StringBuilder errorMessage)
    {
        try
        {
            SQLiteDatabase db = _dal.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SQL_Constants.PATIENT_FIRSTNAME_COLUMN, patient.FirstName);
            values.put(SQL_Constants.PATIENT_LASTNAME_COLUMN, patient.LastName);
            values.put(SQL_Constants.PATIENT_DOB_COLUMN, patient.DateOfBirth);
            values.put(SQL_Constants.PATIENT_ISOPEN_COLUMN, patient.IsOpen ? 1 : 0);
            db.insertOrThrow(SQL_Constants.PATIENT_TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            errorMessage.append(e.getMessage()); // Can't pass Strings by reference in Java so have to use this. Java sucks.
            return false;
        }
        return true;
    }

    /**
     * Retrieve patient from DB by its ID.
     * @param id
     * @return
     */
    public Patient FindPatientByID(int id)
    {
        SQLiteDatabase db = _dal.getReadableDatabase();

        String queryString = String.format(SQL_Constants.SELECT_PATIENT_BY_ID, String.valueOf(id));
        Cursor cursor2 = db.rawQuery(queryString, null);

        Patient patient = null;

        while (cursor2.moveToNext())
        {
            String firstName = cursor2.getString(1);
            String lastName = cursor2.getString(2);
            patient = new Patient(id, firstName, lastName);
        }

        return patient;
    }


    /**
     * Returns first 0-10 patients thats' first or last name contain the search string.
     * @param searchString
     * @return
     */
    public List<Patient> SearchPatients(String searchString)  // TODO test all fields.
    {
        SQLiteDatabase db = _dal.getReadableDatabase();  // TODO Consider how to handle the patients AFTER the first 0 to 10. 'Load More' button to get next? Also consider handling a space in the middle and searching first and last name. AND clause instead of or.

        Cursor cursor = null;
        try
        {
            String queryString = String.format(SQL_Constants.SEARCH_PATIENT_BY_NAME, searchString, searchString, searchString);
            cursor = db.rawQuery(queryString, null);
        }
        catch (Exception e)
        {
            String s = e.getMessage();
        }

        List<Patient> patients = new ArrayList<>();  // TODO consider only having search and recents section?

        while (cursor.moveToNext())
        {
            int ID = cursor.getInt(0);  // TODO fill in other parameters.
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            patients.add(new Patient(ID, firstName, lastName));
        }

        return patients;
    }


    /**
     * Update all fields in Patient table row. Will update the row with corresponding to
     * the ID field passed in by the Patient object.
     * @param patient
     * @param sb
     * @return
     */
    public boolean UpdatePatient(Patient patient, StringBuilder sb)
    {
        try
        {
            SQLiteDatabase db = _dal.getWritableDatabase();

            String updateString = String.format(SQL_Constants.UPDATE_PATIENT,
                                                patient.FirstName,
                                                patient.LastName,
                                                patient.DateOfBirth,
                                                patient.IsOpen ? 1 : 0,
                                                patient.ID
            );

            db.execSQL(updateString);
        }
        catch (Exception e)
        {
            sb.append(e.getMessage());
            return false;
        }
        return true;
    }
}
