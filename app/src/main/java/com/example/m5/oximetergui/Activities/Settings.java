package com.example.m5.oximetergui.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.m5.oximetergui.R;

public class Settings extends ActionBarActivity {

    ArrayAdapter adapter;
    ArrayAdapter read_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        InitControls();
    }

    private void InitControls()
    {
        Spinner spinner = (Spinner) findViewById(R.id.baud_spinner);

        this.adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner rec_spinner = (Spinner) findViewById(R.id.record_rate_spinner);

        this.read_adapter= ArrayAdapter.createFromResource(this,
                R.array.record_rates_array, android.R.layout.simple_spinner_item);
        read_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rec_spinner.setAdapter(read_adapter);
    }

    private void InitializeSettingsData()
    {
        String baudRate = "";
        try
        {
            this.adapter.getPosition(baudRate);
        }
        catch (Exception e)
        {
            String s = e.getMessage();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
