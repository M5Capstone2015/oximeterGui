package com.example.m5.oximetergui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PatientList extends Activity implements View.OnClickListener {

    RelativeLayout mMainLayout = null;
    ArrayList<String> mPatientNames = new ArrayList<String>();
    ArrayList<Integer> mPatientButtons = new ArrayList<Integer>();
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

        List<Patient> patientNames = _model.LoadPatientNames();

        String names = "";
        for (Patient s : patientNames)
            names += (s.FirstName + " " + s.LastName + "\n");
        tv.setText(names);
    }

    public void findByIDClick(View v)
    {
        Patient p = _model.FindPatientByID(1);

        tv.setText("Found: " + p.FirstName);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        View patientListButton = findViewById(R.id.new_patient);
        tv = (TextView) findViewById(R.id.textView);
        patientListButton.setOnClickListener(this);
        //TODO LOAD PATIENTS INTO mPatientNames
        this.CreateTextList(this, mPatientNames, R.id.new_patient);

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

        //Iterate through dynamic patient buttons to see if any of them were clicked
        for(int count = 0; count < mPatientButtons.size(); count++)
        {
            if(buttonId == mPatientButtons.get(count))
            {
                Log.d("PatientList","ID pressed " + mPatientButtons.get(count).toString() + " associates to " + mPatientNames.get(count));
                Intent i = new Intent(this, PatientHistory.class);
                i.putExtra("fullname", mPatientNames.get(count));
                startActivity(i);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            ArrayList<String> patientData = data.getStringArrayListExtra("PatientInfo");
            Log.d("PatientList", patientData.get(0));
            Log.d("PatientList", patientData.get(1));
            String fullName = patientData.get(0) + " " + patientData.get(1);
            Log.d("PatientList", fullName);
            mPatientNames.add(fullName);

            int prevId;
            //If empty patient list append first name below new patient button
            if (mPatientNames.size()==1) {
                prevId = R.id.new_patient;
                this.AppendButtonList(this, fullName, prevId);
            }

            //Otherwise put it below the last patient in the list
            else
            {
                prevId = R.id.new_patient + mPatientNames.size()-1;
                this.AppendButtonList(this, fullName, prevId);
            }

            //New patient UI ID will be the ID above it plus 1
            mPatientButtons.add(prevId+1);

            //TODO SAVE INFORMATION INTO SQL
        }
    }

    //TODO MAKE CreateTextList CALL APPENDBUTTONLIST
    public void CreateTextList (Context context, ArrayList<String> list, int id) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativePatientList);

        int prevTextViewId = 0;
        for (int i = 0; i < list.size(); i++) {
            final TextView textView = new TextView(context);

            textView.setText(list.get(i));

            int curTextViewId = prevTextViewId + 1;
            textView.setId(curTextViewId);
            final RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

            if (i == 0 && id != -1) {
                params.addRule(RelativeLayout.BELOW, id);
            } else {
                params.addRule(RelativeLayout.BELOW, prevTextViewId);
            }
            textView.setLayoutParams(params);

            prevTextViewId = curTextViewId;
            layout.addView(textView, params);
        }
    }

    public void AppendButtonList (Context context, String text, int id) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativePatientList);
        final Button button = new Button(context);

        button.setText(text);

        int curTextViewId = id + 1;
        button.setId(curTextViewId);
        button.setOnClickListener(this);
        final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, id);
        button.setLayoutParams(params);

        layout.addView(button, params);
    }
}

