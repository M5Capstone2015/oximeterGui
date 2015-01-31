package com.example.m5.oximetergui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

public class NewReading extends Activity implements View.OnClickListener {

    ArrayList<String> mDateList;
    int mSp02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reading);

        //Setup OnClickListeners
        View patientListButton = findViewById(R.id.record_button);
        patientListButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int buttonId = v.getId();
        switch (buttonId)
        {
            case R.id.record_button:
                mDateList = getDateTime();
                mSp02 = getSp02();
                Intent i = new Intent();
                i.putStringArrayListExtra("Date", mDateList);
                i.putExtra("Sp02",mSp02);
                setResult(RESULT_OK,i);
                finish();
                break;
        }
    }

    protected ArrayList<String> getDateTime()
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
        Log.d("NewReading","Seconds: " + seconds + System.getProperty("line.separator") +
                "Minutes: " + minutes + System.getProperty("line.separator") +
                "Hours: " + hours + System.getProperty("line.separator") +
                "Day: " + day + System.getProperty("line.separator") +
                "Month: " + month + System.getProperty("line.separator") +
                "Year: " + year + System.getProperty("line.separator"));
        date.add(String.valueOf(seconds));
        date.add(String.valueOf(month));
        date.add(String.valueOf(day));
        date.add(String.valueOf(hours));
        date.add(String.valueOf(minutes));
        date.add(String.valueOf(seconds));

        return date;
    }

    //TODO ACTUALLY IMPLEMENT, FOR NOW JUST RETURNS 92
    protected int getSp02()
    {
        return 92;
    }
}
