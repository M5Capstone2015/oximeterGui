package com.example.m5.oximetergui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class SQL_Sandbox extends ActionBarActivity {

    TextView tv = null;
    PatientModel _model = new PatientModel(this);
    DataModel _dataModel = new DataModel(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql__sandbox);
        tv = (TextView) findViewById(R.id.textView);
    }

    public void patientsClick(View v)
    {
        List<Patient> patients = _model.LoadPatientNames();
        String names = "";
        for (Patient p : patients)
            names += (p.FirstName + " " + p.LastName + "\n");

        tv.setText(names);
    }

    private void UpdatePatients()
    {
        List<Patient> patients = _model.LoadPatientNames();
        String names = "";
        for (Patient p : patients)
            names += (p.FirstName + " " + p.LastName + "\n");

        tv.setText(names);
    }

    public void addPatient(View v)
    {
        Patient p = new Patient();
        p.FirstName = "Bob";
        p.LastName = "Dull";

        StringBuilder sb = new StringBuilder();
        _model.AddPatient(p, sb);

        UpdatePatients();
    }

    public void SelectFirstPatientData(View v)
    {
        List<Data> data = _dataModel.getDataByID(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sql__sandbox, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
