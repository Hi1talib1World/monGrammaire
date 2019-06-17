package com.example.mongrammaire;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
        if (!viewIsAtHome) { //if the current view is not the News fragment
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.home); //display the News fragment
        } else {
            moveTaskToBack(true);  //If view is in News fragment, exit application
        }
    }

    public void buton (View v){
        Intent intent = new Intent(btm_launcher.this, NAVDRAWER.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Bienvenue !",Toast.LENGTH_LONG).show();
    }
}
