package com.example.m5.oximetergui.Helpers;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.prefs.Preferences;

/**
 * Created by Hunt on 4/11/2015.
 */
public class DataSync {

    SharedPreferences _preferences;

    public DataSync(Activity context, SharedPreferences p)
    {
        this._preferences = p;
    }

    public void Run()
    {
        if (this.checkSyncCustom())
            SyncDropbox();
    }

    //
    //  DAN put your code here!
    //  Just hardcode any authentication credentials for now.
    //
    private void SyncDropbox()
    {
    }

    private boolean checkSyncDropbox()
    {
        return true;
    }

    private boolean checkSyncCustom()
    {
        return false;
    }

    private boolean checkSyncGoogle()
    {
        return false;
    }
}
