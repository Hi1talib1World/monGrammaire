package com.example.mongrammaire;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.mongrammaire.Quiz.MainGameActivity;
import com.example.mongrammaire.horisontal_cardv.NAVDRAWER;

public class home extends AppCompatActivity {


    private boolean viewIsAtHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btm_launcher);




    }

    @Override
    public void onBackPressed() {


            moveTaskToBack(true);

    }

    public void buton (View v){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.cow);
        mp.start();
        Intent intent = new Intent(home.this, NAVDRAWER.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Bienvenue !",Toast.LENGTH_LONG).show();


    }
    public void buttonB (View v){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.cow);
        mp.start();
        Intent intent = new Intent(home.this, MainGameActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Bienvenue !",Toast.LENGTH_LONG).show();


    }
}
