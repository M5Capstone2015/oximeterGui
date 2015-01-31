package com.example.m5.oximetergui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class PatientHistory extends Activity implements View.OnClickListener {

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
        else
        {
            //Shouldn't be able to get here, patient should always have a name
            //TODO add error handling?
        }

        //Setup OnClickListeners
        View patientListButton = findViewById(R.id.new_reading);
        patientListButton.setOnClickListener(this);

    }

    static final int NEW_READING_REQUEST = 1;

    @Override
    public void onClick(View v) {
        int buttonId = v.getId();


        switch (buttonId)
        {
            case R.id.new_reading:
                //Intent i = new Intent(this, NewReading.class);
                //startActivityForResult(i,NEW_READING_REQUEST);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK) {
            Log.d("PatientHistory","Result code was: RESULT OK");
        }
    }
}
