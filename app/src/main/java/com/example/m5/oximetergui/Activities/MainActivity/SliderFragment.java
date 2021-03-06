package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.m5.oximetergui.Activities.NewPatient;
import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.PatientAdapter;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.Models.PatientModel;
import com.example.m5.oximetergui.R;

import java.util.ArrayList;
import java.util.List;

public class SliderFragment extends Fragment {

    // --- Activities and Fragments --- //
    private MainScreenFrag _mainScreenFrag;  // todo organize these.
    private MainActivity _mainActivity;

    // --- Views --- //
    private View newPatientButton;
    ListView _patientsList;
    //EditText _searchBar;

    // --- Adapter and Data --- //
    ArrayList<Patient> _patients = new ArrayList<>();
    PatientAdapter adapter;

    // --- Models --- //
    DataModel _dataModel;
    PatientModel _model;

    // --- State --- //
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
        _dataModel = new DataModel(_mainActivity);

        List<Patient> patients = _model.LoadPatientNames();
        for (Patient p : patients)
            _patients.add(p);

        InitializeViews(v);

        adapter = new PatientAdapter(getActivity().getBaseContext(), _patients);
        _patientsList.setAdapter(adapter);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
        {
            Patient patientData = null;
            try {
                patientData = data.getParcelableExtra(Intent_Constants.NewPatientInfo);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            Log.d("PatientListSlider", patientData.FirstName);
            Log.d("PatientListSlider", patientData.LastName);

            _patients.add(patientData); // TODO need to implement this for UPDATE as well.
            PatientAdapter pAdapter = new PatientAdapter(getActivity().getBaseContext(), _patients);
            _patientsList.setAdapter(pAdapter);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_patient_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    // --- Patient ListItem Click --- //
    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> list, View view, int position, long i) {

            if (!_mainActivity.IsOpen()) // For some reason this click event fires when the pane is closed
                return;

            Patient selectedPatient = (Patient) list.getAdapter().getItem(position);  // Get patient from intent

            Log.d("PatientList", "You clicked " + selectedPatient.FirstName + " " + selectedPatient.LastName + " at position " + position);

            if (_selectMode)  // if we are selecting this patient to save data
            {
                ShowConfirmDialog(selectedPatient);
                return;
            }

            MainScreenFrag mainScreen = (MainScreenFrag) getFragmentManager().findFragmentById(R.id.fragment_secondpane);
            mainScreen.LogInPatient(selectedPatient);

        }
    };

    // --- PopUp Dialog Click Event --- //
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    Reading r = new Reading();  // save new reading.
                    //_dataModel.AddNewReading(11);

                    try
                    {
                        SetSelectMode(false);
                        MainScreenFrag main = (MainScreenFrag) getFragmentManager().findFragmentById(R.id.fragment_secondpane);
                        main.LogInPatient(_currentPatient);
                        main.SaveReading();
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

    // --- Close/NewPatient Click Events --- //
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int viewID = v.getId();
            switch (viewID)
            {
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

    /*TextWatcher searchTextChanged = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start,
        int count, int after) {
        }

        private void loadPatients()
        {
            List<Patient> patients = _model.LoadPatientNames();
            _patients.clear();
            for (Patient p : patients)
                _patients.add(p);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
        int before, int count) {

            String search = s.toString();
            if(s.length() == 0 || search == "") {
                this.loadPatients();
                return;
            }

            List<Patient> searchREsults = _model.SearchPatients(search);

            _patients.clear();
            for (Patient p : searchREsults)
                _patients.add(p);
            adapter.notifyDataSetChanged();
        }
    };*/


    private void InitializeViews(View v)
    {
        Button b = (Button) v.findViewById(R.id.closeButton);
        b.setOnClickListener(listener);

        newPatientButton = v.findViewById(R.id.newPatientButton);
        newPatientButton.setOnClickListener(listener);

        _patientsList = (ListView) v.findViewById(R.id.patientList);
        _patientsList.setOnItemClickListener(itemListener);

        /*_searchBar = (EditText) v.findViewById(R.id.search_bar);
        _searchBar.addTextChangedListener(this.searchTextChanged);
        _searchBar.setFocusable(false);
        //_searchBar.setSelected(false);*/
    }

    /*public void EnbleSearchBar(boolean setting)
    {
        //this._searchBar.setEnabled(setting);
        this._searchBar.setFocusable(true);
    }*/

    private void ShowConfirmDialog(Patient selectedPatient)
    {
        _currentPatient = selectedPatient;

        AlertDialog.Builder builder = new AlertDialog.Builder(_mainActivity); // todo move this to seperate function

        builder.setMessage(String.format(General_Constants.ConfirmSaveToPatient, selectedPatient.GetFullName()))
                .setPositiveButton(General_Constants.Yes, dialogClickListener)
                .setNegativeButton(General_Constants.No, dialogClickListener);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
