package com.example.m5.oximetergui.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.dropbox.sync.android.DbxAccountManager;
import com.example.m5.oximetergui.Constants.General_Constants;

import java.util.prefs.Preferences;

/**
 * Created by Hunt on 4/11/2015.
 */
public class DataSync {

    SharedPreferences _preferences;
    private Activity _context;

    //Dropbox stuff
    private DbxAccountManager mDbxAcctMgr;

    public DataSync(Activity context, SharedPreferences p)
    {
        this._preferences = p;
        this._context = context;
        mDbxAcctMgr = DbxAccountManager.getInstance(context.getApplicationContext(), General_Constants.DROPBOX_APP_KEY, General_Constants.DROPBOX_APP_SECRET);
    }

    public void Run(String data)
    {
        if (this.checkSyncDropbox())
            SyncDropbox();
    }

    //
    //  DAN put your code here!
    //  Just hardcode any authentication credentials for now.
    //

    private void SyncDropbox()
    {
        mDbxAcctMgr.startLink(this._context, General_Constants.REQUEST_LINK_TO_DBX);
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
