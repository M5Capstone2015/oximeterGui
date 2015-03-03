package com.example.m5.oximetergui.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m5.oximetergui.Constants.Intent_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class PatientInfo extends ActionBarActivity {

    TextView _name;
    TextView _age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        _name = (TextView) findViewById(R.id.name);
        _age = (TextView) findViewById(R.id.agetextview);

        Patient patientData = getIntent().getParcelableExtra(Intent_Constants.Patient_To_Edit);
        _name.setText(patientData.FirstName + " " + patientData.LastName);
        _age.setText("Age: " + patientData.DateOfBirth);

        addChart();
    }

    private void addChart()
    {
        LineChartView chart = new LineChartView(this);
        ViewGroup layout = (ViewGroup) findViewById(R.id.graphContainer);
        layout.addView(chart);

        List<PointValue> values = new ArrayList<PointValue>();
        values.add(new PointValue(1,2));
        values.add(new PointValue(2,3));
        values.add(new PointValue(3,4));
        values.add(new PointValue(4,5));
        values.add(new PointValue(5,2));
        values.add(new PointValue(6,3));

        Line line = new Line(values);

        List<Line> lines = new ArrayList<Line>();

        line.setColor(ChartUtils.COLORS[4]);
        line.setShape(ValueShape.CIRCLE);
        //line.setCubic(isCubic);
        line.setFilled(true);
        line.setHasLabels(true);
        //line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);

        LineChartData data = new LineChartData(lines);
        chart.setLineChartData(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
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
