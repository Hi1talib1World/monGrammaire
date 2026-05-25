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
    private String currentCategory = "Tous";
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
                currentCategory = title;
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
            boolean matchesCategory = currentCategory.equals("Tous") || model.getCategory().equals(currentCategory);
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
        models.add(new Model("Accords au pluriel", 
            "Règles d'accord du pluriel", 
            "[RULE] Le pluriel des noms se forme généralement en ajoutant un 's' à la fin du mot. [EXAMPLE] Comment dit-on 'des chats' au singulier ? -> [un chat] [EXCEPTION] Les noms en -au, -eau, -eu prennent un 'x' : un château -> [des châteaux].", 
            R.drawable.n1, "Grammaire", 45));

        models.add(new Model("Prépositions", 
            "Utilisation des prépositions", 
            "[RULE] Les prépositions de lieu servent à situer une personne ou un objet. [EXAMPLE] Je vais ____ Paris -> [à] [EXCEPTION] On utilise 'Dans' pour l'intérieur d'un espace fermé : Dans la boîte.", 
            R.drawable.ufo, "Grammaire", 60));

        models.add(new Model("Adjectifs possessifs", 
            "Mon, ton, son...", 
            "[RULE] Ils s'accordent en genre et en nombre avec le nom possédé. [EXAMPLE] C'est la voiture de Marie. C'est ____ voiture -> [sa] [EXCEPTION] Devant une voyelle, ma/ta/sa deviennent mon/ton/son. Ex: Mon amie.", 
            R.drawable.n2, "Grammaire", 30));

        models.add(new Model("Articles", 
            "Définis, indéfinis, partitifs", 
            "[RULE] Il existe trois types d'articles en français. [EXAMPLE] Je mange ____ pain -> [du] [EXCEPTION] Note sur l'élision : 'le' ou 'la' deviennent 'l'' devant une voyelle. Ex: l'oiseau.", 
            R.drawable.satellite, "Grammaire", 100));

        models.add(new Model("Négation", 
            "Ne... pas, ne... plus...", 
            "[RULE] La structure de base est : Ne + Verbe + Pas. [EXAMPLE] Je ____ mange ____ -> [ne / pas] [EXCEPTION] Autres formes : Ne... plus (arrêt), Ne... jamais (fréquence nulle), Ne... rien (objet nul).", 
            R.drawable.n3, "Grammaire", 15));

        // Verbe Category
        models.add(new Model("Passé composé", 
            "Avec avoir et être", 
            "[RULE] Se forme avec l'auxiliaire au présent + Participe Passé. [EXAMPLE] Hier, j'____ (manger) une pomme -> [ai mangé] [EXCEPTION] Auxiliaire ÊTRE pour les 14 verbes de mouvement (aller, venir...) et les verbes pronominaux. Elle est partie.", 
            R.drawable.n7, "Verbe", 50));

        models.add(new Model("Imparfait", 
            "Description et habitude", 
            "[RULE] Radical de 'nous' au présent + terminaisons -ais, -ais, -ait, -ions, -iez, -aient. [EXAMPLE] Quand j'étais petit, je ____ (jouer) au parc -> [jouais] [EXCEPTION] L'imparfait exprime une action longue ou répétée dans le passé.", 
            R.drawable.n8, "Verbe", 40));

        models.add(new Model("Futur simple", 
            "Actions à venir", 
            "[RULE] Formation : Infinitif + terminaisons -ai, -as, -a, -ons, -ez, -ont. [EXAMPLE] Demain, nous ____ (finir) le projet -> [finirons] [EXCEPTION] Radicaux irréguliers : être (ser-), avoir (aur-), aller (ir-).",
            R.drawable.n2, "Verbe", 90));

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
