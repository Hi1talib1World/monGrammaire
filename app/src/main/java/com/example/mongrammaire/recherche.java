package com.example.mongrammaire;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.Data.Local.LessonDatabaseHelper;
import com.example.mongrammaire.Model.LessonModel;
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
        LessonDatabaseHelper dbHelper = new LessonDatabaseHelper(this);
        List<LessonModel> lessons = dbHelper.getAllLessons();

        for (LessonModel lesson : lessons) {
            // Map LessonDatabaseHelper model to Search/Favorites adapter model
            // Defaulting some UI fields like heart and progress for search
            allLessons.add(new Model(
                    lesson.getId(),
                    lesson.getTitle(),
                    lesson.getDescription(),
                    lesson.getContent(),
                    R.drawable.logo, // Default icon for unified search
                    lesson.getCategory(),
                    0 // progress
            ));
        }
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
