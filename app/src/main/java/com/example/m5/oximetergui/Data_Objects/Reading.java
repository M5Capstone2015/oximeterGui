package com.example.m5.oximetergui.Data_Objects;

import java.util.List;

/**
 * Created by Hunt on 2/2/2015.
 */
public class Reading {

    public Reading() {}

    public Reading(int id, String start, String end, String data)
    {
        ID = id;
        StartDate = start;
        EndDate = end;
        DataString = data;
    }

    public Reading(String start, String end, String data)
    {
        StartDate = start;
        EndDate = end;
        DataString = data;
    }

    public static int ID;
    public String StartDate = "";
    public String EndDate = "";
    public String DataString = "";
    public boolean IsSynced = false;

    public int[][] dataPoints;

    private void ParseDataString()
    {
        // split by , and trim white space
        // loop through each and add to dataPoints.
        // how we do this will be dependent on what graphing lib is used.
        dataPoints = new int[6][7];
    }
}
