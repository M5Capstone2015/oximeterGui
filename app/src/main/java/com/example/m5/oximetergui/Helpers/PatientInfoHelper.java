package com.example.m5.oximetergui.Helpers;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.R;

/**
 * Created by danabeled on 2/22/2015.
 */
public class PatientInfoHelper {

    Activity _main;
    View.OnClickListener _v;

    public PatientInfoHelper(Activity main, View.OnClickListener v) {
        _main = main;
        _v = v;
    }

    public void createErrorMessage()
    {
        RelativeLayout mMainLayout = (RelativeLayout) _main.findViewById(R.id.relativeNewPatient);

        //If we have yet to display an error message, display message
        TextView oldMessage = (TextView)_main.findViewById(R.id.error_message);
        if (oldMessage == null) {
            ErrorMessage em = new ErrorMessage("Please Fill out all required fields", _main);
            TextView message = em.CreateErrorBelow(R.id.save);
            mMainLayout.addView(message);
        }
    }

    public Patient ConstructPatient()
    {
        EditText firstName = (EditText)_main.findViewById(R.id.firstName);
        EditText lastName = (EditText)_main.findViewById(R.id.lastName);
        Patient p = new Patient();
        p.FirstName = firstName.getText().toString();
        p.LastName = lastName.getText().toString();
        return p;
    }


}