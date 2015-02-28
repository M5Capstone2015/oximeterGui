package com.example.m5.oximetergui.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.R;

public class PatientInfo extends ActionBarActivity {

    TextView _name;
    TextView _age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        _name = (TextView) findViewById(R.id.name);
        _age = (TextView) findViewById(R.id.agetextview);

        Patient patientData = getIntent().getParcelableExtra(Intent_Constants.Patient_To_Edit);
        _name.setText(patientData.FirstName + " " + patientData.LastName);
        _age.setText("Age: " + patientData.DateOfBirth);
        // populate views based on this.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    public void saveClick(View v)
    {
        //i.putExtra(Intent_Constants.NewPatientInfo, mPatient);
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
