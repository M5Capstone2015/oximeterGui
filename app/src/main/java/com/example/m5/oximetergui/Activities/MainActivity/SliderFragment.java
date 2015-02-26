package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.m5.oximetergui.Activities.NewPatient;
import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.R;

import java.util.ArrayList;

public class SliderFragment extends Fragment {

    private MainScreenFrag _mainScreenFrag;
    private MainActivity _mainActivity;
    private View newPatientButton;
    ArrayList<String> _patientNames = new ArrayList<String>();
    ListView _patientsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void Login(Patient p)
    {
        _mainActivity.ClosePane();
        _mainScreenFrag.LogInPatient(p);
        // TODO update GUI
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.patient_list, container, false);
        InitializeViews(v);

        //_mainScreenFrag = Fragment.ge // todo init frag
        _mainActivity = (MainActivity) getActivity();

        return v;
    }

    private void InitializeViews(View v) {
        Button b = (Button) v.findViewById(R.id.closeButton);
        b.setOnClickListener(listener);
        newPatientButton = v.findViewById(R.id.newPatientButton);
        newPatientButton.setOnClickListener(listener);
        _patientsList = (ListView) v.findViewById(R.id.patientList);

    }
    // This is just for demo purposes.
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int viewID = v.getId();
            switch (viewID) {
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
        if(resultCode == Activity.RESULT_OK) {
            Patient patientData = data.getParcelableExtra(Intent_Constants.NewPatientInfo);
            Log.d("PatientListSlider", patientData.FirstName);
            Log.d("PatientListSlider", patientData.LastName);
            String fullName = patientData.FirstName + " " + patientData.LastName;
            Log.d("PatientListSlider", fullName);

            //TODO implement model and list
            _patientNames.add(fullName);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.row_layout, R.id.listText, _patientNames);
            _patientsList.setAdapter(adapter);

            /*StringBuilder sb = new StringBuilder();
            _model.AddPatient(patientData, sb);*/
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_patient_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
