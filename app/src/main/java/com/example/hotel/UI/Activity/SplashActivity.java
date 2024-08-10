package com.example.hotel.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel.MainActivity;
import com.example.hotel.R;
import com.google.android.material.textview.MaterialTextView;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {


    private ProgressBar LoadingProgress;
    private MaterialTextView TxtLoading, TxtVersionNo;
    private Timer timer;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LoadingProgress = findViewById(R.id.LoadingProgress);
        TxtLoading = findViewById(R.id.TxtLoading);
        TxtVersionNo = findViewById(R.id.VersionNo);
        LoadingProgress.setProgress(0);
        TxtVersionNo.setText("1.0.1");
        TxtLoading.setVisibility(View.VISIBLE);
        TxtLoading.setText("");

        final long period = 40;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //this repeats every 100 ms
                if (i < 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TxtLoading.setText(String.valueOf(i) + "%");
                        }
                    });
                    LoadingProgress.setProgress(i);
                    i++;

                } else {
                    //closing the timer
                    timer.cancel();
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    // close this activity
                    finish();
                }
            }
        }, 0, period);
    }
}