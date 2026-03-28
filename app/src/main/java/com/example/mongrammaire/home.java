package com.example.mongrammaire;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.Quiz.MainGameActivity;
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
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void onButtonAClicked(View v) {
        playSound();
        Intent intent = new Intent(home.this, NAVDRAWER.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Bienvenue !", Toast.LENGTH_LONG).show();
    }

    private void onButtonBClicked(View v) {
        playSound();
        Intent intent = new Intent(home.this, MainGameActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Bienvenue !", Toast.LENGTH_LONG).show();
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
