package com.example.m5.oximetergui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;


public class PatientList extends ListActivity {

    RelativeLayout mMainLayout = null;
    ArrayList<String> mPatientNames = new ArrayList<String>();
    ArrayList<Integer> mPatientButtons = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        View patientListButton = findViewById(R.id.new_patient);
        patientListButton.setOnClickListener(this);
        //TODO LOAD PATIENTS INTO mPatientNames
        this.CreateTextList(this, mPatientNames, R.id.new_patient);

    }

    static final int NEW_PATIENT_REQUEST = 1;

    public void onClick(View v) {
        int buttonId = v.getId();

        //Switch statement for static buttons that can be clicked
        switch (buttonId)
        {
            case R.id.new_patient:
                Intent i = new Intent(this, NewPatient.class);
                startActivityForResult(i,NEW_PATIENT_REQUEST);
                break;
        }

        //Iterate through dynamic patient buttons to see if any of them were clicked
        for(int count = 0; count < mPatientButtons.size(); count++)
        {
            if(buttonId==mPatientButtons.get(count))
            {
                Log.d("PatientList","ID pressed " + mPatientButtons.get(count).toString() + " associates to " + mPatientNames.get(count));
                Intent i = new Intent(this, PatientHistory.class);
                i.putExtra("fullname", mPatientNames.get(count));
                startActivity(i);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            ArrayList<String> patientData = data.getStringArrayListExtra("PatientInfo");
            Log.d("PatientList", patientData.get(0));
            Log.d("PatientList", patientData.get(1));
            String fullName = patientData.get(0) + " " + patientData.get(1);
            Log.d("PatientList", fullName);
            mPatientNames.add(fullName);

            int prevId;
            //If empty patient list append first name below new patient button
            if (mPatientNames.size()==1) {
                prevId = R.id.new_patient;
                this.AppendButtonList(this, fullName, prevId);
            }

            //Otherwise put it below the last patient in the list
            else
            {
                prevId = R.id.new_patient + mPatientNames.size()-1;
                this.AppendButtonList(this, fullName, prevId);
            }

            //New patient UI ID will be the ID above it plus 1
            mPatientButtons.add(prevId+1);

            //TODO SAVE INFORMATION INTO SQL
        }
    }
}


