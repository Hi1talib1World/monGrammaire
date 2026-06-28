package com.example.mongrammaire;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.courslist.cards.favorites.Model;
import com.example.mongrammaire.courslist.cards.favorites.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class DictionnaireFragment extends Fragment {
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyState;
    private List<Model> allLessons;

    public DictionnaireFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dictionnaire, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_results);
        emptyState = view.findViewById(R.id.empty_search_state);
        SearchView searchBar = view.findViewById(R.id.search_bar);
        View btnMenu = view.findViewById(R.id.btnMenu);

        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (getActivity() != null) {
                    DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                    if (drawer != null) {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            });
        }

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
        // Using same data as recherche.java for consistency
        allLessons.add(new Model(1, "Accords au pluriel", "Règles d'accord du pluriel", "...", R.drawable.n1, "Grammaire", 45));
        allLessons.add(new Model(2, "Prépositions", "Utilisation des prépositions", "...", R.drawable.ufo, "Grammaire", 60));
        allLessons.add(new Model(3, "Adjectifs possessifs", "Mon, ton, son...", "...", R.drawable.n2, "Grammaire", 30));
        allLessons.add(new Model(4, "Articles", "Définis, indéfinis, partitifs", "...", R.drawable.satellite, "Grammaire", 100));
        allLessons.add(new Model(5, "Négation", "Ne... pas, ne... plus...", "...", R.drawable.n3, "Grammaire", 15));
        allLessons.add(new Model(6, "Passé composé", "Avec avoir et être", "...", R.drawable.n7, "Verbe", 50));
        allLessons.add(new Model(7, "Imparfait", "Description et habitude", "...", R.drawable.n8, "Verbe", 40));
        allLessons.add(new Model(8, "Futur simple", "Actions à venir", "...", R.drawable.n2, "Verbe", 90));
        allLessons.add(new Model(9, "Impératif", "Donner des ordres", "...", R.drawable.n6, "Verbe", 80));
        allLessons.add(new Model(10, "Verbes pronominaux", "Se laver, se lever...", "...", R.drawable.n5, "Verbe", 75));
    }

    private void setupRecyclerView() {
        adapter = new MyAdapter(new ArrayList<>(allLessons));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
