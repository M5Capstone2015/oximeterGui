package com.example.m5.oximetergui;

/**
 * Created by Hunt on 2/2/2015.
 */
public class Data {

    public Data() {}

    public Data(int id, String start, String end, String data)
    {
        ID = id;
        StartDate = start;
        EndDate = end;
        DataString = data;
    }

    public int ID;
    public String StartDate = "";
    public String EndDate = "";
    public String DataString = "";
    public boolean IsSynced = false;

}
