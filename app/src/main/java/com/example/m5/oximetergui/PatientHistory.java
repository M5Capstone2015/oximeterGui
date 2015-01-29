package com.example.m5.oximetergui;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class PatientHistory extends Activity {

    String patientName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patientName = extras.getString("fullname");
            Log.d("Patient " +  patientName, patientName + " was receieved as the patient's full name");
            TextView nameTitle = (TextView)findViewById(R.id.historyNameTitle);
            nameTitle.setText(patientName);
        }
    }
}
