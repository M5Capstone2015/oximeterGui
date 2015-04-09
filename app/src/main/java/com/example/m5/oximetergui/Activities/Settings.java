package com.example.m5.oximetergui.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.example.m5.oximetergui.R;

import static android.widget.CompoundButton.*;

public class Settings extends ActionBarActivity {

    private ArrayAdapter adapter;
    private ArrayAdapter read_adapter;

    private CheckBox autoSyncCheck;
    private CheckBox googleCheck;
    private CheckBox dropboxCheck;
    private CheckBox customCheck;
    private Button saveButton;

    Resources resources;
    SharedPreferences prefs;

    private boolean viewIsDirty = false;

    private CheckListener checkListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.prefs = getPreferences(Context.MODE_PRIVATE);

        this.InitializeControls();
    }

    private void InitializeControls()
    {
        // Save button instance
        this.saveButton = (Button) findViewById(R.id.saveSettingsButton);

        // Initialize Baud Rate Spinner
        Spinner spinner = (Spinner) findViewById(R.id.baud_spinner);

        this.adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        this.resources = getResources();
        String baud_pref = prefs.getString(resources.getString(R.string.baud_rate_pref), null);

        // User hasn't changed this, so default is 5 reads/second
        if (baud_pref == null)
            spinner.setSelection(2);

        // Initialize Record Rate Spinner
        Spinner rec_spinner = (Spinner) findViewById(R.id.record_rate_spinner);

        this.read_adapter= ArrayAdapter.createFromResource(this,
                R.array.record_rates_array, android.R.layout.simple_spinner_item);
        read_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rec_spinner.setAdapter(read_adapter);

        String read_pref = prefs.getString("read_rate_pref", null);

        // User hasn't changed this, so default is 12 per minute (once every 5 seconds)
        if (read_pref == null)
            rec_spinner.setSelection(3);

        // Load preference form Shared Prefs
        Boolean auto_sync_pref = prefs.getBoolean(resources.getString(R.string.auto_sync_pref), true);
        Boolean dropbox_pref = prefs.getBoolean(resources.getString(R.string.dropbox_pref), false);
        Boolean google_pref = prefs.getBoolean(resources.getString(R.string.google_pref), false);
        Boolean custom_pref = prefs.getBoolean(resources.getString(R.string.google_pref), false);

        // Get and save CheckBox control instances
        this.autoSyncCheck = (CheckBox) findViewById(R.id.auto_sync_checkbox);
        this.dropboxCheck = (CheckBox) findViewById(R.id.dropBox_sync_check);
        this.googleCheck = (CheckBox) findViewById(R.id.google_sync_check);
        this.customCheck = (CheckBox) findViewById(R.id.customSyncCheck);

        // Set checkboxes to appropriate values
        autoSyncCheck.setChecked(auto_sync_pref);
        dropboxCheck.setChecked(dropbox_pref);
        googleCheck.setChecked(google_pref);
        customCheck.setChecked(custom_pref);

        // Attach event
        // s
        this.checkListener = new CheckListener(this);
        autoSyncCheck.setOnCheckedChangeListener(checkListener);
        dropboxCheck.setOnCheckedChangeListener(checkListener);
        googleCheck.setOnCheckedChangeListener(checkListener);
        customCheck.setOnCheckedChangeListener(checkListener);
    }

    private void setPref(String pref_name, Boolean val)
    {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putBoolean(pref_name, val);
        editor.commit();
    }

    private void SavePrefs()
    {
        setPref(this.resources.getString(R.string.auto_sync_pref), this.autoSyncCheck.isChecked());
        setPref(this.resources.getString(R.string.dropbox_pref), this.customCheck.isChecked());
        setPref(this.resources.getString(R.string.google_pref), this.googleCheck.isChecked());
        setPref(this.resources.getString(R.string.custom_pref), this.customCheck.isChecked());
    }

    private class CheckListener implements OnCheckedChangeListener
    {
        private Settings settingsActivity;

        public CheckListener(Settings settings)
        {
            settingsActivity = settings;
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (settingsActivity.viewIsDirty)
                return;

            settingsActivity.viewIsDirty = true;

            Button savebutton = (Button) settingsActivity.findViewById(R.id.saveSettingsButton);
            savebutton.setEnabled(true);

            // set button to active state

            /*
            Resources res = getApplicationContext().getResources();

            switch (buttonView.getId())
            {
                case R.id.auto_sync_checkbox:
                    setPref(res.getString(R.string.auto_sync_pref), isChecked);
                    break;
                case R.id.dropBox_sync_check:
                    break;
                case R.id.google_sync_check:
                    break;
                case R.id.customSyncCheck:
                    break;
            }
            */
        }
    };


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

    public void saveSettingsClick(View v)
    {
        SavePrefs();
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
