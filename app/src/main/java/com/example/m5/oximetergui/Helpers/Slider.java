package com.example.m5.oximetergui.Helpers;

/**
 * Created by Hunt on 2/23/2015.
 */
import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Hunt on 2/23/2015.
 */
public class Slider extends SlidingPaneLayout {

    private boolean _activated = true;

    public Slider(Context context) {
        super(context);
    }

    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Slider(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void SetSlidable()
    {
        _activated = true;
    }

    public void DisableSlide()
    {
        _activated = false;
    }

    public boolean IsSlidable()
    {
        return _activated;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !_activated || super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!_activated) {
            // Careful here, view might be null
            getChildAt(1).dispatchTouchEvent(ev);
            getChildAt(0).dispatchTouchEvent(ev);
            return true;
        }
        return super.onTouchEvent(ev);
    }
}
