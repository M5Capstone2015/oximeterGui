package com.example.m5.oximetergui;

/**
 * Created by Hunt on 1/29/2015.
 */
public class SQL_Constants {

    public static String PATIENT_TABLE_NAME = "Patients";

    public static String PATIENT_FIRSTNAME_COLUMN = "first_name";
    public static String PATIENT_LASTNAME_COLUMN = "last_name";
    public static String PATIENT_ID_COLUMN = "id";

    public static String CREATE_DB_SQL_STRING = // TODO Set each column name as a variable.
            "CREATE TABLE Patients(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "first_name TEXT," +
                    "last_name TEXT," +
                    "age INT," +
                    "date_of_birth TEXT," +
                    "is_open INT" +
            ");";

    public static String[] FROM = new String[] {
            PATIENT_ID_COLUMN,
            PATIENT_FIRSTNAME_COLUMN,
            PATIENT_LASTNAME_COLUMN
    };

    public static String SELECT_PATIENT_BY_ID = "SELECT * FROM Patients WHERE id=%s LIMIT 1";

    public static String UPGRADE = "DROP TABLE IF EXISTS " + PATIENT_TABLE_NAME;
}
