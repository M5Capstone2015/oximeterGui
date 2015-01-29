package com.example.m5.oximetergui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;


public class PatientList extends Activity implements View.OnClickListener {

    RelativeLayout mMainLayout = null;
    ArrayList<String> mPatientNames = new ArrayList<String>();
    ArrayList<Integer> mPatientButtons = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        View patientListButton = findViewById(R.id.new_patient);
        patientListButton.setOnClickListener(this);
        //TODO LOAD PATIENTS INTO mPatientNames
        this.CreateTextList(this, mPatientNames, R.id.new_patient);

    }

    static final int NEW_PATIENT_REQUEST = 1;

    public void onClick(View v) {
        int buttonId = v.getId();

        switch (buttonId)
        {
            case R.id.new_patient:
                Intent i = new Intent(this, NewPatient.class);
                startActivityForResult(i,NEW_PATIENT_REQUEST);
                break;
        }

        for(int i = 0; i < mPatientButtons.size(); i++)
        {
            if(buttonId==mPatientButtons.get(i))
            {
                Log.d("PatientList","ID pressed " + mPatientButtons.get(i).toString() + " associates to " + mPatientNames.get(i));
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
            //If empty patient list append first name below new patient button
            int prevId;
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
            mPatientButtons.add(prevId+1);

            //TODO SAVE INFORMATON INTO SQL
        }
    }

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

