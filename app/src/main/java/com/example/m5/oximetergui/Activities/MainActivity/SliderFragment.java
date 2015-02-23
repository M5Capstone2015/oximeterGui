package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.MainHelper;
import com.example.m5.oximetergui.Helpers.ReadingCollector;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.R;

public class SliderFragment extends Fragment {

    private MainScreenFrag _mainScreenFrag;
    private MainActivity _mainActivity;

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

        Button b = (Button) v.findViewById(R.id.closeButton);
        b.setOnClickListener(listener);

        //_mainScreenFrag = Fragment.ge // todo init frag
        _mainActivity = (MainActivity) getActivity();

        return v;
    }

    // This is just for demo purposes.
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _mainActivity.ClosePane();
        }
    };



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_patient_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
