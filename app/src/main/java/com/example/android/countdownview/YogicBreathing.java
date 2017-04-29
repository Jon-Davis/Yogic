package com.example.android.countdownview;

import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.countdownview.Callbacks.CountdownFinishCallback;
import com.example.android.countdownview.views.CountdownView;

import java.util.Locale;

public class YogicBreathing extends AppCompatActivity implements CountdownFinishCallback {
    //4 inhale, 4 hold, 2 inhale, 6 exhale 2 hold, 2 exhale
    private static final int FOUR_SECONDS_BREATH_IN = 0, FOUR_SECONDS_HOLD = 1,
            TWO_SECONDS_INHALE = 2, SIX_SECONDS_EXHALE = 3, TWO_SECONDS_HOLD = 4,
            TWO_SECONDS_BREATH_OUT_2 = 5;
    private int currentState;
    private CountdownView countdownView;
    private TextView header;
    private Button startButton;
    private boolean hasStarted = false;
    private TextToSpeech speech;

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
                speech.speak("Inhale",TextToSpeech.QUEUE_FLUSH,null,null);
                countdownView.setTime(4);
                countdownView.start();
                header.setText("Inhale");
                hasStarted = true;
                startButton.setText("Restart");
            } else {
                countdownView.setTime(0);
                countdownView.stop();
                header.setText("When you are ready to begin, press start.");
                hasStarted = false;
                startButton.setText("Start");
            }

        });
        speech =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.UK);
                    speech.setSpeechRate((float) 0.9);
                    speech.setPitch((float) 1.1);
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Yogic Breathing\n\nIn this section, you will be asked to perform a series " +
                "of breathing exercises. When the screen prompts you to 'Inhale', please breath in for" +
                " the entire duration of the on screen clock. When the screen prompts you to 'Hold'," +
                " please hold your breath for the entire duration of the on screen clock. Finally " +
                "when prompted to 'Exhale', please exhale for the entire duration of the on screen clock" +
                "\n\nThe pattern will be as followed.\n\nInhale for 4 seconds\n\nHold for 4 seconds\n\n" +
                "Inhale for 2 seconds\n\nExhale for 6 seconds, Hold for 2 seconds\n\nExhale for 2 seconds" +
                "\n\nYou may restart the activity at any time. To begin the exersice, press the start" +
                " button, then follow the on screen prompts")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        //comment
        currentState = FOUR_SECONDS_BREATH_IN;
    }

    @Override
    public void callback() {
        switch (currentState){
            case FOUR_SECONDS_BREATH_IN:
                speech.speak("Hold",TextToSpeech.QUEUE_FLUSH,null,null);
                countdownView.setTime(4);
                countdownView.start();
                header.setText("Hold");
                currentState = FOUR_SECONDS_HOLD;
                break;
            case FOUR_SECONDS_HOLD:
                speech.speak("Inhale",TextToSpeech.QUEUE_FLUSH,null,null);
                countdownView.setTime(2);
                countdownView.start();
                header.setText("Inhale");
                currentState = TWO_SECONDS_INHALE;
                break;
            case TWO_SECONDS_INHALE:
                speech.speak("Exhale",TextToSpeech.QUEUE_FLUSH,null,null);
                countdownView.setTime(6);
                countdownView.start();
                header.setText("Exhale");
                currentState = SIX_SECONDS_EXHALE;
                break;
            case SIX_SECONDS_EXHALE:
                speech.speak("Hold",TextToSpeech.QUEUE_FLUSH,null,null);
                countdownView.setTime(2);
                countdownView.start();
                header.setText("Hold");
                currentState = TWO_SECONDS_HOLD;
                break;
            case TWO_SECONDS_HOLD:
                speech.speak("Exhale",TextToSpeech.QUEUE_FLUSH,null,null);
                countdownView.setTime(2);
                countdownView.start();
                header.setText("Exhale");
                currentState = TWO_SECONDS_BREATH_OUT_2;
                break;
            case TWO_SECONDS_BREATH_OUT_2:
                header.setText("Very good, on to the next exercise.");
                break;
        }
    }
}
