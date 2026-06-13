package com.example.mongrammaire.horisontal_cardv;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.MenuItem;

import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.mongrammaire.Alphabets.acceuil;
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

public class NAVDRAWER extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Adapter adapter;
    private cours.OnFragmentInteractionListener mListener;
    //the recyclerview
    private boolean viewIsAtHome;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private RecyclerView recyclerView;
    private ArrayList<Model> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.play, R.drawable.play,R.drawable.play, R.drawable.play,R.drawable.play,R.drawable.play,R.drawable.play};
    private String[] myImageNameList = new String[]{
            "Sujet", // Subject
            "Verbe", // Verb
            "Complément d'objet direct", // Direct Object
            "Complément d'objet indirect", // Indirect Object
            "Adverbe", // Adverb
            "Préposition", // Preposition
            "Conjonction" // Conjunction
    };


    ProgressBar androidProgressBar;
    int progressStatusCounter = 0;
    TextView textView;
    Handler progressHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //make fragment in the opener
        if (savedInstanceState == null) {
            Fragment newFragment = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.screen_area, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.app_name)
                .setIcon(R.drawable.logo)
                .setMessage("Voulez-vous sortir des cours ?")
                .setCancelable(false)
                .setPositiveButton("OUI", (dialog, id) -> finish())
                .setNegativeButton("NON", (dialog, id) -> dialog.cancel())
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Translatetool.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        if (id == R.id.nav_ac) {
            //home screen
            fragment = new HomeFragment();
        }else if (id == R.id.nav_home) {
            fragment = new acceuil();
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
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(NAVDRAWER.this, LoginActivity.class));
            finish();
        }

        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}