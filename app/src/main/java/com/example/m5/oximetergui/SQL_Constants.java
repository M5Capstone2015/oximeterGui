package com.example.m5.oximetergui;

/**
 * Created by Hunt on 1/29/2015.
 */
public class SQL_Constants {

    // Table Names
    public static String PATIENT_TABLE_NAME = "Patients";
    public static String DATA_TABLE_NAME = "Patients";

    // Patient column names
    public static String PATIENT_FIRSTNAME_COLUMN = "first_name"; // todo maybe get read of these. Overly verbose for a small DB
    public static String PATIENT_LASTNAME_COLUMN = "last_name";
    public static String PATIENT_ID_COLUMN = "id";

    // Data column names
    public static String DATA_STARTDATE_COLUMN = "start_date";
    public static String DATA_ENDDATE_COLUMN = "end_date";
    public static String DATA_READINGDATA_COLUMN = "is_synced";
    public static String DATA_ISSYNCED_COLUMN = "is_synced";

    // Create table statements
    public static String CREATE_DB_SQL_STRING = // TODO Set each column name as a variable.
            "CREATE TABLE Patients(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "first_name TEXT," +
                    "last_name TEXT," +
                    "age INT," +
                    "date_of_birth TEXT," +
                    "is_open INT" +
            ");" +
            "CREATE TABLE Data(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "start_date DATETIME," +
                    "end_date TEXT," +
                    "patient_id INT" +
                    "data_string INT" +
                    "is_synced INT" +
            ");";

    // From clause for select query
    public static String[] FROM = new String[] {
            PATIENT_ID_COLUMN,
            PATIENT_FIRSTNAME_COLUMN,
            PATIENT_LASTNAME_COLUMN
    };

    public static String SELECT_PATIENT_BY_ID = "SELECT * FROM Patients WHERE id=%s LIMIT 1";
    public static String SELECT_DATA_BY_ID = "SELECT * FROM Data WHERE id=%s LIMIT 1";

    public static String UPDATE_PATIENT = "UPDATE Patients" +
            " SET first_name = '%s'," +
            " last_name = '%s'," +
            " age = %s," +
            " date_of_birth = '%s'," +
            " is_open = %s" +
            " WHERE id = %s;";

    public static String UPDATE_DATA_SYNC = "UPDATE Data " +
                                            "SET is_synced = %s, " +
                                            "WHERE id = %s;";

    public static String UPDATE_DATA_OWNERSHIP = "UPDATE Data " +
                                                 "SET patient_id = %s, " +
                                                 "WHERE id = %s;";

    public static String UPGRADE = "DROP TABLE IF EXISTS " + PATIENT_TABLE_NAME + ";" +
                                   " DROP TABLE IF EXISTS " + DATA_TABLE_NAME + ";";
}
