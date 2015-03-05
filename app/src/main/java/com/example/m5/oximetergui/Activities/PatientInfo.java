package com.example.m5.oximetergui.Activities;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
import com.example.m5.oximetergui.Helpers.DataViewAdapter;
import com.example.m5.oximetergui.Helpers.GraphicsUtils;
import com.example.m5.oximetergui.Models.DataModel;
import com.example.m5.oximetergui.R;

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
    List<Reading> readings;
    DataViewAdapter adapter;
    ListView _listView;
    Context context;
    LinearLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        context = this;

        container = (LinearLayout) findViewById(R.id.cont);

        _name = (TextView) findViewById(R.id.name);
        _age = (TextView) findViewById(R.id.agetextview);

        Patient patientData = null;
        try {
            patientData = getIntent().getParcelableExtra(Intent_Constants.Patient_To_Edit);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        _name.setText(patientData.FirstName + " " + patientData.LastName);
        _age.setText("Age: " + patientData.DateOfBirth);

        //TextView dataVeiw = (TextView) findViewById(R.);
        DataModel _dataModel = new DataModel(this);
        readings = _dataModel.GetDataByPatientID(patientData.ID);

        _listView = (ListView) findViewById(R.id.readings);
        _listView.setOnItemClickListener(adapterClickListener);
        adapter = new DataViewAdapter(this, readings);
        _listView.setAdapter(adapter);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
