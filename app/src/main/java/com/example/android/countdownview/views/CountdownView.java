package com.example.android.countdownview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.android.countdownview.R;

import static java.lang.Thread.sleep;

/**
 * Created by Jonathon on 4/19/2017.
 */

public class CountdownView extends android.support.v7.widget.AppCompatTextView {
    private static final int MILLIS = 0, SECONDS = 1, MINUTES = 2;
    private int time;                   // The current time of the countdown
    private int timeScale;              // The time scale
    private int callbackFrequency;      // The frequency of callbacks
    private long nextCallBackTime;      // The time the next callback will run
    private int signficantTimeStep;     // The time scale in terms of millis
    private final Runnable tick =
            new Runnable(){
                @Override
                public void run() {
                    try {
                        sleep(signficantTimeStep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    callback();
                    time--;
                    setText(""+time);
                    if(time > 0)
                        post(this);
                }

                private void callback() {
                    //TODO: callback
                }
            };

    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CountdownView,
                0, 0);
        try {
            time = a.getInteger(R.styleable.CountdownView_time, 0);
            timeScale = a.getInteger(R.styleable.CountdownView_timeScale, 1);
            callbackFrequency = a.getInteger(R.styleable.CountdownView_callbackFrequency, 1);
        } finally {
            a.recycle();
        }

        signficantTimeStep = (timeScale == MILLIS)?1:(timeScale==SECONDS)?1000:60000;
        this.setText(""+time);
        this.post(tick);
    }



}
