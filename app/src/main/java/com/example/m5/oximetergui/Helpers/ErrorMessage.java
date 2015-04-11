package com.example.m5.oximetergui.Helpers;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.m5.oximetergui.R;

/**
 * Created by danabeled on 2/5/2015.
 */
public class ErrorMessage {
    private String mErrorText;
    private TextView mTv;
    private Activity mActivity;

    public ErrorMessage(String errorText, Activity activity)
    {
        mErrorText = errorText;
        mActivity = activity;
    }

    public TextView CreateErrorBelow(int id)
    {
        mTv = (TextView) mActivity.getLayoutInflater().inflate(R.layout.textview_error_message, null);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //mTv.setText(mErrorText);
        /*params.addRule(RelativeLayout.BELOW, id);
        mTv.setLayoutParams(params);*/
        return mTv;
    }
}
