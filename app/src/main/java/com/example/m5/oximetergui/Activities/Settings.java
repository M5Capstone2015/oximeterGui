package com.example.m5.oximetergui.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.m5.oximetergui.R;

import static android.widget.CompoundButton.*;

public class Settings extends ActionBarActivity {

    private ArrayAdapter adapter;
    private ArrayAdapter read_adapter;

    private EditText customServer;
    private CheckBox autoSyncCheck;
    private CheckBox googleCheck;
    private CheckBox dropboxCheck;
    private CheckBox customCheck;
    private Spinner baudSpinner;
    private Spinner readSpinner;
    private Button saveButton;


    Resources resources;
    SharedPreferences prefs;

    private boolean viewIsDirty = false;

    private CheckListener checkListener;
    private OnSpinnerSelected spinnerListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Preferences
        this.prefs = getPreferences(Context.MODE_PRIVATE);

        // Load setting data
        this.LoadSettingData();
    }

    private void LoadSettingData()
    {
        // Get control instances
        this.saveButton = (Button) findViewById(R.id.saveSettingsButton);
        this.baudSpinner = (Spinner) findViewById(R.id.baud_spinner);
        this.readSpinner = (Spinner) findViewById(R.id.record_rate_spinner);
        this.autoSyncCheck = (CheckBox) findViewById(R.id.auto_sync_checkbox);
        this.dropboxCheck = (CheckBox) findViewById(R.id.dropBox_sync_check);
        this.googleCheck = (CheckBox) findViewById(R.id.google_sync_check);
        this.customCheck = (CheckBox) findViewById(R.id.customSyncCheck);
        this.customServer = (EditText) findViewById((R.id.custom_server_editbox));

        // Initialize Baud Rate Spinner
        this.adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        baudSpinner.setAdapter(adapter);
        this.resources = getResources();

        // Initialize Record Rate Spinner
        this.read_adapter= ArrayAdapter.createFromResource(this,
                R.array.record_rates_array, android.R.layout.simple_spinner_item);
        read_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        readSpinner.setAdapter(read_adapter);

        // Load preference form Shared Prefs
        Boolean auto_sync_pref = prefs.getBoolean(resources.getString(R.string.auto_sync_pref), true);
        Boolean dropbox_pref = prefs.getBoolean(resources.getString(R.string.dropbox_pref), false);
        Boolean google_pref = prefs.getBoolean(resources.getString(R.string.google_pref), false);
        Boolean custom_pref = prefs.getBoolean(resources.getString(R.string.google_pref), false);

        int baud_pref = prefs.getInt(resources.getString(R.string.baud_rate_pref), 0);
        int read_pref = prefs.getInt(resources.getString(R.string.read_rate_pref), 0);

        String serverURL = prefs.getString(resources.getString(R.string.custom_server_url), "");
        this.customServer.setText(serverURL);

        // Set spinners
        this.baudSpinner.setSelection(baud_pref);
        this.readSpinner.setSelection(read_pref);

        // Set checkboxes to appropriate values
        this.autoSyncCheck.setChecked(auto_sync_pref);
        this.dropboxCheck.setChecked(dropbox_pref);
        this.googleCheck.setChecked(google_pref);
        this.customCheck.setChecked(custom_pref);

        // Initialize and attach events
        this.checkListener = new CheckListener(this);
        this.spinnerListener = new OnSpinnerSelected(this);

        this.baudSpinner.setOnItemSelectedListener(spinnerListener);
        this.readSpinner.setOnItemSelectedListener(spinnerListener);
        this.autoSyncCheck.setOnCheckedChangeListener(checkListener);
        this.dropboxCheck.setOnCheckedChangeListener(checkListener);
        this.googleCheck.setOnCheckedChangeListener(checkListener);
        this.customCheck.setOnCheckedChangeListener(checkListener);
    }

    private void setPref(String pref_name, Boolean val)
    {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putBoolean(pref_name, val);
        editor.commit();
    }

    private void setPref(String pref_name, int val)
    {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putInt(pref_name, val);
        editor.commit();
    }

    private void setPref(String pref_name, String val)
    {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString(pref_name, val);
        editor.commit();
    }

    private void SavePrefs()
    {
        // Save auto-sync
        setPref(this.resources.getString(R.string.auto_sync_pref), this.autoSyncCheck.isChecked());

        // Save sync options
        setPref(this.resources.getString(R.string.dropbox_pref), this.dropboxCheck.isChecked());
        setPref(this.resources.getString(R.string.google_pref), this.googleCheck.isChecked());
        setPref(this.resources.getString(R.string.custom_pref), this.customCheck.isChecked());

        // Save baud/read rate
        setPref(this.resources.getString(R.string.baud_rate_pref), this.baudSpinner.getSelectedItemPosition());
        setPref(this.resources.getString(R.string.read_rate_pref), this.readSpinner.getSelectedItemPosition());

        // Save custom server URL
        setPref(this.resources.getString(R.string.custom_server_url), this.customServer.getText().toString());
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
            settingsActivity.MakeDirty();
        }
    };

    class OnSpinnerSelected implements AdapterView.OnItemSelectedListener {

        private Settings settingsActivity;

        public OnSpinnerSelected(Settings settings)
        {
            settingsActivity = settings;
        }

        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            settingsActivity.MakeDirty();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
            // your code here
        }

    };

    public void MakeDirty()
    {
        if (this.viewIsDirty)
                return;

        this.viewIsDirty = true;
        this.saveButton.setEnabled(true);
    }

    public void CleanView()
    {
        this.viewIsDirty = false;
        this.saveButton.setEnabled(false);
    }

    public void saveSettingsClick(View v)
    {
        SavePrefs();
        CleanView();
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
