package com.example.m5.oximetergui.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.MainHelper;
import com.example.m5.oximetergui.Helpers.ReadingCollector;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.NuJack.NuJack;
import com.example.m5.oximetergui.NuJack.OnDataAvailableListener;
import com.example.m5.oximetergui.R;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView percentView; // TODO refactor this to its own class. SetRed() SetGreen SetPercent() methods.
    private String mPatientName = null;

    private boolean _recording = false;

    ReadingCollector _collector;
    //NuJack _nuJack;
    DataModel _dataModel;
    MainHelper _mainHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _mainHelper = new MainHelper(this, this);
        _collector = new ReadingCollector();
        _dataModel = new DataModel(this);
        //_nuJack = new NuJack(_listener);
        //_nuJack.Start();


        _mainHelper.ConstructMainLayout(R.layout.activity_main);
        percentView = (TextView) findViewById(R.id.percentView);
    }

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
        //if (_nuJack != null)
        //    _nuJack.Stop();
    }

    public void sqlClick(View v)
    {
        Intent i = new Intent(this, SQL_Sandbox.class);
        startActivity(i);
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.patient_list:
                Intent i1 = new Intent(this, PatientList.class);
                startActivityForResult(i1, General_Constants.PATIENT_REQUEST);
                break;

            case R.id.save_reading:
                if (mPatientName!=null) {
                    Intent i2 = new Intent();
                    setResult(RESULT_OK, i2);
                    finish();
                }

        }
    }

    public void startReadingClick(View v)
    {
        _collector.Restart();

        _recording = true;

        /*
        if (_patientSelected)
        {
            // Render name
        }
        else
        {
            // Render something else (maybe nothing)
        }
        */
    }

    public void stopReadingClick(View v)
    {
        _recording = false;

        Reading newReading = _collector.GetReading();
        _dataModel.AddNewReading(newReading);

        /*
        if (_patientSelected)
        {
        }
        else
        {
        }
        */
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Patient p = new Patient();
            p = data.getParcelableExtra(Intent_Constants.NameToPatient);
            _mainHelper.LoadPatient(p);
        }
    }
}
