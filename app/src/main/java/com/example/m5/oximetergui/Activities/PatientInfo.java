package com.example.m5.oximetergui.Activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.DataViewAdapter;
import com.example.m5.oximetergui.Helpers.GraphicsUtils;
import com.example.m5.oximetergui.Helpers.ImageHelper;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class PatientInfo extends ActionBarActivity {

    TextView _name;
    TextView _age;
    TextView _location;
    TextView _notes;
    EditText _nameFirstEdit;
    List<Reading> readings;
    DataViewAdapter adapter;
    ListView _listView;
    ImageView _imageView;
    Context context;
    LinearLayout container;
    ImageHelper _imageHelper = new ImageHelper(this);
    Boolean _editmode = false;
    ViewSwitcher viewSwitcher;
    Animation slide_in_left, slide_out_right;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        context = this;
        container = (LinearLayout) findViewById(R.id.cont);

        _name = (TextView) findViewById(R.id.name);
        _age = (TextView) findViewById(R.id.agetextview);
        _location = (TextView) findViewById(R.id.location);
        _notes = (TextView) findViewById(R.id.notesBody);
        _nameFirstEdit = (EditText) findViewById(R.id.firstName);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);
        slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        Patient patientData = null;
        try {
            patientData = getIntent().getParcelableExtra(Intent_Constants.Patient_To_Edit);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        _name.setText(patientData.FirstName + " " + patientData.LastName);
        _nameFirstEdit.setText(patientData.FirstName);
        _age.setText("Age: " + patientData.DateOfBirth);
        _location.setText("Location: " + patientData.Location);
        _notes.setText(patientData.Notes);

        //TextView dataVeiw = (TextView) findViewById(R.);
        DataModel _dataModel = new DataModel(this);
        readings = _dataModel.GetDataByPatientID(patientData.ID);

        _listView = (ListView) findViewById(R.id.readings);
        _listView.setOnItemClickListener(adapterClickListener);
        adapter = new DataViewAdapter(this, readings);
        _listView.setAdapter(adapter);

        _imageView = (ImageView) findViewById(R.id.imageView);

        if (!patientData.imageFilePath.matches("")) {
            LoadImage(this, patientData, _imageView);
        }
        else {
            _imageView.setImageResource(R.drawable.placeholder);
        }
        _imageView.setOnClickListener(imageViewListener);
    }

    private void LoadImage(Context context, Patient patient, ImageView view)
    {
        ContextWrapper cw;
        File directory;

        try {
            cw = new ContextWrapper(context);
            directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        String filename = patient.ID.toString() + "_image.jpg";
        File filePath = new File(directory, filename);

        int size = (int) filePath.length();
        byte[] bytes = new byte[size];

        try
        {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filePath));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            Bitmap iamge = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            view.setImageBitmap(iamge);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private ImageView.OnClickListener imageViewListener = new ImageView.OnClickListener() {

        @Override
        public void onClick(View dialog)
        {
            _imageHelper.dispatchChoosePictureIntent();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case Intent_Constants.SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    _imageHelper.setImage(selectedImage, _imageView);
                }
        }
    }

    private LineChartView chart;

    private AdapterView.OnItemClickListener adapterClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> list, View view, int position, long i)
        {
            Reading selectedPatient = (Reading) list.getAdapter().getItem(position);  // Get patient from intent

            LinearLayout continaer = (LinearLayout) view;
            ViewGroup chartContainer = (ViewGroup) view.findViewById(R.id.chartContainer);

            LineChartView lcv = (LineChartView) continaer.findViewById(R.id.chart);

            if (!selectedPatient.IsDrawn)
            {
                // set data here.
                populateChart(lcv, selectedPatient);
                selectedPatient.IsDrawn = true;
            }

            if (lcv.isShown()) {
                GraphicsUtils.slide_up(context, lcv);
                chartContainer.setVisibility(View.GONE);
            }
            else {
                chartContainer.setVisibility(View.VISIBLE);
                GraphicsUtils.slide_down(context, lcv);
            }
        }
    };

    private void populateChart(LineChartView lcv, Reading r)
    {
        List<PointValue> values = r.ConvertData();

        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[2]);
        line.setShape(ValueShape.CIRCLE);
        line.setFilled(true);
        line.setHasLabels(true);
        line.setHasLines(true);
        line.setHasPoints(true);

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);

        axisX.setName("Axis X");
        axisY.setName("Axis Y");

        LineChartData data = new LineChartData(lines);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        lcv.setLineChartData(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    public void slideDown(View v)
    {
        //addChart();
        if (chart.getVisibility() == View.GONE)
        {
            chart.setVisibility(View.VISIBLE);
            slide_down(this, chart);
        }
        else
        {
            slide_up(this, chart);
            chart.setVisibility(View.GONE);
        }
        //addChart();
    }

    public static void slide_down(Context ctx, View v)
    {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if (a != null)
        {
            a.reset();
            if (v != null)
            {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context ctx, View v)
    {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if (a != null)
        {
            a.reset();
            if (v != null)
            {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            case R.id.edit:
                Log.d("PatientInfo", "Edit button pressed");
                if (this._editmode == false) {
                    item.setIcon(R.drawable.floppydisk);
                    this._editmode = true;
                    viewSwitcher.showNext();
                }
                else {
                    item.setIcon(R.drawable.pencil2);
                    this._editmode = false;
                    viewSwitcher.showPrevious();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
