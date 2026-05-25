package com.example.mongrammaire;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.courslist.cards.favorites.Model;
import com.example.mongrammaire.courslist.cards.favorites.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class recherche extends AppCompatActivity {
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyState;
    private List<Model> allLessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        recyclerView = findViewById(R.id.recycler_view_results);
        emptyState = findViewById(R.id.empty_search_state);
        SearchView searchBar = findViewById(R.id.search_bar);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        setupData();
        setupRecyclerView();

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void setupData() {
        allLessons = new ArrayList<>();
        // These should ideally come from a common repository/database
        allLessons.add(new Model("Accords au pluriel", "Règles d'accord du pluriel", "...", R.drawable.n1, "Grammaire", 45));
        allLessons.add(new Model("Prépositions", "Utilisation des prépositions", "...", R.drawable.ufo, "Grammaire", 60));
        allLessons.add(new Model("Adjectifs possessifs", "Mon, ton, son...", "...", R.drawable.n2, "Grammaire", 30));
        allLessons.add(new Model("Articles", "Définis, indéfinis, partitifs", "...", R.drawable.satellite, "Grammaire", 100));
        allLessons.add(new Model("Négation", "Ne... pas, ne... plus...", "...", R.drawable.n3, "Grammaire", 15));
        allLessons.add(new Model("Passé composé", "Avec avoir et être", "...", R.drawable.n7, "Verbe", 50));
        allLessons.add(new Model("Imparfait", "Description et habitude", "...", R.drawable.n8, "Verbe", 40));
        allLessons.add(new Model("Futur simple", "Actions à venir", "...", R.drawable.n2, "Verbe", 90));
        allLessons.add(new Model("Impératif", "Donner des ordres", "...", R.drawable.n6, "Verbe", 80));
        allLessons.add(new Model("Verbes pronominaux", "Se laver, se lever...", "...", R.drawable.n5, "Verbe", 75));
    }

    private void setupRecyclerView() {
        adapter = new MyAdapter(new ArrayList<>(allLessons));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void filter(String text) {
        List<Model> filteredList = new ArrayList<>();
        for (Model model : allLessons) {
            if (model.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                model.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                model.getCategory().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(model);
            }
        }

        if (filteredList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
            adapter.updateList(filteredList);
        }
    }
}
