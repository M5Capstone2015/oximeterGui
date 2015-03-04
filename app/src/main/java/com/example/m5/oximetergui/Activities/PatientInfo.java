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
import android.widget.TextView;

import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

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

        TextView dataVeiw = (TextView) findViewById(R.id.dataView);
        DataModel _dataModel = new DataModel(this);
        readings = _dataModel.GetDataByPatientID(patientData.ID);

        String txt = "";
        for (Reading r : readings)
            txt += (r.EndDate + "\n");

        dataVeiw.setText(txt);
        addChart();
    }

    private LineChartView chart;

    private void addChart()
    {
        this.chart = new LineChartView(this);
        ViewGroup layout = (ViewGroup) findViewById(R.id.graphContainer);
        layout.addView(chart);

        //List<PointValue> values = new ArrayList<PointValue>();

        List<PointValue> values = readings.get(1).ConvertData();
        /*
        values.add(new PointValue(1,2));
        values.add(new PointValue(2,3));
        values.add(new PointValue(3,4));
        values.add(new PointValue(4,5));
        values.add(new PointValue(5,2));
        values.add(new PointValue(6,3));
        */

        Line line = new Line(values);

        List<Line> lines = new ArrayList<Line>();

        line.setColor(ChartUtils.COLORS[2]);
        line.setShape(ValueShape.CIRCLE);
        //line.setCubic(isCubic);
        line.setFilled(true);
        line.setHasLabels(true);
        //line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);

        LineChartData data = new LineChartData(lines);
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Axis X");
        axisY.setName("Axis Y");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chart.setLineChartData(data);
        chart.setVisibility(View.GONE);
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

    public static void slide_down(Context ctx, View v) {
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
