package com.example.m5.oximetergui.Data_Objects;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by Hunt on 2/2/2015.
 */
public class Reading {

    public int ID;
    public int PatientID;
    public String StartDate = "";
    public String EndDate = "";
    public String DataString = "";
    public boolean IsSynced = false;
    public boolean IsDrawn = false;

    public Reading() {}

    public Reading(int id, String start, String end, String data)
    {
        this.ID = id;
        this.StartDate = start;
        this.EndDate = end;
        this.DataString = data;
    }

    public Reading(int id, String start, String end, String data, int patientID)
    {
        this.ID = id;
        this.StartDate = start;
        this.EndDate = end;
        this.DataString = data;
        this.PatientID = patientID;
    }

    public Reading(String start, String end, String data)
    {
        this.StartDate = start;
        this.EndDate = end;
        this.DataString = data;
    }

    public List<PointValue> ConvertData()
    {
        String lines[] = this.DataString.split("\\r?\\n");
        List<PointValue> points = new ArrayList<>();

        try {
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].equals(""))
                    continue;
                float val = Float.parseFloat(lines[i]);
                points.add(new PointValue(i, val));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return points;
    }
}
