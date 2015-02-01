package com.example.m5.oximetergui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class PatientModel {

    DB _db;

    public PatientModel(Context context)
    {
        _db = new DB(context);
    }


    /**
     * Queries patient names for PatientList activity.
     *
     * @return
    */
    public List<Patient> LoadPatientNames() // TODO change this to return boolean and take a List<String> to populate
    {
        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor cursor = db.query(SQL_Constants.PATIENT_TABLE_NAME,
                                 SQL_Constants.FROM, null, null, null, null, ""); // TODO add ORDERBY param later.

        List<Patient> patients = new ArrayList<>();

        while (cursor.moveToNext())
        {
            int ID = cursor.getInt(0);
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            patients.add(new Patient(ID, firstName, lastName));
        }
        return patients;
    }

    /**
     *
     * Persists a new patient in the Patient table from a Patient object.
     *
     * @param patient
     * @param errorMessage
     * @return
     */
    public boolean AddPatient(Patient patient, StringBuilder errorMessage)
    {
        try
        {
            SQLiteDatabase db = _db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SQL_Constants.PATIENT_FIRSTNAME_COLUMN, patient.FirstName);
            values.put(SQL_Constants.PATIENT_LASTNAME_COLUMN, patient.LastName);
            db.insertOrThrow(SQL_Constants.PATIENT_TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            if (errorMessage == null)
                errorMessage = new StringBuilder();
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
        SQLiteDatabase db = _db.getReadableDatabase();

        String queryString = String.format(SQL_Constants.SELECT_PATIENT_BY_ID, String.valueOf(id));
        Cursor cursor2 = db.rawQuery(queryString, null);

        Patient patient = null;

        while (cursor2.moveToNext()) {
            String firstName = cursor2.getString(1);
            String lastName = cursor2.getString(2);
            patient = new Patient(id, firstName, lastName);
        }

        return patient;
    }

    public boolean UpdatePatient(Patient patient, StringBuilder sb) // TODO fix this, some SQL syntax problem
    {
        try
        {
            SQLiteDatabase db = _db.getReadableDatabase();

            String updateString = String.format(SQL_Constants.UPDATE_PATIENT,
                                                patient.FirstName,
                                                patient.LastName,
                                                String.valueOf(patient.Age),
                                                patient.DateOfBirth,
                                                String.valueOf(patient.IsOpen),
                                                String.valueOf(patient.ID)
            );

            db.rawQuery(updateString, null);
        }
        catch (Exception e)
        {
            sb.append(e.getMessage());
            return false;
        }
        return true;
    }
}
