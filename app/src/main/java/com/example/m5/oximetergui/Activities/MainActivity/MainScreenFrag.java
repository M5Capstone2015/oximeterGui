package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m5.oximetergui.Activities.PatientInfo;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.ReadingCollector;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.NuJack.NuJack;
import com.example.m5.oximetergui.NuJack.OnDataAvailableListener;
import com.example.m5.oximetergui.R;

/**
 * Created by Hunt on 2/23/2015.
 */
public class MainScreenFrag extends Fragment {

    // --- Activities and Fragments --- //
    private MainScreenFrag _mainScreenFrag;
    private MainActivity _mainActivity;

    // --- Helpers/Model --- //
    ReadingCollector _collector = null;  // TODO wrap all this (minus NuJack) in an object for easy serialization.
    NuJack _nuJack = null; // TODO disable horizontal screen orientation
    private DataModel _dataModel = null;

    // --- View state Objects --- //
    private boolean _recording = false;
    private Patient _currentPatient = null;

    // --- Views --- //
    private View startButton;
    private View stopButton;
    private View selectPatientsButton;
    private View logOutButton;
    private TextView infoTextView;
    private TextView percent;

    public void LogInPatient(Patient p)
    {
        if (p == null)
            return;

        _currentPatient = p;

        SetViewInvisible(selectPatientsButton);

        infoTextView.setText(p.FirstName + " " + p.LastName);
        SetViewVisible(infoTextView);
        SetViewVisible(logOutButton);

        _mainActivity.ClosePane();
    }

    private void PatientListClick()
    {
        if (!_recording)
            _mainActivity.OpenPane();
    }

    private void SetViewVisible(View v)
    {
        v.setVisibility(View.VISIBLE);
    }

    private void SetViewInvisible(View v)
    {
        v.setVisibility(View.INVISIBLE);
    }

    private void LogOut()
    {
        if (_recording) // todo maybe just make this invisible.
            return;

        _currentPatient = null;

        SetViewInvisible(infoTextView);
        SetViewVisible(selectPatientsButton);
        SetViewInvisible(logOutButton);
        // TOOD udpate GUI here
    }

    public void StartRecording()
    {
        _recording = true;

        startButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.VISIBLE);

        _mainActivity.DisablePane();
    }

    private void PatientInfo()
    {
        if (_recording) // todo just make button invisible instead.
            return;

        Intent i = new Intent(_mainActivity, PatientInfo.class);
        i.putExtra(Intent_Constants.Patient_To_Edit, _currentPatient);
        startActivityForResult(i, 1);  // We actually care about the req code, just user was deleted or not.
    }

    private void StopRecording()
    {
        _recording = false;

        stopButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);

        ShowDialog();

        _mainActivity.EnablePane();
    }

    private void ShowDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mainActivity);
        builder.setMessage("You want to save this here data?").setPositiveButton("Yea Bruh", dialogClickListener)
                .setNegativeButton("Naw Bruh", dialogClickListener); //.show();

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    if (_currentPatient == null) {
                        // set slider to select mode (button clicks will trigger a dialog confirming they want to save to this patient)
                        SliderFragment sliderFrag = (SliderFragment) getFragmentManager().findFragmentById(R.id.fragment_firstpane);
                        sliderFrag.SetSelectMode(true);
                        _mainActivity.OpenPane();
                        _mainActivity.DisablePane();
                    }
                    else
                    {
                        try
                        {
                            //Reading newReading = _collector.GetReading();
                            //_dataModel.AddNewReading(newReading); // TODO fix. Commented because currently crashing shit
                            Reading r = new Reading();
                            _dataModel.AddNewReading(r);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    break;

                case DialogInterface.BUTTON_NEGATIVE: // Delete this reading.
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
        {
            Patient patientData = data.getParcelableExtra(Intent_Constants.NewPatientInfo);

            Log.d("PatientListSlider", patientData.FirstName);
            Log.d("PatientListSlider", patientData.LastName);

            //_patients.add(patientData); // TODO need to implement this for UPDATE as well.
            //PatientAdapter pAdapter = new PatientAdapter(getActivity().getBaseContext(), _patients);
            //_patientsList.setAdapter(pAdapter);

            //StringBuilder sb = new StringBuilder();
            //_model.AddPatient(patientData, sb);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            _nuJack = new NuJack(_dataAvailableListner);
            _nuJack.Start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        _mainScreenFrag = this;
        _mainActivity = (MainActivity) getActivity();
        View v = inflater.inflate(R.layout.main_screen, container, false);
        InitializeButtons(v);
        return v;
    }

    private void InitializeButtons(View v)
    {
        selectPatientsButton = v.findViewById(R.id.patient_list);
        selectPatientsButton.setOnClickListener(startButtonListener);

        startButton = v.findViewById(R.id.start_reading);
        startButton.setOnClickListener(startButtonListener);

        stopButton = v.findViewById(R.id.stop_reading_main);
        stopButton.setOnClickListener(startButtonListener);

        logOutButton = v.findViewById(R.id.log_out_button);
        logOutButton.setOnClickListener(startButtonListener);

        infoTextView = (TextView) v.findViewById(R.id.patient_name);
        infoTextView.setOnClickListener(startButtonListener);

        percent = (TextView) v.findViewById(R.id.percentView);
    }


    OnDataAvailableListener _dataAvailableListner = new OnDataAvailableListener() {
        @Override
        public void DataAvailable(final String data) {
            // do gui things here.

            if (_recording)
            {
                // log stuff
            }

            try
            {
                _mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        percent.setText(data + "%");
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };


    private OnClickListener startButtonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (_mainScreenFrag == null)
                return;

            int viewID = v.getId();
            switch (viewID) {
                case R.id.patient_list:
                    _mainScreenFrag.PatientListClick();
                    break;
                case R.id.start_reading:
                    _mainScreenFrag.StartRecording();
                    break;
                case R.id.stop_reading_main:
                    _mainScreenFrag.StopRecording();
                    break;
                case R.id.patient_name:
                    _mainScreenFrag.PatientInfo();
                    break;
                case R.id.log_out_button:
                    _mainScreenFrag.LogOut();
                    break;
            }
        }
    };
}
