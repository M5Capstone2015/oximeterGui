package com.example.m5.oximetergui.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.m5.oximetergui.Helpers.ReadingCollector;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.Models.PatientModel;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.R;
import com.example.m5.oximetergui.Data_Objects.Reading;

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

    public void updateClick(View v)
    {
        Patient p = new Patient();
        p.IsOpen = true;
        p.DateOfBirth = "";
        p.Age = 15;
        p.ID = 1;
        p.FirstName = "Steve";
        p.LastName = "Joel";
        _model.UpdatePatient(p, null);
        UpdatePatients();
    }

    public void selectData(View v)
    {
        try {
            //List<Reading> readings = _dataModel.GetDataByPatientID(1);
            List<Reading> readings = _dataModel.GetAllData();
            String txt = "";
            for (Reading r : readings)
                txt += ("\n" + r.DataString);

            tv.setText(txt);
        }
        catch (Exception e)
        {
            String mes = e.getMessage();
            tv.setText(mes);
        }
    }

    public void addReading(View v) {
        try {
            Reading r = new Reading();
            r.DataString = "1,1,1,2,3,4,2,2,3";
            r.StartDate = "1-20-2001 12:22";  // TODO consider changing start/end date format to include seconds.
            r.EndDate = "1-20-2001 12:35";

            _dataModel.AddNewReading(r);
        }
        catch(Exception e)
        {
            String mes = e.getMessage();
            tv.setText(mes);
        }
    }

    /*
    public void SelectFirstPatientData(View v)
    {
        Reading data = _dataModel.getDataByID(1);
    }
    */

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
