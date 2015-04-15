package com.example.m5.oximetergui.Activities.MainActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileStatus;
import com.example.m5.oximetergui.Activities.PatientInfo;
import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.BackupBuilder;
import com.example.m5.oximetergui.Helpers.DataSync;
import com.example.m5.oximetergui.Helpers.ReadingCollector;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.Models.PatientModel;
import com.example.m5.oximetergui.NuJack.NuJack;
import com.example.m5.oximetergui.NuJack.OnDataAvailableListener;
import com.example.m5.oximetergui.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Hunt on 2/23/2015.
 */
public class MainScreenFrag extends Fragment {

    // --- API Objects --- ///
    private SharedPreferences prefs;
    private MainScreenFrag _mainScreenFrag;
    private MainActivity _mainActivity;
    private Resources _resources;

    // --- Helpers/Model --- //
    private ReadingCollector _collector = null;  // TODO wrap all this (minus NuJack) in an object for easy serialization.
    private NuJack _nuJack = null;
    private DataModel _dataModel = null;
    private PatientModel _patientModel = null;
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
    private LinearLayout syncCont;
 // --- Async Workers --- //
    private Handler mHandler = new Handler();
    private RequestTask requestTask = new RequestTask();


    public void StartSync()
    {
        final ProgressBar pb = (ProgressBar) _mainActivity.findViewById(R.id.progressBar);
        syncCont = (LinearLayout) _mainActivity.findViewById(R.id.syncContainer);

        syncCont.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);

        BackupBuilder backupBuilder = new BackupBuilder(this._patientModel, this._dataModel);

        if (this._dataSync != null) {
            this._dataSync.Run(backupBuilder.GenerateFile());
        }
        pb.setVisibility(View.GONE);

        //requestTask.execute("", "");

        /*
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
        */
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
        if (resultCode == Activity.RESULT_OK && requestCode == General_Constants.NEW_PATIENT_REQUEST)
        {
            Patient patientData = data.getParcelableExtra(Intent_Constants.NewPatientInfo);

            Log.d("PatientListSlider", patientData.FirstName);
            Log.d("PatientListSlider", patientData.LastName);

            //_patients.add(patientData); // TODO if patient name is updated or delete update GUI accordingly
        }
        if (requestCode == General_Constants.REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                // ... Start using Dropbox files.
                Toast.makeText(getActivity(), "DropBox sync succeded! :-)", Toast.LENGTH_LONG).show();
            } else {
                // ... Link failed or was cancelled by the user.
                Toast.makeText(getActivity(), "DropBox sync failed :(", Toast.LENGTH_LONG).show();
            }
            // make thingy go up
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            _dataModel = new DataModel(getActivity());
            _patientModel = new PatientModel(getActivity());
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
        this._mainScreenFrag = this;
        this._mainActivity = (MainActivity) getActivity();

        this._resources = getResources();

        View v = inflater.inflate(R.layout.main_screen, container, false);
        InitializeButtons(v);

        this.prefs = _mainActivity.getSharedPreferences("pulse_prefs", Context.MODE_PRIVATE);
        this._dataSync = new DataSync(_mainActivity, prefs, _resources);

        return v;
    }

    private void InitializeButtons(View v)
    {
        this.selectPatientsButton = v.findViewById(R.id.patient_list);
        this.selectPatientsButton.setOnClickListener(_mainListener);

        this.startButton = v.findViewById(R.id.start_reading);
        this.startButton.setOnClickListener(_mainListener);

        this.stopButton = v.findViewById(R.id.stop_reading_main);
        this.stopButton.setOnClickListener(_mainListener);

        this.logOutButton = v.findViewById(R.id.log_out_button);
        this.logOutButton.setOnClickListener(_mainListener);

        this.infoTextView = (TextView) v.findViewById(R.id.patient_name);
        this.infoTextView.setOnClickListener(_mainListener);

        this.tv = v.findViewById(R.id.circle_2);
        this.bpmCirlce = (TextView) v.findViewById(R.id.circle_3);

        this.smallCircle = v.findViewById(R.id.smallGreen);
        this.percent = (TextView) v.findViewById(R.id.percentView2);
    }

    private void SetBar(int val)
    {
        if (this.smallCircle == null)
            return;

        //int height = smallCircle.getHeight();
        //smallCircle.getBackground().setBounds(0, 0, 0, 1000);
        //smallCircle.requestLayout();

        smallCircle.getLayoutParams().height = val;
        smallCircle.requestLayout();
    }

    View tv;
    TextView bpmCirlce;
    View smallCircle;
    private boolean _set = false;

    public void moveTv()
    {
        int sh = smallCircle.getHeight();
        int sw = smallCircle.getWidth();

        float sx = smallCircle.getX();
        float sy = smallCircle.getY();

        int height = this.tv.getHeight();
        int width = this.tv.getWidth();

        // Move bar to the center of
        if (!_set) {
            smallCircle.setX(sx + width / 2 - sw / 2);
            smallCircle.setY(sy + height / 2 - sh / 2);
            _set = true;
        }

        int bigRadius = this.percent.getHeight();

        float bigCircle_Y = this.percent.getY();
        float bigCircle_X = this.percent.getX();

        this.tv.setY(bigCircle_Y);
        this.tv.setX(bigCircle_X+ bigRadius);

        this.bpmCirlce.setY(bigCircle_Y+ bigRadius - height);
        this.bpmCirlce.setX(bigCircle_X + bigRadius);
    }

    private void setView()
    {
        tv.post(new Runnable() {
            @Override
            public void run() {
                tv.getHeight(); //height is ready
            }
        });
    }


    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = _mainActivity.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
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
            if (counter > 300 && !done)
            {
                WriteToFile(recData, "dump.csv");
                done = true;
                recData = "";

                /*
                _mainActivity.runOnUiThread(new Runnable() { // UI update must be on main UI thread.
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Wrote the file!!!!", Toast.LENGTH_LONG).show();
                    }
                });
                */
            }

            if (_recording)
                _collector.AddNewData(Integer.parseInt(data));

            try
            {
                _mainActivity.runOnUiThread(new Runnable() { // UI update must be on main UI thread.
                    @Override
                    public void run() {
                        //if (done) {
                            //percent.setText("Done!");
                            //return;
                        //}
                        //percent.setText(data + "%");
                        //percent.setText(counter + "%");

                        int parsed = Integer.parseInt(data);
                        animate(parsed);

                        int res = PeakDetection(parsed);

                        if (res > 0)
                            bpmCirlce.setText(res + " b/m");

                        //percent.setText(parsed);
                        percent.setText(data);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };


    private ArrayList<Integer> buffer = new ArrayList<>();
    private ArrayList<Integer> peeks = new ArrayList<>();

    public int PeakDetection(int data)
    {
        // here if smaller than size fuck this shit below just add an return
        //
        buffer.add(data);
        if (buffer.size() < 15)
            return -1;


        int prev = buffer.get(buffer.size()-2);
        int second_pre = buffer.get(buffer.size()-3);

        int prev_deriv = prev - second_pre;
        int second_deriv = data - prev;
        int sample_between_beat;

        if (prev_deriv >= 0 && second_deriv < 0)
        {
            peeks.add(data);
            if(peeks.size() < 4) { return -1; }
            peeks.remove(0);
            int psize = peeks.size();

            if((peeks.get(psize-2) > peeks.get(psize-1)) && (peeks.get(psize-2) > peeks.get(psize-3))) {
                // This means the previous peak is a beat
                if(buffer.size() < 10) {
                    // This isn't a real beat, just a double detection
                    return -1;
                }

                else {
                    // Delete everything before the beat
                    int size = buffer.size();
                    printLst(buffer);
                    //System.out.printf("Numbe of elements before %d%n", buffer.size());
                    deleteAllButLast(buffer);
//					printLst(buffer);
                    //System.out.printf("Numbe of elements after %d%n", buffer.size());
                    return size;
                }

            }

        }
        return -1;
    }

    public static void printLst(ArrayList<Integer> lst)
    {
        for (int i = 0; i < lst.size()-2; i++)
        {
            int item = lst.get(i);
            //System.out.printf("element: %s%n", item);
        }
    }

    public static void deleteAllButLast(ArrayList<Integer> lst)
    {
        final int size = lst.size() -2;
        try {
            for (int i = 0; i < size; i++) {
                lst.remove(0);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //
    //  Thread to perform HTTP request in background
    //
    class RequestTask extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... uri)
        {
            //String url = uri[0];
            //String data = uri[1];
            // Third one uneccesary.

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
            Log.d("AsyncTask","Finished");
            syncCont.setVisibility(View.GONE);


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
                        Random r = new Random();
                        int high = 50;
                        int low = 10;
                        int R = r.nextInt(high-low) + low;

                        //SetBar(R);
                        //animate();
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

    private int[] intArray = new int[] {1,2,3,4,5,6,7,8,9,10,9,8,7,6,5,4,3,2};
    private int pos = 0;
    private float last = 0f;

    private int _emptyCount = 0;
    private int _nonEmptyCount = 0;



    private void animate(int data)
    {

        /*
        ObjectAnimator animX = ObjectAnimator.ofFloat(this.smallCircle, "x", 50f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(this.smallCircle, "y", 100f);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        animSetXY.start();
        */

        //View myView=(View) findViewById(R.id.heightView);
        //ObjectAnimator scaleXIn = ObjectAnimator.ofFloat(smallCircle, "scaleX", 0f, 1f);

        /*
        if (data < 705)
            _emptyCount++;
        else
        {
            if (_emptyCount > 0)
                _emptyCount--;
        }

        if (_emptyCount > 10 && smallCircle.getVisibility() == View.VISIBLE)
        {
            this.SetViewInvisible(smallCircle);
        }
        else if (smallCircle.getVisibility() == View.INVISIBLE)
            this.SetViewVisible(smallCircle);
        */

        /*
        if (data < 705) {
            this.SetViewInvisible(smallCircle);
            return;
        }
        else
        {
            if (smallCircle.getVisibility() == View.INVISIBLE)
                this.SetViewVisible(smallCircle);
        }
        */

        pos++;

        if (pos > intArray.length - 1)
            pos = 0;

        float scale = ((float) intArray[pos]) / 10f;

        /*
        Random r = new Random();
        int high = 50;
        int low = 10;
        int R = r.nextInt(high-low) + low;
        */

        int upperLimit = 950;
        if (data > upperLimit)
        {
            data = upperLimit;
        }

        int lowerLimit = 800;
        if (data < lowerLimit)
        {
            data = lowerLimit;
        }

        int d2 = 1000 - data;

        float s2 = ((float) d2) / 150f;


        //ObjectAnimator scaleYIn = ObjectAnimator.ofFloat(smallCircle, "scaleY", 0f, 1f);
        //ObjectAnimator scaleYIn = ObjectAnimator.ofFloat(smallCircle, "scaleY", this.last, scale);

        ObjectAnimator scaleYIn = ObjectAnimator.ofFloat(smallCircle, "scaleY", this.last, s2);
        //ObjectAnimator scaleYIn = ObjectAnimator.ofFloat(smallCircle, "scaleY", s2, this.last);

        //this.last = scale;
        this.last = s2;

        scaleYIn.setDuration(50);
        scaleYIn.start();


        //AnimatorSet set = new AnimatorSet();

        //set.play(scaleXIn).with(scaleYIn);
        //set.setDuration(2000);
        //set.start();


    }


    private OnClickListener _mainListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (_mainScreenFrag == null)
                return;

            int viewID = v.getId();
            switch (viewID) {
                case R.id.patient_list:
                    //_mainScreenFrag.SetBar(20);
                    //_mainScreenFrag.animate();
                    //_mainScreenFrag.PatientListClick();
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
