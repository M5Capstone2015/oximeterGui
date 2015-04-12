package com.example.m5.oximetergui.Helpers;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hunt on 2/3/2015.
 */
public final class DateHelper {
    public static String GetCurrentDateTime()
    {
        Calendar c = Calendar.getInstance();
        //int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        Date date = new Date(year, month, day);
        date.setMinutes(minutes);
        date.setHours(hours);

        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm"); // TODO move this to constants

        return ft.format(date);
    }

    public static String GetBackupDate()
    {
        Calendar c = Calendar.getInstance();
        //int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        Date date = new Date(year, month, day);
        date.setMinutes(minutes);
        date.setHours(hours);

        SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmm"); // TODO move this to constants

        return ft.format(date);
    }

    public static String DateToString(Date d) // TODO implement
    {
        return "";
    }

    public static Date StringToDate(String str) // TODO implement
    {
        return null;
    }
}
