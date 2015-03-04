package com.example.m5.oximetergui.Helpers;

/**
 * Created by Hunt on 3/4/2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Data_Objects.Reading;
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

/**
 * Created by danabeled on 2/26/2015.
 */
public class DataViewAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List<Reading> _data;
    TextView _b;
    Context _dva;
    LinearLayout _container;



    public DataViewAdapter(Context context, List<Reading> data) {
        _dva = context;
        mInflater = LayoutInflater.from(context);
        _data = data;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int position){
        return _data.get(position);
    }

    @Override
    public boolean isEnabled(int position)
    {
        //return clickingEnabled;
        return true;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.data_view, parent, false);
        }
        else {
            view = convertView;
        }

        //_container = (LinearLayout) view.findViewById(R.id.expanderContainer);
        _b = (TextView) view.findViewById(R.id.expTextView);
        _b.setText(_data.get(position).EndDate);

        _container = (LinearLayout) view.findViewById(R.id.expanderContainer);


        /*
        View.OnClickListener itemClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Reading reading = _data.get(position);
                    if (_container == null)
                    {
                        int x = 5;
                        x+=1;
                    }
                    int count = _container.getChildCount();

                    LineChartView lcv;
                    if (count == 2) {
                        lcv = (LineChartView) _container.getChildAt(1);
                    } else {
                        lcv = createChart(reading);
                    }

                    //_container.addView(lcv);
                    Button b = new Button(_dva);
                    b.setText("Testing 123");
                    _container.addView(b);


                    Toast.makeText(_dva, ("Item " + reading.EndDate + " clicked!"), Toast.LENGTH_LONG).show();
                    //Toast.makeText(_dva, "Test", Toast.LENGTH_LONG).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        */

        //_b.setOnClickListener(itemClick);

        return view;
    }


    private LineChartView createChart(Reading readings)
    {
        LineChartView chart = new LineChartView(_dva);
        //ViewGroup layout = (ViewGroup) findViewById(R.id.graphContainer);
        //layout.addView(chart);

        //List<PointValue> values = new ArrayList<PointValue>();

        List<PointValue> values = readings.ConvertData();
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
        //chart.setVisibility(View.GONE);

        return chart;
    }

}
