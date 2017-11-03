package com.onetallprogrammer.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final private int MAX_SECONDS = 600;
    final private int INITIAL_SECONDS = 30;
    final private int MILLISECONDS_IN_SECOND = 1000;
    final private int SECONDS_IN_MINUTE = 60;

    private CountDownTimer timer;
    private boolean timerRunning = false;
    private SeekBar setTimeSeekBar;
    private TextView timeDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeDisplay = (TextView) findViewById(R.id.timeDisplay);

        setTimeSeekBar = (SeekBar) findViewById(R.id.setTimeSeekBar);
        setTimeSeekBar.setMax(MAX_SECONDS);
        setTimeSeekBar.setProgress(INITIAL_SECONDS);

        timeDisplay.setText(formatTime(setTimeSeekBar.getProgress()));

        setTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                timeDisplay.setText(formatTime(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * turns timer on and off (callback)
     *
     * @param view the button being pressed
     */
    public void toggleTimer(View view) {
        timerRunning = !timerRunning;

        if (timerRunning) {
            startTimer();
        } else {
            stopTimer();
        }

        updateToggleButtonText();
    }
    
    /**
     * Formats given milliseconds into a minutes : seconds string
     *
     * @param seconds time being formatted
     * @return formatted string
     */
    private String formatTime(int seconds) {
        int minutes = seconds / SECONDS_IN_MINUTE;
        seconds = seconds % SECONDS_IN_MINUTE;

        String minutesString;
        String secondsString;

        minutesString = String.valueOf(minutes);

        if (seconds < 10) {
            secondsString = "0" + String.valueOf(seconds);
        } else {
            secondsString = String.valueOf(seconds);
        }

        return minutesString + ":" + secondsString;
    }

    /**
     * manages what happens when timer is started
     */
    private void startTimer() {
        setTimeSeekBar.setEnabled(false);

        // 50 milliseconds added to give thread time to process it's onTick method
        timer = new CountDownTimer(MILLISECONDS_IN_SECOND * setTimeSeekBar.getProgress() + 50,
                MILLISECONDS_IN_SECOND) {
            @Override
            public void onTick(long millisRemaining) {
                int secondsOnTimer = (int) (millisRemaining / MILLISECONDS_IN_SECOND);
                timeDisplay.setText(formatTime(secondsOnTimer));
            }

            @Override
            public void onFinish() {
                resetTimer();
                MediaPlayer.create(getApplicationContext(), R.raw.rooster).start();
            }
        }.start();
    }

    /**
     * manages what happens when timer is stopped
     */
    private void stopTimer() {
        setTimeSeekBar.setEnabled(true);

        timer.cancel();
    }

    /**
     * resets the timer to its initial parameters
     */
    private void resetTimer() {
        timeDisplay.setText(formatTime(INITIAL_SECONDS));
        setTimeSeekBar.setProgress(INITIAL_SECONDS);
        setTimeSeekBar.setEnabled(true);
        updateToggleButtonText();
    }

    /**
     * updates text displayed by timer activating button
     */
    private void updateToggleButtonText() {
        Button timerToggleButton = (Button) findViewById(R.id.timerToggleButton);
        if (timerRunning) {
            timerToggleButton.setText(R.string.toggle_button_stop);
        } else {
            timerToggleButton.setText(R.string.toggle_button_start);
        }
    }
}
