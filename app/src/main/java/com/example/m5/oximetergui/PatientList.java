package com.example.m5.oximetergui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PatientList extends ListActivity implements View.OnClickListener {

    RelativeLayout mMainLayout = null;
    ArrayList<String> mPatientNames = new ArrayList<String>();
    List<Integer> mPatientButtons = new ArrayList<Integer>();
    TextView tv;
    PatientModel _model = new PatientModel(this);


    public void addClick(View v)
    {
        Patient p1 = new Patient();
        p1.FirstName = "Hunt";
        p1.LastName = "Graham";

        StringBuilder sb = new StringBuilder();
        if (!_model.AddPatient(p1, sb)) {
            String errorMessage = sb.toString();// TODO Do something with error
            return;
        }

        DisplayPatients();
    }

    private void DisplayPatients()
    {
        List<Patient> patientNames = _model.LoadPatientNames();

        String names = "";
        for (Patient s : patientNames)
            names += (s.FirstName + " " + s.LastName + "\n");
        tv.setText(names);
    }

    public void findByIDClick(View v)
    {
        //Patient p = _model.FindPatientByID(1);

        //tv.setText("Found: " + p.FirstName);

        Patient p = new Patient();
        p.FirstName = "John";
        p.LastName = "Steve";
        p.Age = 15;
        p.ID = 5;
        p.DateOfBirth = "February";
        p.IsOpen = false;

        StringBuilder sb = new StringBuilder();

        if(!_model.UpdatePatient(p, sb))
        {
            String errorMessage = sb.toString();
            tv.setText(errorMessage);
            return;
        }

        DisplayPatients();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        View patientListButton = findViewById(R.id.new_patient);
        tv = (TextView) findViewById(R.id.textView);
        patientListButton.setOnClickListener(this);
        //TODO LOAD PATIENTS INTO mPatientNames AND SETUP LIST ADAPTER

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            //TODO pass patient not string list
            ArrayList<String> patientData = data.getStringArrayListExtra("PatientInfo");
            Log.d("PatientList", patientData.get(0));
            Log.d("PatientList", patientData.get(1));
            String fullName = patientData.get(0) + " " + patientData.get(1);
            Log.d("PatientList", fullName);
            mPatientNames.add(fullName);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.listText, mPatientNames);
            setListAdapter(adapter);

            //TODO SAVE INFORMATION INTO SQL
        }
    }

    // when an item of the list is clicked
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        String selectedItem = (String) getListAdapter().getItem(position);

        Log.d("PatientList", "You clicked " + selectedItem + " at position " + position);
        Intent i = new Intent(this, PatientHistory.class);
        i.putExtra("fullname", selectedItem);
        startActivity(i);
    }
}


