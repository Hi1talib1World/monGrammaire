package com.example.mongrammaire;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.courslist.cards.favorites.MyAdapter;
import com.example.mongrammaire.courslist.cours;

public class recherche extends AppCompatActivity {
    public MyAdapter adapter;
    private cours.OnFragmentInteractionListener mListener;
    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navdrawer, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(item);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                //adapter.getFilter().filter(newText);
                return false;

            }
        });
        return true;
    }
}
