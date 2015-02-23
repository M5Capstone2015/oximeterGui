package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.MainHelper;
import com.example.m5.oximetergui.Helpers.ReadingCollector;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.R;

/**
 * Created by Hunt on 2/23/2015.
 */
public class MainScreenFrag extends Fragment {

    private Patient _currentPatient = null;
    private MainScreenFrag _mainScreenFrag;
    private MainActivity _mainActivity;

    ReadingCollector _collector = null;
    //NuJack _nuJack = null;
    private DataModel _dataModel = null;
    private MainHelper _mainHelper = null;
    private boolean _recording = false;

    ViewGroup _container;

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

    private void StartRecording()
    {
        _recording = true;
        // TODO update GUI
    }

    private void StopRecording()
    {
        _recording = false;

        Reading newReading = _collector.GetReading();
        //_dataModel.AddNewReading(newReading); //Commented because currently crashing shit
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
        View v = inflater.inflate(R.layout.main_screen, container, false);
        InitializeButtons(v);

        View startButton = inflater.inflate(R.layout.start_button, null);

        /* ----- DAN, This is what I meant. ------- */
        _container = (ViewGroup) v.findViewById(R.id.mainLayoutContainer);
        _container.addView(startButton);
        /* ---------------------------------------- */

        return v;
    }

    private void InitializeButtons(View v)
    {
        Button b = (Button) v.findViewById(R.id.patient_list);
        b.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MainActivity) getActivity())._OpenPane();
        }
    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_patient_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
