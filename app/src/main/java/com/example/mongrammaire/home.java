package com.example.mongrammaire;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.Quiz.MainGameActivity;
import com.example.mongrammaire.Utils.ToastHelper;
import com.example.mongrammaire.databinding.ActivityBtmLauncherBinding;
import com.example.mongrammaire.horisontal_cardv.NAVDRAWER;

/**
 * Main entry activity for the Mon Grammaire application.
 * This activity provides the initial landing screen with options to navigate 
 * to the main navigation drawer or start the quiz game.
 */
public class home extends AppCompatActivity {

    private ActivityBtmLauncherBinding binding;
    private SoundPool soundPool;
    private int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Use View Binding
        binding = ActivityBtmLauncherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize SoundPool for efficient sound effects
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
        soundId = soundPool.load(this, R.raw.cow, 1);

        // Set click listeners using binding
        binding.buttonA.setOnClickListener(this::onButtonAClicked);
        binding.buttonB.setOnClickListener(this::onButtonBClicked);
        binding.btnHomeSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.mongrammaire.horisontal_cardv.NAVDRAWER.class);
            intent.putExtra("start_search", true);
            startActivity(intent);
        });

        startAnimations();
    }

    private void startAnimations() {
        // Initial state
        binding.logo.setAlpha(0f);
        binding.logo.setTranslationY(-50f);
        binding.title.setAlpha(0f);
        binding.title.setTranslationY(30f);
        binding.subtitle.setAlpha(0f);
        binding.subtitle.setTranslationY(30f);
        binding.buttonA.setAlpha(0f);
        binding.buttonA.setTranslationY(50f);
        binding.buttonB.setAlpha(0f);
        binding.buttonB.setTranslationY(50f);

        // Animate
        binding.logo.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(200).start();
        binding.title.animate().alpha(1f).translationY(0f).setDuration(700).setStartDelay(400).start();
        binding.subtitle.animate().alpha(1f).translationY(0f).setDuration(700).setStartDelay(500).start();
        binding.buttonA.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(700).start();
        binding.buttonB.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(850).start();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void onButtonAClicked(View v) {
        playSound();
        Intent intent = new Intent(home.this, NAVDRAWER.class);
        intent.putExtra("target_category", "Grammaire");
        startActivity(intent);
    }

    private void onButtonBClicked(View v) {
        playSound();
        Intent intent = new Intent(home.this, NAVDRAWER.class);
        intent.putExtra("target_category", "Verbe");
        startActivity(intent);
    }

    private void playSound() {
        if (soundId != 0) {
            soundPool.play(soundId, 1, 1, 0, 0, 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        binding = null; // Avoid memory leaks
    }
}
