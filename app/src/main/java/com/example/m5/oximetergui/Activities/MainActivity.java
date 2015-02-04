package com.example.m5.oximetergui.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.DateHelper;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.NuJack.OnDataAvailableListener;
import com.example.m5.oximetergui.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView percentView; // TODO refactor this to its own class. SetRed() SetGreen SetPercent() methods.
    private boolean _recording = false;

    // AVERAGE STUFF //
    private int _averageCount = 0;
    private float _sum = 0.0f;  // TODO refactor all of this averaging into its own class
    private float _average = 0.0f;
    private String dataString = "";
    // AVERAGE STUFF //

    private Date _startTime;

    DataModel _dataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View patientListButton = findViewById(R.id.patient_list);
        patientListButton.setOnClickListener(this);

        percentView = (TextView) findViewById(R.id.percentView);
         _dataModel = new DataModel(this);
    }

    // TODO Should have conditional:  Is patient selected or not, display data either way. Use intent or startActivityForResult()
    // If  have_patient
    //      blow up xml for that put it on top
    // else
    //      blow up other xml

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

    private OnDataAvailableListener _listener = new OnDataAvailableListener() {

        @Override
        public void DataAvailable(int data) {

            _averageCount++;
            _sum += data;
            _average = _sum / (General_Constants.baudRate * General_Constants.RollingAverageLength);

            if (_averageCount == General_Constants.baudRate * General_Constants.RollingAverageLength)
            {
                //_dataModel.AddNewReading(new Reading(DateHelper.DateToString(_startTime), DateHelper.GetCurrentDateTime(), ));
            }

            String newPercent = "";
            if (data < 10) // If new percent single digit, prepend a space to so it's centered.
                newPercent = " ";
            newPercent += (dataString + "%%");

            percentView.setText(newPercent);
        }
    };
}
