package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.m5.oximetergui.Activities.NewPatient;
import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.PatientAdapter;
import com.example.m5.oximetergui.Models.PatientModel;
import com.example.m5.oximetergui.R;

import java.util.ArrayList;
import java.util.List;

public class SliderFragment extends Fragment {

    private MainScreenFrag _mainScreenFrag;  // todo organize these.
    private MainActivity _mainActivity;
    private View newPatientButton;
    ArrayList<Patient> _patients = new ArrayList<Patient>();
    ListView _patientsList;
    PatientModel _model;
    PatientAdapter adapter;
    private Patient _currentPatient = null;

    private boolean _selectMode = false;

    public void SetSelectMode(boolean mode)
    {
        _selectMode = mode;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.patient_list, container, false);

        _mainScreenFrag = (MainScreenFrag) getFragmentManager().findFragmentById(R.id.fragment_secondpane);
        _mainActivity = (MainActivity) getActivity();

        _model = new PatientModel(_mainActivity);

        List<Patient> patients = _model.LoadPatientNames();
        for (Patient p : patients)
            _patients.add(p);

        InitializeViews(v);
        adapter = new PatientAdapter(getActivity().getBaseContext(), _patients);
        _patientsList.setAdapter(adapter);

        return v;
    }

    private void InitializeViews(View v)
    {
        Button b = (Button) v.findViewById(R.id.closeButton);
        b.setOnClickListener(listener);

        newPatientButton = v.findViewById(R.id.newPatientButton);
        newPatientButton.setOnClickListener(listener);

        _patientsList = (ListView) v.findViewById(R.id.patientList);
        _patientsList.setOnItemClickListener(itemListener);
    }

    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> list, View view, int position, long i) {

            if (!_mainActivity.IsOpen()) // For some reason this click event fires when the pane is closed for somereason.
                return;

            Patient selectedPatient = (Patient) list.getAdapter().getItem(position);

            Log.d("PatientList", "You clicked " + selectedPatient.FirstName + " " + selectedPatient.LastName + " at position " + position);

            if (_selectMode)
            {
                _currentPatient = selectedPatient;

                AlertDialog.Builder builder = new AlertDialog.Builder(_mainActivity);
                builder.setMessage("Save reading to " + selectedPatient.GetFullName() + "?").setPositiveButton("Yea Bruh", dialogClickListener)
                        .setNegativeButton("Naw Bruh", dialogClickListener); // .show();

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                return;
            }

            MainScreenFrag mainScreen = (MainScreenFrag) getFragmentManager().findFragmentById(R.id.fragment_secondpane);
            mainScreen.LogInPatient(selectedPatient);

        }
    };

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    Reading r = new Reading();  // save new reading.
                    //_dataModel.AddNewReading(11);

                    try {
                        SetSelectMode(false);
                        MainScreenFrag main = (MainScreenFrag) getFragmentManager().findFragmentById(R.id.fragment_secondpane);
                        main.LogInPatient(_currentPatient);
                        _mainActivity.ClosePane();
                        _mainActivity.EnablePane();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE: // Delete this reading.
                    break;
            }
        }
    };

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int viewID = v.getId();
            switch (viewID)
            {
                // This button is just for demo purposes.
                case R.id.closeButton:
                    _mainActivity.ClosePane();
                    break;
                case R.id.newPatientButton:
                    Intent i = new Intent(getActivity().getBaseContext(), NewPatient.class);
                    startActivityForResult(i, General_Constants.NEW_PATIENT_REQUEST);
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

            _patients.add(patientData); // TODO need to implement this for UPDATE as well.
            PatientAdapter pAdapter = new PatientAdapter(getActivity().getBaseContext(), _patients);
            _patientsList.setAdapter(pAdapter);

            StringBuilder sb = new StringBuilder();
            _model.AddPatient(patientData, sb);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_patient_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
