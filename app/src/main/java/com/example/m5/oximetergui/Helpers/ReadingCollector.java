package com.example.m5.oximetergui.Helpers;

import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Data_Objects.Reading;

/**
 * Created by Hunt on 2/3/2015.
 */
public class ReadingCollector {

    private int _averageCount = 0;
    private float _sum = 0.0f;  // TODO refactor all of this averaging into its own class
    private float _average = 0.0f;
    private String dataString = "";

    public void AddNewData(int newData)
    {
        _sum +=
        _averageCount++;
        _sum += newData;

        _average = _sum / (General_Constants.baudRate * General_Constants.RollingAverageLength);

        if (_averageCount == General_Constants.baudRate * General_Constants.RollingAverageLength) {
            dataString += ("\n" + newData);
            Reset();
        }

    }

    public String GetData()
    {
        return dataString;
    }

    public void Reset()
    {
        _averageCount = 0;
        _sum = 0.0f;
        _average = 0.0f;
        dataString = "";
    }
}
