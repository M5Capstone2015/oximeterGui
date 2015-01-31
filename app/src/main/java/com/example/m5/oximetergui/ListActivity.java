package com.example.m5.oximetergui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Daniel Abel on 1/31/2015.
 * Extends activity, contains functions necessary to make dynamic list classes to be used
 * throughout our application
 */
public class ListActivity extends Activity implements View.OnClickListener {
    //TODO MAKE CreateTextList CALL APPENDBUTTONLIST
    public void CreateTextList (Context context, ArrayList<String> list, int id) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativePatientList);

        int prevTextViewId = 0;
        for (int i = 0; i < list.size(); i++) {
            final TextView textView = new TextView(context);

            textView.setText(list.get(i));

            int curTextViewId = prevTextViewId + 1;
            textView.setId(curTextViewId);
            final RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

            if (i == 0 && id != -1) {
                params.addRule(RelativeLayout.BELOW, id);
            } else {
                params.addRule(RelativeLayout.BELOW, prevTextViewId);
            }
            textView.setLayoutParams(params);

            prevTextViewId = curTextViewId;
            layout.addView(textView, params);
        }
    }

    public void AppendButtonList (Context context, String text, int id) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativePatientList);
        final Button button = new Button(context);

        button.setText(text);

        int curTextViewId = id + 1;
        button.setId(curTextViewId);
        button.setOnClickListener(this);
        final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, id);
        button.setLayoutParams(params);

        layout.addView(button, params);
    }

    //Must be implemented to implement OnClickListener
    public void onClick(View v)
    {
    }
}
