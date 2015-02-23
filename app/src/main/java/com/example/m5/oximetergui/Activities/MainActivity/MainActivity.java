package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.widget.TextView;

import com.example.m5.oximetergui.Activities.NewPatient;
import com.example.m5.oximetergui.Activities.PatientList;
import com.example.m5.oximetergui.Activities.SQL_Sandbox;
import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.MainHelper;
import com.example.m5.oximetergui.Helpers.ReadingCollector;
import com.example.m5.oximetergui.Helpers.Slider;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.NuJack.OnDataAvailableListener;
import com.example.m5.oximetergui.R;


public class MainActivity extends Activity {

    private TextView percentView; // TODO refactor this to its own class. SetRed() SetGreen SetPercent() methods.
    private String mPatientName = null;

    private boolean _recording = false;

    ReadingCollector _collector;
    //NuJack _nuJack;
    DataModel _dataModel;
    MainHelper _mainHelper;
    Patient _patient = null;

    Slider pane;

    public void ClosePane()
    {
        pane.closePane();
    }

    public void _OpenPane()
    {
        pane.openPane();
    }

    public void DisablePane()
    {
        pane.DisableSlide();
    }

    public void EnablePane()
    {
        pane.SetSlidable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pane = (Slider) findViewById(R.id.slidingpanelayout);

        pane.setPanelSlideListener(new SlidingPaneLayout.SimplePanelSlideListener()
        {

            @Override
            public void onPanelClosed(View panel) {
                // TODO Auto-generated method stub
                switch (panel.getId()) {
                    case R.id.fragment_secondpane:
                        getFragmentManager().findFragmentById(R.id.fragment_firstpane).setHasOptionsMenu(false);
                        getFragmentManager().findFragmentById(R.id.fragment_secondpane).setHasOptionsMenu(true);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPanelOpened(View panel) {
                // TODO Auto-generated method stub
                switch (panel.getId()) {
                    case R.id.fragment_secondpane:
                        getFragmentManager().findFragmentById(R.id.fragment_firstpane).setHasOptionsMenu(true);
                        getFragmentManager().findFragmentById(R.id.fragment_secondpane).setHasOptionsMenu(false);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // TODO Auto-generated method stub
            }

        });
    }


    /*
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //_mainHelper = new MainHelper(this, this);
        //_collector = new ReadingCollector();
        //_dataModel = new DataModel(this);
        //_nuJack = new NuJack(_listener);
        //_nuJack.Start();


        //_mainHelper.ConstructMainLayout(R.layout.activity_main);
        //percentView = (TextView) findViewById(R.id.percentView);
    }
    */

    @Override
    public void onResume()  // TODO load percent/patient selected/and rolling average data
    {
        super.onResume();
        //if (_nuJack != null)
           // _nuJack.Start();
    }

    @Override
    public void onPause() // TODO save percent/patient selected/and rolling average data
    {
        super.onPause();
        //if (_nuJack != null) // TODO need to create public interface on frags to save shit.
        //    _nuJack.Stop();
    }

    public void startReadingClick()
    {
        _collector.Restart();
        _recording = true;
        //_mainHelper.StartRecording(_patient);
    }

    public void stopReadingClick()
    {
        _recording = false;

        Reading newReading = _collector.GetReading();
        //_dataModel.AddNewReading(newReading); //Commented because currently crashing shit
        //_mainHelper.StopRecording(_patient);
    }

    /*
    private OnDataAvailableListener _listener = new OnDataAvailableListener() {

        @Override
        public void DataAvailable(String _data)
        {
            if (!_recording)
                return;

            int data = Integer.parseInt(_data);
            _collector.AddNewData(data);
            percentView.setText(data < 10 ? " " + data : String.valueOf(data));
        }
    };
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            _patient = data.getParcelableExtra(Intent_Constants.NamePatient);
            _mainHelper.LoadPatient(_patient, false);
        }
    }
}
