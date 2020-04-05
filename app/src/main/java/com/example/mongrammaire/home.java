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

public class home extends AppCompatActivity {


    private boolean viewIsAtHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btm_launcher);




    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Voulez-vous sortir?")
                .setCancelable(false)
                .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        if (!viewIsAtHome) { //if the current view is not the News fragment
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.home); //display the News fragment
        } else {
            moveTaskToBack(true);
        }
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
