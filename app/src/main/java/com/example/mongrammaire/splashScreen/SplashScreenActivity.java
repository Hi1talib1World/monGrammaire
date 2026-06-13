package com.example.mongrammaire.splashScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.R;
import com.example.mongrammaire.home;
import com.example.mongrammaire.onboarding.OnboardingActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String PREF_NAME = "onboarding_prefs";
    private static final String KEY_IS_FIRST_RUN_COMPLETED = "is_first_run_completed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            boolean isFirstRunCompleted = prefs.getBoolean(KEY_IS_FIRST_RUN_COMPLETED, false);

            Intent intent;
            if (isFirstRunCompleted) {
                intent = new Intent(SplashScreenActivity.this, home.class);
            } else {
                intent = new Intent(SplashScreenActivity.this, OnboardingActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}
