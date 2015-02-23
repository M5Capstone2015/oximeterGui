package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.m5.oximetergui.R;

/**
 * Created by Hunt on 2/23/2015.
 */
public class MainScreenFrag extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.main_screen, container, false);
        InitializeButtons(v);

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
