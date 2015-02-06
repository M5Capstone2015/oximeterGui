package com.example.m5.oximetergui.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.DateHelper;
import com.example.m5.oximetergui.Helpers.ReadingCollector;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.NuJack.OnDataAvailableListener;
import com.example.m5.oximetergui.R;

import java.util.Date;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView percentView; // TODO refactor this to its own class. SetRed() SetGreen SetPercent() methods.
    private boolean _recording = false;
    private boolean _patientSelected = false;
    private String mPatientName=null;

    ReadingCollector _collector = new ReadingCollector();

    private Date _startTime;
    DataModel _dataModel;

    // TODO Should have conditional:  Is patient selected or not, display data either way. Use intent or startActivityForResult()
    // If  have_patient
    //      blow up xml for that put it on top
    // else
    //      blow up other xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            //Blow up patient name
            mPatientName = extras.getString(Intent_Constants.NameToPatient);
            TextView nameTitle = (TextView)findViewById(R.id.patient_name);
            nameTitle.setText(mPatientName);
        }

        View patientListButton = findViewById(R.id.patient_list);
        patientListButton.setOnClickListener(this);
        View saveButton = findViewById(R.id.save_reading);
        saveButton.setOnClickListener(this);

        percentView = (TextView) findViewById(R.id.percentView);
         _dataModel = new DataModel(this);
    }

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
                Intent i1 = new Intent(this, PatientList.class);
                startActivity(i1);
                break;

            case R.id.save_reading:
                if (mPatientName!=null) {
                    Intent i2 = new Intent();
                    setResult(RESULT_OK, i2);
                    finish();
                }

        }
    }


    public void startReadingClick(View v)
    {

        if (_patientSelected)
        {
            // Render name
        }
        else
        {
            // Render something else (maybe nothing)
        }

        _collector.Reset();

        _startTime = DateHelper.StringToDate(DateHelper.GetCurrentDateTime()); // TODO this is actually disgusting... same as below

        _recording = true;
    }

    public void stopReadingClick(View v)
    {
        _recording = false;

        // Save to model
        Reading newReading = new Reading(DateHelper.DateToString(_startTime), // TODO refactor this into ReadingCollector class
                                         DateHelper.GetCurrentDateTime(),
                                         _collector.GetData()
        );
        _dataModel.AddNewReading(newReading);

        if (_patientSelected)
        {
        }
        else
        {

        }
    }

    private OnDataAvailableListener _listener = new OnDataAvailableListener() {

        @Override
        public void DataAvailable(int data)
        {
            if (!_recording)
                return;

            _collector.AddNewData(data);
            percentView.setText(data < 10 ? " " + data : String.valueOf(data));
        }
    };
}
