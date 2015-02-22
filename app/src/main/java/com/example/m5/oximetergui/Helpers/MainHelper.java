package com.example.m5.oximetergui.Helpers;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.m5.oximetergui.R;

/**
 * Created by danabeled on 2/22/2015.
 */
public class MainHelper{

    Activity _main;
    View.OnClickListener _v;

    public MainHelper(Activity main, View.OnClickListener v)
    {
        _main = main;
        _v = v;
    }


    public void ConstructMainLayout()
    {
        View patientListButton = _main.findViewById(R.id.patient_list);
        patientListButton.setOnClickListener(_v);
        View saveButton = _main.findViewById(R.id.save_reading);
        saveButton.setOnClickListener(_v);
    }


}
