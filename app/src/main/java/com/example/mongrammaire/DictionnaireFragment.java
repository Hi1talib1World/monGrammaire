package com.example.mongrammaire;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DictionnaireFragment extends Fragment {
    private DictionaryAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyState;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;

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
        progressBar = view.findViewById(R.id.dictionary_progress_bar);
        SearchView searchBar = view.findViewById(R.id.search_bar);

        requestQueue = Volley.newRequestQueue(requireContext());

        setupRecyclerView();
        loadInitialWords();

        searchBar.setQueryHint("Rechercher un mot...");
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchWord(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadInitialWords();
                    emptyState.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }

    private void loadInitialWords() {
        List<DictionaryWord> initialWords = new ArrayList<>();
        initialWords.add(new DictionaryWord("Grammaire", "Ensemble des règles qui régissent une langue.", "nom"));
        initialWords.add(new DictionaryWord("Verbe", "Mot qui exprime une action ou un état.", "nom"));
        initialWords.add(new DictionaryWord("Adjectif", "Mot qui qualifie un nom.", "nom"));
        initialWords.add(new DictionaryWord("Apprendre", "Acquérir la connaissance de quelque chose par l'étude ou l'expérience.", "verbe"));
        initialWords.add(new DictionaryWord("Français", "Langue romane parlée en France et dans de nombreux pays.", "nom/adjectif"));
        adapter.updateList(initialWords);
    }

    private void setupRecyclerView() {
        adapter = new DictionaryAdapter(new ArrayList<>(), this::showWordDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void showWordDetails(DictionaryWord word) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(word.getWord())
                .setMessage(word.getDefinition())
                .setPositiveButton("Fermer", null)
                .show();
    }

    private void searchWord(String word) {
        if (word == null || word.trim().isEmpty()) return;

        String searchWord = word.trim();

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyState.setVisibility(View.GONE);

        // Removed exintro=1 as it is often empty for Wiktionary entries.
        // Added exsentences=5 to get a concise summary instead.
        String url = "https://fr.wiktionary.org/w/api.php?action=query&format=json&prop=extracts&explaintext=1&exsentences=5&redirects=1&titles=" + searchWord;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        if (!response.has("query")) {
                            showEmptyState();
                            return;
                        }
                        JSONObject query = response.getJSONObject("query");
                        JSONObject pages = query.getJSONObject("pages");
                        Iterator<String> keys = pages.keys();
                        
                        List<DictionaryWord> results = new ArrayList<>();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            if (key.equals("-1")) continue; // Word not found

                            JSONObject page = pages.getJSONObject(key);
                            String title = page.getString("title");
                            String extract = "";
                            
                            if (page.has("extract")) {
                                extract = page.getString("extract").trim();
                            }
                            
                            if (extract.isEmpty()) {
                                // Fallback: if extract is empty, maybe it's just a title
                                extract = "Aucune définition courte disponible. Cliquez pour voir plus de détails sur Wiktionary.";
                            }

                            if (extract.length() > 600) {
                                extract = extract.substring(0, 600) + "...";
                                int lastSpace = extract.lastIndexOf(' ');
                                if (lastSpace > 500) extract = extract.substring(0, lastSpace) + "...";
                            }
                            results.add(new DictionaryWord(title, extract, "Définition"));
                        }

                        if (results.isEmpty()) {
                            showEmptyState();
                        } else {
                            showResults(results);
                        }
                    } catch (JSONException e) {
                        Log.e("Dictionary", "JSON Error", e);
                        showEmptyState();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Dictionary", "Volley Error", error);
                    showEmptyState();
                });

        requestQueue.add(request);
    }

    private void showResults(List<DictionaryWord> results) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.GONE);
        adapter.updateList(results);
    }

    private void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        emptyState.setVisibility(View.VISIBLE);
    }
}
