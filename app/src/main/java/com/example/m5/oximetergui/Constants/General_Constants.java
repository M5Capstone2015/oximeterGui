package com.example.m5.oximetergui.Constants;

/**
 * Created by Hunt on 2/3/2015.
 */
public final class General_Constants {
    public static int baudRate = 3; // Reads/second
    public static int RollingAverageLength = 10;  // # of seconds in rolling average

    public static String AskUserToSaveData = "Save this reading?";
    public static String ConfirmSaveToPatient = "Save data to patient %s?";
    public static String Yes = "Yes";
    public static String No = "No";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final int MODIFY_PATIENT = 1;

    public static final int NEW_PATIENT_REQUEST = 1;
    public static final int NEW_READING_REQUEST = 2;

    public static final int PATIENT_REQUEST = 3;

    public static int DecoderRollingAvgLength = 5;
}
