package com.example.m5.oximetergui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class PatientList extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        View patientListButton = findViewById(R.id.new_patient);
        patientListButton.setOnClickListener(this);
    }

    static final int NEW_PATIENT_REQUEST = 0;

    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.new_patient:
                Intent i = new Intent(this, NewPatient.class);
                startActivityForResult(i,NEW_PATIENT_REQUEST);
                break;
        }
    }
}
