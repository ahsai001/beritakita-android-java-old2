package com.ahsailabs.beritakita;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;

import com.ahsailabs.beritakita.bases.BaseApp;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //cara 1 : Handler delay
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.start(SplashActivity.this);
                finish();
            }
        }, 3000);*/

        //cara 2 :
        new CountDownTimer(3000, 1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                MainActivity.start(SplashActivity.this);
                finish();
            }
        }.start();

        ((BaseApp)getApplication()).data = "ini data baru dari splash";
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}