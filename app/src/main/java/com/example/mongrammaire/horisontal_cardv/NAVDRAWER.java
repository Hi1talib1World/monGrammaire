package com.example.mongrammaire.horisontal_cardv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.mongrammaire.Alphabets.acceuil;
import com.example.mongrammaire.Exsercises;
import com.example.mongrammaire.HomeFragment;
import com.example.mongrammaire.Quiz.MainGameActivity;
import com.example.mongrammaire.R;
import com.example.mongrammaire.Translatetool;
import com.example.mongrammaire.aboutpage.word;
import com.example.mongrammaire.cards.MyAdapter;
import com.example.mongrammaire.courslist.cours;
import com.example.mongrammaire.horisontal_cardv.Adapter;
import com.example.mongrammaire.horisontal_cardv.Model;
import com.example.mongrammaire.recherche;

import java.util.ArrayList;

public class NAVDRAWER extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public MyAdapter adapter;
    private cours.OnFragmentInteractionListener mListener;
    //the recyclerview

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private RecyclerView recyclerView;
    private ArrayList<Model> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.house, R.drawable.house,R.drawable.house, R.drawable.house,R.drawable.house,R.drawable.house,R.drawable.house};
    private String[] myImageNameList = new String[]{"Apple","Mango" ,"Strawberry","Pineapple","Orange","Blueberry","Watermelon"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mon Grammaire");
        if (savedInstanceState == null) {
            Fragment newFragment = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.screen_area, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        } else if (id == R.id.nav_slideshow) {
            fragment = new Exsercises();
        } else if (id == R.id.nav_tools) {
            fragment = new word();
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(NAVDRAWER.this, recherche.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(NAVDRAWER.this, MainGameActivity.class);
            startActivity(intent);
        }

        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area, fragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}