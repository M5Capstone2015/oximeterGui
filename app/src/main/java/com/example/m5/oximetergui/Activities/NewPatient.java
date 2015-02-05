package com.example.m5.oximetergui.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.R;

import java.util.ArrayList;


public class NewPatient extends Activity implements View.OnClickListener {

    RelativeLayout mMainLayout = null;
    TextView mErrorMessage = null;
    ArrayList<String> mPatientInfo = new ArrayList<String>();
    Patient mPatient = new Patient();


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
                EditText firstName = (EditText)findViewById(R.id.firstName);
                EditText lastName = (EditText)findViewById(R.id.lastName);
                mPatient.FirstName = firstName.getText().toString();
                mPatient.LastName = lastName.getText().toString();

                if (!mPatient.Validate())
                {
                    this.createErrorMessage();
                }

                //If contains first and last name, exit screen
                else
                {
                    mPatientInfo.add(mPatient.FirstName);
                    mPatientInfo.add(mPatient.LastName);
                    // TODO Move to Intent Constants final class
                    Intent i = new Intent();
                    i.putStringArrayListExtra("PatientInfo",mPatientInfo);
                    setResult(RESULT_OK, i);
                    finish();
                    break;
                }
        }
    }

    // TODO refactor this to its own class.  ErrorMessage em = new ErrorMessage(errorText, context);
    private void createErrorMessage()
    {
        //If this is the first time we've stored the main relative layout, retrieve the layout
        //via its ID
        if (mMainLayout == null) {
            mMainLayout = (RelativeLayout) findViewById(R.id.relativeNewPatient);
        }

        //If we have yet to display an error message, display message
        if (mErrorMessage == null) {
            mErrorMessage = (TextView) getLayoutInflater().inflate(R.layout.textview_error_message, null);
            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.save);
            mErrorMessage.setLayoutParams(params);
            mMainLayout.addView(mErrorMessage);
        }
    }
}
