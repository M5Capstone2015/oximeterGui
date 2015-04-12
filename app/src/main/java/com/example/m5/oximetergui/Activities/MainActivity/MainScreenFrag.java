package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m5.oximetergui.Activities.PatientInfo;
import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.DataSync;
import com.example.m5.oximetergui.Helpers.ReadingCollector;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.NuJack.NuJack;
import com.example.m5.oximetergui.NuJack.OnDataAvailableListener;
import com.example.m5.oximetergui.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.Preferences;

/**
 * Created by Hunt on 2/23/2015.
 */
public class MainScreenFrag extends Fragment {

    // --- API Objects --- ///
    private SharedPreferences prefs;
    private MainScreenFrag _mainScreenFrag;
    private MainActivity _mainActivity;

    // --- Helpers/Model --- //
    private ReadingCollector _collector = null;  // TODO wrap all this (minus NuJack) in an object for easy serialization.
    private NuJack _nuJack = null;
    private DataModel _dataModel = null;
    private DataSync _dataSync = null;

    // --- View state Objects --- //
    private boolean _recording = false;
    private Patient _currentPatient = null;

    // --- Views --- //
    private View startButton;
    private View stopButton;
    private View selectPatientsButton;
    private View logOutButton;
    private TextView infoTextView;
    private TextView percent;

    // --- Async Workers --- //
    private Handler mHandler = new Handler();
    private RequestTask requestTask = new RequestTask();


    public void StartSync()
    {
        final ProgressBar pb = (ProgressBar) _mainActivity.findViewById(R.id.progressBar);
        final LinearLayout syncCont = (LinearLayout) _mainActivity.findViewById(R.id.syncContainer);

        syncCont.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);

        if (this._dataSync != null)
            this._dataSync.Run("fake data here!");

        //requestTask.execute("");

        for (int i = 0; i < 101; i++)
        {
            pb.setProgress(i);
            new Thread(new Runnable() {
                public void run()
                {
                    int mProgressStatus = 0;
                    while (mProgressStatus < 100)
                    {
                        mProgressStatus++;

                        try {
                            Thread.sleep(100);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        final int s = mProgressStatus;

                        mHandler.post(new Runnable() {
                            public void run() {
                                pb.setProgress(s);
                                //pb.incrementProgressBy(1);
                                //pb.setProgress(pb.getProgress() * 100);
                            }
                        });

                    }

                    mHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "Sync successful!", Toast.LENGTH_SHORT).show();
                            syncCont.setVisibility(View.GONE);
                        }
                    });
                }
            }).start();



        }
    }

    public void LogInPatient(Patient p)
    {
        if (p == null)
            return;

        _currentPatient = p;

        SetViewInvisible(selectPatientsButton);

        String name = p.FirstName + " " + p.LastName + ":";
        Spanned formated = Html.fromHtml("<u>" + name + "</u>");
        infoTextView.setText(formated);

        SetViewVisible(infoTextView);
        SetViewVisible(logOutButton);

        _mainActivity.AddLoginButton();

        _mainActivity.ClosePane();
    }

    private void PatientListClick()
    {
        if (!_recording)
            _mainActivity.OpenPane();
    }

    private void SetViewVisible(View v)
    {
        v.setVisibility(View.VISIBLE);
    }

    private void SetViewInvisible(View v)
    {
        v.setVisibility(View.INVISIBLE);
    }

    public void LogOut()
    {
        if (_recording)
            return;

        _currentPatient = null;

        SetViewInvisible(infoTextView);
        SetViewVisible(selectPatientsButton);
        SetViewInvisible(logOutButton);

        _mainActivity.RemoveLogoutButton();

        // TOOD udpate GUI here
    }

    public void StartRecording()
    {
        _collector.Restart();
        _recording = true;

        startButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.VISIBLE);

        _mainActivity.DisablePane();
    }

    public void PatientInfo()
    {
        if (_recording) // todo add graphical que that this button is disabled.
            return;

        Intent i = new Intent(_mainActivity, PatientInfo.class);
        i.putExtra(Intent_Constants.Patient_To_Edit, _currentPatient);
        startActivityForResult(i, 1);  // We only care about the req code, just if user was deleted or not.
    }

    private void StopRecording()
    {
        _recording = false;

        stopButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);

        ShowDialog();

        _mainActivity.EnablePane();
    }

    private void ShowDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mainActivity);
        builder.setMessage(General_Constants.AskUserToSaveData)
                .setPositiveButton(General_Constants.Yes, dialogClickListener)
                .setNegativeButton(General_Constants.No, dialogClickListener);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which)
            {
                case DialogInterface.BUTTON_POSITIVE:

                    if (_currentPatient == null)
                    {
                        // set slider to select mode (button clicks will trigger a dialog confirming they want to save to this patient)
                        SliderFragment sliderFrag = (SliderFragment) getFragmentManager().findFragmentById(R.id.fragment_firstpane);
                        sliderFrag.SetSelectMode(true);
                        _mainActivity.OpenPane();
                        _mainActivity.DisablePane();
                    }
                    else
                    {
                        SaveReading();
                    }
                    break;

                case DialogInterface.BUTTON_NEGATIVE: // Delete this reading.
                    break;
            }
        }
    };

    public void SaveReading()
    {
        Reading r = _collector.GetReading();
        try
        {
            r.PatientID = _currentPatient.ID;
            _dataModel.AddNewReading(r);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
        {
            Patient patientData = data.getParcelableExtra(Intent_Constants.NewPatientInfo);

            Log.d("PatientListSlider", patientData.FirstName);
            Log.d("PatientListSlider", patientData.LastName);

            //_patients.add(patientData); // TODO if patient name is updated or delete update GUI accordingly
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            _dataModel = new DataModel(getActivity());
            _nuJack = new NuJack(_dataAvailableListner);
            _nuJack.Start();

            Thread thread = new Thread(_fakeReader);
            thread.start();
            _collector = new ReadingCollector();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        _mainScreenFrag = this;
        _mainActivity = (MainActivity) getActivity();

        View v = inflater.inflate(R.layout.main_screen, container, false);
        InitializeButtons(v);

        this.prefs = _mainActivity.getPreferences(Context.MODE_PRIVATE);
        this._dataSync = new DataSync(_mainActivity, prefs);

        return v;
    }

    private void InitializeButtons(View v)
    {
        selectPatientsButton = v.findViewById(R.id.patient_list);
        selectPatientsButton.setOnClickListener(_mainListener);

        startButton = v.findViewById(R.id.start_reading);
        startButton.setOnClickListener(_mainListener);

        stopButton = v.findViewById(R.id.stop_reading_main);
        stopButton.setOnClickListener(_mainListener);

        logOutButton = v.findViewById(R.id.log_out_button);
        logOutButton.setOnClickListener(_mainListener);

        infoTextView = (TextView) v.findViewById(R.id.patient_name);
        infoTextView.setOnClickListener(_mainListener);

        percent = (TextView) v.findViewById(R.id.percentView);
    }

    public void WriteToFile(String content, String filename)
    {
        OutputStream fos;
        try {
            fos = _mainActivity.openFileOutput(filename, Context.MODE_WORLD_READABLE);
            fos.write(content.getBytes());
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }


    private int counter = 0;
    public String data = "";
    public String recData = "";
    private boolean done = false;

    OnDataAvailableListener _dataAvailableListner = new OnDataAvailableListener() {
        @Override
        public void DataAvailable(String _data) {

            _mainScreenFrag.data = _data;
            recData += (_data + ", ");

            counter++;
            if (counter > 1000 && !done) {
                WriteToFile(recData, "dump.csv");
                done = true;
                recData = "";
                // wrote file
                _mainActivity.runOnUiThread(new Runnable() { // UI update must be on main UI thread.
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Wrote the file!!!!", Toast.LENGTH_LONG).show();
                    }
                });
            }


            if (_recording)
            {
                // log stuff
            }

            try
            {
                _mainActivity.runOnUiThread(new Runnable() { // UI update must be on main UI thread.
                    @Override
                    public void run() {
                        percent.setText(data + "%");
                        //percent.setText(counter + "%");
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    private void fireRequest()
    {
    }

    //
    //  Thread to perform HTTP request in background
    //
    class RequestTask extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... uri)
        {
            try {
                int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpClient client = new DefaultHttpClient(httpParams);

                String data = "{\"json\": \"thang\"";

                //HttpPost request = new HttpPost("http://ip.jsontest.com/");
                //HttpPost request = new HttpPost("76.24.0.235");
                HttpPost request = new HttpPost();
                request.setEntity(new ByteArrayEntity(data.getBytes("UTF8")));
                HttpResponse response = client.execute(request);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            // Do stuff with response...
            //appendTextView(result);
        }
    }

    // Keeping the below uncommented for testing reader classes later on.
    int count = 1;

    private Runnable _fakeReader = new Runnable() {
        @Override
        public void run() {
            while (true)
            {
                count++;
                try
                {
                    if (_recording)
                        _collector.AddNewData(count);

                    _mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //percent.setText(count + "%");
                    }
                });
                    //percent.setText(count + "%");
                    Thread.sleep(50);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    };


    private OnClickListener _mainListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (_mainScreenFrag == null)
                return;

            int viewID = v.getId();
            switch (viewID) {
                case R.id.patient_list:
                    _mainScreenFrag.PatientListClick();
                    break;
                case R.id.start_reading:
                    _mainScreenFrag.StartRecording();
                    break;
                case R.id.stop_reading_main:
                    _mainScreenFrag.StopRecording();
                    break;
                case R.id.patient_name:
                    _mainScreenFrag.PatientInfo();
                    break;
                case R.id.log_out_button:
                    _mainScreenFrag.LogOut();
                    break;
            }
        }
    };
}
