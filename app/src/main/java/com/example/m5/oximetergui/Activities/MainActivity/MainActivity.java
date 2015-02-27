package com.example.m5.oximetergui.Activities.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;

import com.example.m5.oximetergui.Activities.NewPatient;
import com.example.m5.oximetergui.Constants.General_Constants;
import com.example.m5.oximetergui.Helpers.Slider;
import com.example.m5.oximetergui.R;


public class MainActivity extends Activity {

    Slider pane;

    public void ClosePane()
    {
        pane.closePane();
    }

    public boolean IsOpen()
    {
        return pane.isOpen();
    }

    public void _OpenPane()
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
                        getFragmentManager().findFragmentById(R.id.fragment_firstpane).setHasOptionsMenu(false);
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
                        getFragmentManager().findFragmentById(R.id.fragment_secondpane).setHasOptionsMenu(false);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

        });
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

    /*  TODO integrate this with Fragment.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            _patient = data.getParcelableExtra(Intent_Constants.NamePatient);
            _mainHelper.LoadPatient(_patient, false);
        }
    }
    */
}
