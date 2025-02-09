package com.example.stopwatchapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Timer libraries
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView stopWatchLabel;
    private Timer timer;
    private int min;
    private int sec;
    private int frac;
    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references of textview and buttons
        stopWatchLabel = (TextView) findViewById(R.id.stopWatchLabel);
        Button startButton = (Button) findViewById(R.id.startButton);
        Button stopButton = (Button) findViewById(R.id.stopButton);
        Button resetButton = (Button) findViewById(R.id.resetButton);

        // Create timer
        timer = new Timer();

        // Add listeners for buttons
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

    }

    private void startTimer(){
        // Checks if timer is already running so timer task is not repeated
        if (!running) {
            running = true;
            // Change text color to black
            stopWatchLabel.setTextColor(getResources().getColor(R.color.black, getTheme()));
            // Increase time
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    frac++;
                    if (frac >= 100) {
                        sec++;
                        frac = 0;
                        if (sec >= 60) {
                            min++;
                            sec = 0;
                        }
                    }
                    // Updates label constantly
                    stopWatchLabel.setText(String.format("%02d", min) + ":" + String.format("%02d", sec) + "." + String.format("%02d", frac));
                }
            }, 0, 10);
        }
    }

    private void stopTimer(){
        // Checks if running so no need to stop a non-started timer
        if (running) {
            // Sets color to red
            stopWatchLabel.setTextColor(getResources().getColor(R.color.red, getTheme()));
            // Resets running value
            running = false;
            // Cancels timer to stop current timer
            // and replaces it with a new timer so it can be started again
            timer.cancel();
            timer = new Timer();
        }
    }

    private void resetTimer(){
        // Sets color to black
        stopWatchLabel.setTextColor(getResources().getColor(R.color.black, getTheme()));
        // Resets running value
        running = false;
        // Resets value
        min = 0;
        sec = 0;
        frac = 0;
        // Cancels timer to stop current timer
        // and replaces it with a new timer so it can be started again
        timer.cancel();
        timer = new Timer();
        // Sets timer back to default
        stopWatchLabel.setText(String.format("%02d", min) + ":" + String.format("%02d", sec) + "." + String.format("%02d", frac));
    }
}