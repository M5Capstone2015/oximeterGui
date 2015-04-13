package com.example.m5.oximetergui.Activities.MainActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Hunt on 4/12/2015.
 */
public class PercentView extends View {

    public PercentView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        int radius;
        radius = 100;

        int height = getHeight();
        int width = getWidth();

        int c = Color.parseColor("#EBECFB");
        Paint p = new Paint();
        p.setColor(c);


        //Paint paint = new Paint();
        p.setStyle(Paint.Style.FILL);
        //paint.setColor(Color.WHITE);

        canvas.drawPaint(p);

        //canvas.drawRect(0, 0, width, height, p);

        // Use Color.parseColor to define HTML colors
        p.setColor(Color.parseColor("#1333B0"));
        p.setStrokeWidth(2);
        canvas.drawCircle(x / 2, y / 2, radius, p);
    }

}
