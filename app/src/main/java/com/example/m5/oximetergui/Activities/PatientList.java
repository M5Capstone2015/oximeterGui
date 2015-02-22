package com.example.m5.oximetergui.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.Models.PatientModel;
import com.example.m5.oximetergui.R;

import java.util.ArrayList;
import java.util.List;


public class PatientList extends ListActivity implements View.OnClickListener {

    ArrayList<String> mPatientNames = new ArrayList<String>();
    PatientModel _model = new PatientModel(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        View patientListButton = findViewById(R.id.new_patient);
        patientListButton.setOnClickListener(this);
        List<Patient> patients = _model.LoadPatientNames();
        for (Patient p : patients)
            mPatientNames.add(p.FirstName + " " + p.LastName);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.listText, mPatientNames);
        setListAdapter(adapter);
    }


    public void onClick(View v) {
        int buttonId = v.getId();

        //Switch statement for static buttons that can be clicked
        switch (buttonId)
        {
            case R.id.new_patient:
                Intent i = new Intent(this, NewPatient.class);
                startActivityForResult(i, General_Constants.NEW_PATIENT_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Patient patientData = data.getParcelableExtra(Intent_Constants.NewPatientInfo);
            Log.d("PatientList", patientData.FirstName);
            Log.d("PatientList", patientData.LastName);
            String fullName = patientData.FirstName + " " + patientData.LastName;
            Log.d("PatientList", fullName);
            mPatientNames.add(fullName);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.listText, mPatientNames);
            setListAdapter(adapter);

            StringBuilder sb = new StringBuilder();
            _model.AddPatient(patientData, sb);
        }
    }

    // when an item of the list is clicked
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        String selectedItem = (String) getListAdapter().getItem(position);

        Log.d("PatientList", "You clicked " + selectedItem + " at position " + position);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(Intent_Constants.NameToPatient, selectedItem);
        setResult(RESULT_OK, i);
        finish();
    }
}


