package com.example.mongrammaire.horisontal_cardv;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import java.util.Objects;
import android.widget.TextView;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.OnBackPressedCallback;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.mongrammaire.DictionnaireFragment;
import com.example.mongrammaire.courslist.cards.favorites.FavoriteListFragment;
import com.example.mongrammaire.HomeFragment;
import com.example.mongrammaire.Quiz.MainGameActivity;
import com.example.mongrammaire.R;
import com.example.mongrammaire.Translatetool;
import com.example.mongrammaire.aboutpage.word;
import com.example.mongrammaire.courslist.cards.favorites.MyAdapter;
import com.example.mongrammaire.courslist.cours;
import com.example.mongrammaire.recherche;
import com.example.mongrammaire.auth.LoginActivity;
import com.example.mongrammaire.TcfFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

public class NAVDRAWER extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //make fragment in the opener
        if (savedInstanceState == null) {
            String targetCategory = getIntent().getStringExtra("target_category");
            boolean startSearch = getIntent().getBooleanExtra("start_search", false);
            
            Fragment newFragment;
            if (targetCategory != null) {
                newFragment = new com.example.mongrammaire.courslist.cours();
                Bundle bundle = new Bundle();
                bundle.putString("selected_category", targetCategory);
                newFragment.setArguments(bundle);
            } else {
                newFragment = new HomeFragment();
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.screen_area, newFragment);
            ft.commit();
            
            if (startSearch) {
                // If we came from Home Search icon, trigger the search intent or UI
                Intent intent = new Intent(this, com.example.mongrammaire.recherche.class);
                startActivity(intent);
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        
        if (getIntent().getBooleanExtra("open_drawer", false)) {
            drawer.openDrawer(androidx.core.view.GravityCompat.START);
        }
        
        updateHeaderProgress(navigationView);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void updateHeaderProgress(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        com.google.android.material.progressindicator.LinearProgressIndicator progressBar = 
                headerView.findViewById(R.id.header_overall_progress);
        TextView subtitle = headerView.findViewById(R.id.tv_header_subtitle);
        
        try (com.example.mongrammaire.Data.Local.LessonDatabaseHelper dbHelper = 
                new com.example.mongrammaire.Data.Local.LessonDatabaseHelper(this)) {
            int overall = dbHelper.getOverallCompletionPercentage();
            
            progressBar.setProgress(overall, true);
            subtitle.setText(getString(R.string.overall_progress, overall));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Translatetool.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_ac) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_home) {
            fragment = new DictionnaireFragment();
        } else if (id == R.id.nav_gallery) {
            fragment = new cours();
        } else if (id == R.id.nav_tcf) {
            fragment = new TcfFragment();
        } else if (id == R.id.nav_slideshow) {
            fragment = new FavoriteListFragment();
        } else if (id == R.id.nav_tools) {
            fragment = new word();
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(NAVDRAWER.this, recherche.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(NAVDRAWER.this, MainGameActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_rate) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        } else if (id == R.id.nav_app_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "Téléchargez " + getString(R.string.app_name) + " sur le Play Store : https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(intent, "Partager via"));
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(NAVDRAWER.this, LoginActivity.class));
            finish();
        }

        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}