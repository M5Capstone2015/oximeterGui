package com.example.m5.oximetergui.Helpers;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
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

    public void ConstructMainLayout()
    {
        _main.setContentView(R.layout.activity_new_patient);
        View savebutton = _main.findViewById(R.id.save);
        savebutton.setOnClickListener(_v);
        View cameraButton = _main.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(_v);
    }

    public void createErrorMessage()
    {
        RelativeLayout mMainLayout = (RelativeLayout) _main.findViewById(R.id.relativeNewPatient);

        //If we have yet to display an error message, display message
        TextView message = (TextView)_main.findViewById(R.id.errorTxt);
        if (message.getText().equals("")) {
            message.setText("Please Fill out all required fields");
        }
    }

    public Patient ConstructPatient()
    {
        EditText firstName = (EditText) _main.findViewById(R.id.firstName);
        EditText lastName = (EditText) _main.findViewById(R.id.lastName);
        EditText age = (EditText) _main.findViewById(R.id.agetextview);

        Patient p = new Patient();
        p.FirstName = firstName.getText().toString();
        p.LastName = lastName.getText().toString();
        p.DateOfBirth = age.getText().toString();
        return p;
    }


}
