package com.example.m5.oximetergui.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Helpers.ErrorMessage;
import com.example.m5.oximetergui.Helpers.PatientInfoHelper;
import com.example.m5.oximetergui.R;

import java.util.ArrayList;


public class NewPatient extends Activity implements View.OnClickListener {

    RelativeLayout mMainLayout = null;
    ArrayList<String> mPatientInfo = new ArrayList<String>();
    Patient mPatient = new Patient();
    PatientInfoHelper _helper = new PatientInfoHelper(this, this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);

        //Create listener for save button
        View patientListButton = findViewById(R.id.save);
        patientListButton.setOnClickListener(this);
    }


    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.save:
                mPatient = _helper.ConstructPatient();

                if (!mPatient.Validate())
                {
                    _helper.createErrorMessage();
                }

                //If contains first and last name, exit screen
                else
                {
                    mPatientInfo.add(mPatient.FirstName);
                    mPatientInfo.add(mPatient.LastName);
                    Intent i = new Intent();
                    i.putExtra(Intent_Constants.NewPatientInfo,mPatient);
                    setResult(RESULT_OK, i);
                    finish();
                    break;
                }
        }
    }


}
