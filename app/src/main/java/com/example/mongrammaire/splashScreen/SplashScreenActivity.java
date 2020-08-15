package com.example.mongrammaire.splashScreen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mongrammaire.R;
import com.example.mongrammaire.horisontal_cardv.NAVDRAWER;

public class SplashScreenActivity extends AppCompatActivity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_splash_screen);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreenActivity.this, NAVDRAWER.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}