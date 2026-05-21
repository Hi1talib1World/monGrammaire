package com.example.mongrammaire.courslist;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.Utils.ToastHelper;
import com.example.mongrammaire.courslist.cards.favorites.Model;

import com.example.mongrammaire.R;
import com.example.mongrammaire.courslist.cards.favorites.MyAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class cours extends Fragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    public MyAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private List<Model> allModels;
    private String currentCategory = "all";
    private String currentSearchQuery = "";
    RecyclerView recyclerView;

    public cours() {
        // Required empty public constructor
    }

    public static cours newInstance() {
        return new cours();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cours, container, false);

        recyclerView = v.findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);

        allModels = getPlayers();
        adapter = new MyAdapter(new ArrayList<>(allModels));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        v.findViewById(R.id.btn_back).setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        MaterialButton btnFilter = v.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(requireContext(), view);
            popup.getMenu().add("Tous");
            popup.getMenu().add("Grammaire");
            popup.getMenu().add("Verbe");
            
            popup.setOnMenuItemClickListener(item -> {
                String title = item.getTitle().toString();
                btnFilter.setText(title);
                currentCategory = title.equals("Tous") ? "all" : title;
                applyFilters();
                return true;
            });
            popup.show();
        });

        SearchView searchView = v.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        return v;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        currentSearchQuery = newText;
        applyFilters();
        return true;
    }

    private void applyFilters() {
        List<Model> filteredList = new ArrayList<>();
        for (Model model : allModels) {
            boolean matchesCategory = currentCategory.equals("all") || model.getCategory().equals(currentCategory);
            boolean matchesSearch = model.getTitle().toLowerCase().contains(currentSearchQuery.toLowerCase());
            
            if (matchesCategory && matchesSearch) {
                filteredList.add(model);
            }
        }
        adapter.updateList(filteredList);
    }

    private ArrayList<Model> getPlayers(){
        ArrayList<Model> models = new ArrayList<>();

        // Grammaire Category
        models.add(new Model("Accords au pluriel", "Règles d'accord du pluriel", R.drawable.n1, 0, "Grammaire", 45));
        models.add(new Model("Prépositions", "Utilisation des prépositions", R.drawable.ufo, 0, "Grammaire", 60));
        models.add(new Model("Adjectifs possessifs", "Mon, ton, son...", R.drawable.n2, 0, "Grammaire", 30));
        models.add(new Model("Articles", "Définis, indéfinis, partitifs", R.drawable.satellite, 0, "Grammaire", 100));
        models.add(new Model("Négation", "Ne... pas, ne... plus...", R.drawable.n3, 0, "Grammaire", 15));
        models.add(new Model("Phrases conditionnelles", "Si j'avais...", R.drawable.star, 0, "Grammaire", 0));
        models.add(new Model("Pronoms relatifs", "Qui, que, dont, où", R.drawable.n4, 0, "Grammaire", 0));
        models.add(new Model("Discours rapporté", "Il a dit que...", R.drawable.house, 0, "Grammaire", 0));
        models.add(new Model("Les Adverbes", "Modificateurs de verbes et adjectifs", R.drawable.n5, 0, "Grammaire", 20));
        models.add(new Model("Comparatifs & Superlatifs", "Plus que, moins que, le meilleur", R.drawable.star, 0, "Grammaire", 10));

        // Verbe Category
        models.add(new Model("Impératif", "Donner des ordres", R.drawable.n6, 0, "Verbe", 80));
        models.add(new Model("Passé composé", "Le passé composé avec avoir et être", R.drawable.n7, 0, "Verbe", 50));
        models.add(new Model("Imparfait", "L'imparfait de l'indicatif", R.drawable.n8, 0, "Verbe", 40));
        models.add(new Model("Temps du passé", "Comparaison des temps du passé", R.drawable.logo, 0, "Verbe", 10));
        models.add(new Model("Le Passif", "La voix passive", R.drawable.ufo, 0, "Verbe", 0));
        models.add(new Model("Subjonctif présent", "Exprimer le doute ou le souhait", R.drawable.n1, 0, "Verbe", 5));
        models.add(new Model("Futur simple", "Actions à venir", R.drawable.n2, 0, "Verbe", 90));
        models.add(new Model("Conditionnel présent", "Le mode de l'imaginaire", R.drawable.n3, 0, "Verbe", 0));
        models.add(new Model("Plus-que-parfait", "L'antériorité dans le passé", R.drawable.n4, 0, "Verbe", 0));
        models.add(new Model("Verbes pronominaux", "Se laver, se lever...", R.drawable.n5, 0, "Verbe", 75));

        return models;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings){
            ToastHelper.showCustomToast(getContext(), "Paramètres");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
