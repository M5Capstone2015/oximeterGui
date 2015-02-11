package com.example.m5.oximetergui.Helpers;

import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Data_Objects.Reading;

/**
 * Created by Hunt on 2/3/2015.
 */
public class ReadingCollector {

    private int _averageCount = 0;
    private float _sum = 0.0f;
    private float _average = 0.0f;
    private String dataString = "";
    private String startDate = "";

    public void AddNewData(int newData)
    {
        _averageCount++;
        _sum += newData;

        _average = _sum / (General_Constants.baudRate * General_Constants.RollingAverageLength);

        if (_averageCount == General_Constants.baudRate * General_Constants.RollingAverageLength) {
            dataString += ("\n" + _average);
            ResetAverages();
        }

    }

    public String GetData()
    {
       return dataString;
    }

    public Reading GetReading()
    {
        Reading reading = new Reading();
        reading.StartDate = this.startDate;
        reading.EndDate = DateHelper.GetCurrentDateTime();
        reading.DataString = this.dataString;
        reading.IsSynced = false;

        return reading;
    }

    private void ResetAverages()
    {
        _averageCount = 0;
        _sum = 0.0f;
        _average = 0.0f;
    }

    public void Restart()
    {
        ResetAverages();
        dataString = "";
        startDate = DateHelper.GetCurrentDateTime();
    }
}
