package com.example.m5.oximetergui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hunt on 1/29/2015.
 */
public class PatientListModel {

    DB _db;

    public PatientListModel(Context context)
    {
        _db = new DB(context);
    }

    /**
     * Queries patient names for PatientList activity.
     *
     * @return
    */
    public List<String> LoadPatientNames() // TODO change this to return boolean and take a List<String> to populate
    {
        // TODO Consider just using SELECT * FROM Patients and populating Patient objects. Easier that way. Otherwise need to have ref to ID.
        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor cursor = db.query("Patients", SQL_Constants.FROM, null, null, null, null, ""); // TODO add ORDERBY param later.
        List<String> names = new ArrayList<>();
        while (cursor.moveToNext())
        {
            String firstName = cursor.getString(0);
            String lastName = cursor.getString(1);
            names.add(firstName + " " + lastName);
        }
        return names;
    }

    /**
     * Persists a new patient in the Patient table from a Patient object.
     *
     * @param patient
     * @param errorMessage
     * @return
     */
    public boolean AddPatient(Patient patient, StringBuilder errorMessage)
    {
        try {
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
}
