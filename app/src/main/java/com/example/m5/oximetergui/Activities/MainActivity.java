package com.example.m5.oximetergui.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.m5.oximetergui.NuJack.OnDataAvailableListener;
import com.example.m5.oximetergui.R;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View patientListButton = findViewById(R.id.patient_list);
        patientListButton.setOnClickListener(this);
    }

    // TODO New patient vs New Reading:  Refactor to one class.  Should have conditional:  Is patient selected or not, display data either way.

    // If  have_patient
    //      blow up xml for that put it on top
    // else
    //      blow up other xml

    // TODO think about how to handle non-selected readings.

    // TODO figure out format for data in Model. We know:  10 sec rolling average

    public void sqlClick(View v)
    {
        Intent i = new Intent(this, SQL_Sandbox.class);
        startActivity(i);
    }



    // TODO refactor then import NuJack libs.



    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.patient_list:
                Intent i = new Intent(this, PatientList.class);
                startActivity(i);
                break;
        }
    }

    protected ArrayList<String> getDateTime()  // TODO refactor this to use Date object, not ArrayList of strings.
    {
        ArrayList<String> date = new ArrayList<String>();

        int seconds;
        int minutes;
        int hours;
        int day;
        int month;
        int year;

        Calendar c = Calendar.getInstance();
        //TODO SAVE INTO SQL DATABASE
        seconds = c.get(Calendar.SECOND);
        minutes = c.get(Calendar.MINUTE);
        hours = c.get(Calendar.HOUR_OF_DAY);
        day = c.get(Calendar.DATE);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        Log.d("NewReading", "Seconds: " + seconds + System.getProperty("line.separator") +
                "Minutes: " + minutes + System.getProperty("line.separator") +
                "Hours: " + hours + System.getProperty("line.separator") +
                "Day: " + day + System.getProperty("line.separator") +
                "Month: " + month + System.getProperty("line.separator") +
                "Year: " + year + System.getProperty("line.separator"));
        date.add(String.valueOf(year));
        date.add(String.valueOf(month));
        date.add(String.valueOf(day));
        date.add(String.valueOf(hours));
        date.add(String.valueOf(minutes));
        date.add(String.valueOf(seconds));

        return date;
    }

    private OnDataAvailableListener _listener = new OnDataAvailableListener() {
        @Override
        public void DataAvailable(int data) {
            // Update average
            // Update percent
            // if low
                // set to red
            // else
                // set to green
        }
    };

}
