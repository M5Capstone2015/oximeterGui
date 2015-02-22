package com.example.m5.oximetergui.Helpers;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.R;

/**
 * Created by danabeled on 2/22/2015.
 * Used to build onClickListeners, removing redudent code on state redraws
 */

public class MainHelper {

    Activity _main;
    View.OnClickListener _v;

    public MainHelper(Activity main, View.OnClickListener v) {
        _main = main;
        _v = v;
    }


    public void ConstructMainLayout(int layout) {
        _main.setContentView(layout);
        View patientListButton = _main.findViewById(R.id.patient_list);
        patientListButton.setOnClickListener(_v);
    }

    public void LoadPatient(Patient p)
    {
        View v = _main.findViewById (R.id.main);
        v.invalidate();
        this.ConstructMainLayout(R.layout.activity_main_patient_selected);
        TextView nameTitle = (TextView)_main.findViewById(R.id.patient_name);
        nameTitle.setText(p.FirstName + " " + p.LastName);
        View name = _main.findViewById(R.id.patient_name);
        name.setOnClickListener(_v);
    }


}
