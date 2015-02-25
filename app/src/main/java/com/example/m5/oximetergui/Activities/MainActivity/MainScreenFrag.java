package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.ReadingCollector;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.NuJack.NuJack;
import com.example.m5.oximetergui.R;

/**
 * Created by Hunt on 2/23/2015.
 */
public class MainScreenFrag extends Fragment {

    private MainScreenFrag _mainScreenFrag;
    private MainActivity _mainActivity;

    ReadingCollector _collector = null;  // TODO wrap all this (minus NuJack) in an object for easy serialization.
    NuJack _nuJack = null; // TODO disable horizontal screen orientation
    private DataModel _dataModel = null;
    private boolean _recording = false;
    private Patient _currentPatient = null;

    private View startButton;
    private View stopButton;
    private View selectPatientsButton;
    private View infoButton;
    private TextView percent;

    public void LogInPatient(Patient p)
    {
        _currentPatient = p;
        // TODO update GUI here
    }

    private void LogOut()
    {
        _currentPatient = null;
        // TOOD udpate GUI here
    }

    public void StartRecording()
    {
        _recording = true;

        startButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.VISIBLE);
    }

    private void StopRecording()
    {
        _recording = false;

        stopButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);

        //Reading newReading = _collector.GetReading();
        //_dataModel.AddNewReading(newReading); // TODO fix. Commented because currently crashing shit
        //_mainHelper.StopRecording(_patient);

        // TODO update GUI
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    private OnClickListener startButtonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (_mainScreenFrag == null)
                return;

            int viewID = v.getId();
            switch (viewID) {
                case R.id.patient_list:
                    _mainActivity._OpenPane();
                    break;
                case R.id.start_reading:
                    _mainScreenFrag.StartRecording();
                    break;
                case R.id.stop_reading_main:
                    _mainScreenFrag.StopRecording();
                    break;
            }
        }
    };
}
