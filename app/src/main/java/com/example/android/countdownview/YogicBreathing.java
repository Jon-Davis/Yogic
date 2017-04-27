package com.example.android.countdownview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.countdownview.Callbacks.CountdownFinishCallback;
import com.example.android.countdownview.views.CountdownView;

public class YogicBreathing extends AppCompatActivity implements CountdownFinishCallback {
    private static final int FOUR_SECONDS_BREATH_IN = 0, SIX_SECONDS_BREATH_OUT = 1,
            FOUR_SECONDS_BREATH_OUT = 2, TWO_SECONDS_BREATH_OUT = 3;
    private int currentState;
    private CountdownView countdownView;
    private TextView header;
    private Button startButton;
    private boolean hasStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_view);

        //sets up the views
        countdownView = (CountdownView) this.findViewById(R.id.Yogic_Countdown);
        countdownView.setFinishCallback(this);
        header = (TextView) this.findViewById(R.id.Yogic_Header);
        startButton = (Button) this.findViewById(R.id.Yogic_Start);
        startButton.setOnClickListener(e ->{
            if(!hasStarted) {
                countdownView.setTime(4);
                countdownView.start();
                header.setText("Breath In");
            }
            hasStarted = true;
        });
        //comment
        currentState = FOUR_SECONDS_BREATH_IN;
    }

    @Override
    public void callback() {
        switch (currentState){
            case FOUR_SECONDS_BREATH_IN:
                countdownView.setTime(6);
                countdownView.start();
                header.setText("Breath Out.");
                currentState = SIX_SECONDS_BREATH_OUT;
                break;
            case SIX_SECONDS_BREATH_OUT:
                countdownView.setTime(4);
                countdownView.start();
                header.setText("Breath In.");
                currentState = FOUR_SECONDS_BREATH_OUT;
                break;
            case FOUR_SECONDS_BREATH_OUT:
                countdownView.setTime(2);
                countdownView.start();
                header.setText("Continue to Breath In.");
                currentState = TWO_SECONDS_BREATH_OUT;
                break;
            case TWO_SECONDS_BREATH_OUT:
                header.setText("Very good, on to the next exercise.");
                currentState = SIX_SECONDS_BREATH_OUT;
                break;
        }
    }
}
