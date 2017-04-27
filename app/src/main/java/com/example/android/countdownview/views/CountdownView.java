package com.example.android.countdownview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.android.countdownview.Callbacks.CountdownFinishCallback;
import com.example.android.countdownview.Callbacks.CountdownTickCallback;
import com.example.android.countdownview.R;

import static java.lang.Thread.sleep;

/**
 * Created by Jonathon on 4/19/2017.
 */

public class CountdownView extends android.support.v7.widget.AppCompatTextView {
    private static final int MILLIS = 0, SECONDS = 1, MINUTES = 2;
    private int time;                   // The current time of the countdown
    private int timeScale;              // The time scale
    private int signficantTimeStep;     // The time scale in terms of millis
    private boolean isRunning;          // tests if the countdown is running

    //The two types of callbacks that can occur
    private CountdownTickCallback tickCallback;
    private CountdownFinishCallback finishCallback;

    //This is the runnable task that ticks down 1 time unit
    private final Runnable tick =
            new Runnable(){
                @Override
                public void run() {

                    // return if no longer running
                    if(!isRunning)
                        return;

                    //sleep for the specified amount of time
                    try {
                        sleep(signficantTimeStep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //decrement the amount of time and update the gui
                    time--;
                    setText(""+time);

                    //perform the necessary callbacks
                    if(time > 0 && tickCallback != null)
                        tickCallback.callback(time);
                    else if (time == 0 && finishCallback != null)
                        finishCallback.callback();

                    //if the time has not yet reached 0, redo the action
                    if(time > 0)
                        post(this);
                    else
                        isRunning = false;
                }
            };

    /**
     * The constructor for the Countdown view
     * @param context
     * @param attrs
     */
    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        //grab the necessary attributes needed for the countdown
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CountdownView,
                0, 0);
        try {
            time = a.getInteger(R.styleable.CountdownView_time, 0);
            timeScale = a.getInteger(R.styleable.CountdownView_timeScale, 1);
            isRunning = a.getBoolean(R.styleable.CountdownView_immediatelyBeginCountdown, false);
        } finally {
            a.recycle();
        }

        //convert the enum timestep to milliseconds
        signficantTimeStep = (timeScale == MILLIS)?1:(timeScale==SECONDS)?1000:60000;

        //update the text with the starting time
        this.setText(""+time);

        //begin the countdown
        if(isRunning)
            this.post(tick);
    }

    /**
     * Begins the countdown, only if it is not already running
     */
    public void start(){
        if(isRunning)
            return;
        isRunning = true;
        this.post(tick);
    }

    /**
     * Stops the current countdown, if the countdown is already halted, does nothing
     */
    public void stop(){
        isRunning = false;
    }

    /**
     * updates the countdown time
     * @param time The new time the countdown will begin at
     */
    public void setTime(int time){
        this.setText(""+time);
        this.time = time;
    }

    /**
     * Sets the callback class
     * @param tickCallback The class to be called back when this event occurs
     */
    public void setTickCallback(CountdownTickCallback tickCallback){
        this.tickCallback = tickCallback;
    }

    /**
     * Sets the callback class
     * @param finishCallback The class to be called back when this event occurs
     */
    public void setFinishCallback(CountdownFinishCallback finishCallback){
        this.finishCallback = finishCallback;
    }

}
