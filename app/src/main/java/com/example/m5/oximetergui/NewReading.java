package com.example.m5.oximetergui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.util.Calendar;

public class NewReading extends Activity implements View.OnClickListener {

    private int seconds;
    private int minutes;
    private int hours;
    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reading);

        //Setup OnClickListeners
        View patientListButton = findViewById(R.id.record_button);
        patientListButton.setOnClickListener(this);
    }

    protected void getDateTime()
    {
        Calendar c = Calendar.getInstance();
        //TODO SAVE INTO SQL DATABASE
        seconds = c.get(Calendar.SECOND);
        minutes = c.get(Calendar.MINUTE);
        hours = c.get(Calendar.HOUR_OF_DAY);
        day = c.get(Calendar.DATE);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        Log.d("NewReading","Seconds: " + seconds + System.getProperty("line.separator") +
                "Minutes: " + minutes + System.getProperty("line.separator") +
                "Hours: " + hours + System.getProperty("line.separator") +
                "Day: " + day + System.getProperty("line.separator") +
                "Month: " + month + System.getProperty("line.separator") +
                "Year: " + year + System.getProperty("line.separator"));
    }

    @Override
    public void onClick(View v) {
        int buttonId = v.getId();
        switch (buttonId)
        {
            case R.id.record_button:
                getDateTime();
        }
    }
}
