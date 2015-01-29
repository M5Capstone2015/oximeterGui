package com.example.m5.oximetergui;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class PatientHistory extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("fullname");
            Log.d("Patient " +  name, name + " was receieved as the paitent's full name");
        }
    }
}
