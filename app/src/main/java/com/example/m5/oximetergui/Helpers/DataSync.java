package com.example.m5.oximetergui.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.R;

import java.util.prefs.Preferences;

/**
 * Created by Hunt on 4/11/2015.
 */
public class DataSync {

    SharedPreferences _preferences;
    private Activity _context;
    Resources resources;

    //Dropbox stuff
    private DbxAccountManager mDbxAcctMgr;

    public DataSync(Activity context, SharedPreferences p, Resources res)
    {
        this.resources = res;
        this._preferences = p;
        this._context = context;
        mDbxAcctMgr = DbxAccountManager.getInstance(context.getApplicationContext(), General_Constants.DROPBOX_APP_KEY, General_Constants.DROPBOX_APP_SECRET);
    }

    public void Run(String data)
    {
        boolean dr = this.checkSyncDropbox();
        //if (this.checkSyncDropbox())
        if (dr)
            SyncDropbox(data);
    }

    //
    //  DAN put your code here!
    //  Just hardcode any authentication credentials for now.
    //

    private void SyncDropbox(String data)
    {
        mDbxAcctMgr.startLink(this._context, General_Constants.REQUEST_LINK_TO_DBX);
        try
        {
            DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
            String filename = String.format("pulse_backup_%s.txt", DateHelper.GetBackupDate());
            //String filename = String.format("pulse_backup_%s.txt", DateHelper.GetBackupDate());
            DbxFile testFile = dbxFs.create(new DbxPath(filename));
            try
            {
                testFile.writeString(data);
            }
            finally
            {
                testFile.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean checkSyncDropbox()
    {
        return _preferences.getBoolean(resources.getString(R.string.dropbox_pref), true);
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
