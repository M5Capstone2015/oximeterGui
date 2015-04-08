package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.m5.oximetergui.Activities.NewPatient;
import com.example.m5.oximetergui.Activities.PatientInfo;
import com.example.m5.oximetergui.Activities.Settings;
import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Data_Objects.Patient;
import com.example.m5.oximetergui.Helpers.Slider;
import com.example.m5.oximetergui.R;


public class MainActivity extends ActionBarActivity {

    Slider pane;
    MenuItem logoutButton;

    public void ClosePane()
    {
        pane.closePane();
    }

    public boolean IsOpen()
    {
        return pane.isOpen();
    }

    public void OpenPane()
    {
        pane.openPane();
    }

    public void DisablePane()
    {
        pane.DisableSlide();
    }

    public void EnablePane()
    {
        pane.SetSlidable();
    }

    public void AddLoginButton()
    {
        logoutButton.setVisible(true);
    }

    public void RemoveLogoutButton()
    {
        logoutButton.setVisible(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        pane = (Slider) findViewById(R.id.slidingpanelayout);

        pane.setPanelSlideListener(new SlidingPaneLayout.SimplePanelSlideListener()  // TODO decide what to do about action bar in general, handle here.
        {

            @Override
            public void onPanelClosed(View panel) {
                switch (panel.getId()) {
                    case R.id.fragment_secondpane:
                        getFragmentManager().findFragmentById(R.id.fragment_firstpane).setHasOptionsMenu(true);
                        getFragmentManager().findFragmentById(R.id.fragment_secondpane).setHasOptionsMenu(true);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPanelOpened(View panel) {
                switch (panel.getId()) {
                    case R.id.fragment_secondpane:
                        getFragmentManager().findFragmentById(R.id.fragment_firstpane).setHasOptionsMenu(true);
                        getFragmentManager().findFragmentById(R.id.fragment_secondpane).setHasOptionsMenu(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

        });

        setTitle("Pulse-Ox");

    }


    @Override
    public void onResume()  // TODO load percent/patient selected/and rolling average data
    {
        super.onResume();
        //if (_nuJack != null)
           // _nuJack.Start();
    }

    @Override
    public void onPause() // TODO save percent/patient selected/and rolling average data
    {
        super.onPause();
        //if (_nuJack != null) // TODO need to create public interface on frags to save shit.
        //    _nuJack.Stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        this.logoutButton= menu.findItem(R.id.log_out);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            case R.id.log_out:
                LogOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void LogOut()
    {
        try {
            MainScreenFrag frag = (MainScreenFrag) getFragmentManager().findFragmentById(R.id.fragment_secondpane);
            frag.LogOut();
        }
        catch (Exception e)
        {
            String err = e.getMessage();
        }

    }

}
