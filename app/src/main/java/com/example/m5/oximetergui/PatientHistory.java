package com.example.m5.oximetergui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class PatientHistory extends ListActivity implements View.OnClickListener {

    String mPatientName;
    ArrayList<String> mReadingDates = new ArrayList<String>();

    // TODO move to constants. Also refactor this to pass Patient object through intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mPatientName = extras.getString("fullname");
            Log.d("Patient " + mPatientName, mPatientName + " was receieved as the patient's full name");
            TextView nameTitle = (TextView)findViewById(R.id.historyNameTitle);
            nameTitle.setText(mPatientName);
        }
        else
        {
            //Shouldn't be able to get here, patient should always have a name
            //TODO add error handling?
        }

        //Setup OnClickListeners
        View patientListButton = findViewById(R.id.new_reading);
        patientListButton.setOnClickListener(this);

    }

    static final int NEW_READING_REQUEST = 1;

    @Override
    public void onClick(View v) {
        int buttonId = v.getId();


        switch (buttonId)
        {
            case R.id.new_reading:
                Intent i = new Intent(this, NewReading.class);
                startActivityForResult(i, NEW_READING_REQUEST);
                break;
        }
    }


    @Override
    // TODO add object in intent
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK) {
            Log.d("PatientHistory","Result code was: RESULT OK");
            ArrayList<String> dateInfo = data.getStringArrayListExtra("Date");
            String formattedDate = dateInfo.get(1) + "/" + dateInfo.get(2) + "/" + dateInfo.get(0) + " at " + dateInfo.get(3) + ":" + dateInfo.get(4) + ":" +  dateInfo.get(5);
            mReadingDates.add(formattedDate);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.listText, mReadingDates);
            setListAdapter(adapter);
        }
    }
}
