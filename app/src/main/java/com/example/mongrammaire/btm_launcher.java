package com.example.mongrammaire;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class btm_launcher extends AppCompatActivity {


    private boolean viewIsAtHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btm_launcher);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch (itemId){
                    case R.id.action_recents:
                        addFragment(new HomeFragment());
                        viewIsAtHome = true;
                        break;
                    case R.id.action_favorites:
                        addFragment(new BlankFragment1());
                        viewIsAtHome = false;
                        break;
                    case R.id.action_nearby:
                        addFragment(new BlankFragment2());
                        viewIsAtHome = false;
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    private void addFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(btm_launcher.this);
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
        Intent intent = new Intent(btm_launcher.this, NAVDRAWER.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Bienvenue !",Toast.LENGTH_LONG).show();


    }
    public void buttonB (View v){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.cow);
        mp.start();
        Intent intent = new Intent(btm_launcher.this, MainGameActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Bienvenue !",Toast.LENGTH_LONG).show();


    }
}
