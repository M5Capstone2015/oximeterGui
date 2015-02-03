package com.example.m5.oximetergui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


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


}
