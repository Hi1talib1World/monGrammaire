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
            "Le pluriel des noms se forme généralement en ajoutant un 's' à la fin du mot. Ex: un chat -> des chats.\n\n" +
            "Exceptions notables :\n" +
            "- Les noms en -s, -x, -z ne changent pas : un nez -> des nez.\n" +
            "- Les noms en -au, -eau, -eu prennent un 'x' : un château -> des châteaux.\n" +
            "- Les noms en -al deviennent -aux : un cheval -> des chevaux.", 
            R.drawable.n1, "Grammaire", 45));

        models.add(new Model("Prépositions", 
            "Utilisation des prépositions", 
            "Les prépositions de lieu :\n" +
            "- 'À' pour une ville ou un point précis : Je vais à Paris.\n" +
            "- 'En' pour un pays féminin : Je vais en France.\n" +
            "- 'Au' pour un pays masculin : Je vais au Japon.\n" +
            "- 'Dans' pour l'intérieur : Dans la boîte.", 
            R.drawable.ufo, "Grammaire", 60));

        models.add(new Model("Adjectifs possessifs", 
            "Mon, ton, son...", 
            "Ils s'accordent en genre et en nombre avec le nom possédé :\n\n" +
            "Masculin : Mon, Ton, Son, Notre, Votre, Leur.\n" +
            "Féminin : Ma, Ta, Sa, Notre, Votre, Leur.\n" +
            "Pluriel : Mes, Tes, Ses, Nos, Vos, Leurs.\n\n" +
            "Attention : devant une voyelle, ma/ta/sa deviennent mon/ton/son. Ex: Mon amie.", 
            R.drawable.n2, "Grammaire", 30));

        models.add(new Model("Articles", 
            "Définis, indéfinis, partitifs", 
            "1. Articles définis : le, la, l', les (chose connue).\n" +
            "2. Articles indéfinis : un, une, des (chose inconnue).\n" +
            "3. Articles partitifs : du, de la, de l' (quantité indéterminée). Ex: Je mange du pain.\n\n" +
            "Note sur l'élision : 'le' ou 'la' deviennent 'l'' devant une voyelle ou un h muet. Ex: l'oiseau.", 
            R.drawable.satellite, "Grammaire", 100));

        models.add(new Model("Négation", 
            "Ne... pas, ne... plus...", 
            "La structure de base est : Ne + Verbe + Pas.\n" +
            "Ex: Je ne mange pas.\n\n" +
            "Autres formes :\n" +
            "- Ne... plus (arrêt d'une action)\n" +
            "- Ne... jamais (fréquence nulle)\n" +
            "- Ne... rien (objet nul)\n" +
            "- Ne... personne (sujet/objet humain nul)", 
            R.drawable.n3, "Grammaire", 15));

        models.add(new Model("Pronoms relatifs", 
            "Qui, que, dont, où", 
            "Ils servent à relier deux phrases pour éviter les répétitions :\n" +
            "- QUI : remplace le sujet. Ex: L'homme qui parle.\n" +
            "- QUE : remplace le COD. Ex: Le livre que je lis.\n" +
            "- OÙ : remplace le lieu ou le temps. Ex: La ville où j'habite.", 
            R.drawable.n4, "Grammaire", 0));

        models.add(new Model("Les Adverbes", 
            "Modificateurs de verbes", 
            "Les adverbes sont invariables. Beaucoup se terminent par -ment.\n\n" +
            "Ex: Lentement, rapidement, joyeusement.\n" +
            "Ils servent à préciser la manière, le temps ou le lieu de l'action.", 
            R.drawable.n5, "Grammaire", 20));

        // Verbe Category
        models.add(new Model("Passé composé", 
            "Avec avoir et être", 
            "Se forme avec l'auxiliaire au présent + Participe Passé.\n\n" +
            "Auxiliaire ÊTRE pour :\n" +
            "1. Les 14 verbes de mouvement (aller, venir, partir...)\n" +
            "2. Les verbes pronominaux (se lever).\n\n" +
            "Note : Avec être, le participe passé s'accorde avec le sujet. Ex: Elle est partie.", 
            R.drawable.n7, "Verbe", 50));

        models.add(new Model("Imparfait", 
            "Description et habitude", 
            "Formation : Radical de 'nous' au présent + terminaisons -ais, -ais, -ait, -ions, -iez, -aient.\n\n" +
            "L'imparfait exprime une action longue ou répétée dans le passé, ou une description.\n" +
            "Ex: Quand j'étais petit, je jouais souvent au parc.", 
            R.drawable.n8, "Verbe", 40));

        models.add(new Model("Futur simple", 
            "Actions à venir", 
            "Formation : Infinitif + terminaisons -ai, -as, -a, -ons, -ez, -ont.\n\n" +
            "Ex: Manger -> Je mangerai, tu mangeras.\n\n" +
            "Ir réguliers : être (ser-), avoir (aur-), aller (ir-), faire (fer-), voir (verr-).", 
            R.drawable.n2, "Verbe", 90));

        models.add(new Model("Impératif", 
            "Donner des ordres", 
            "Il n'existe qu'à 3 personnes : Tu, Nous, Vous.\n" +
            "Pas de sujet exprimé.\n\n" +
            "Ex: Parle ! Parlons ! Parlez !\n" +
            "Note : Les verbes en -er perdent le 's' à la 2e pers. du singulier. Ex: Mange ! (et non manges).", 
            R.drawable.n6, "Verbe", 80));

        models.add(new Model("Verbes pronominaux", 
            "Se laver, se lever...", 
            "Se construisent avec un pronom réfléchi (me, te, se, nous, vous, se).\n\n" +
            "Présent : Je me lave, tu te laves, il se lave...\n" +
            "Passé composé : Toujours avec l'auxiliaire ÊTRE. Ex: Je me suis lavé.", 
            R.drawable.n5, "Verbe", 75));

        models.add(new Model("Conditionnel présent", 
            "Souhait ou hypothèse", 
            "Formation : Radical du futur + terminaisons de l'imparfait.\n\n" +
            "Ex: Je mangerais (I would eat).\n" +
            "S'utilise souvent avec 'si' : Si j'avais de l'argent, j'achèterais une voiture.", 
            R.drawable.n3, "Verbe", 0));

        models.add(new Model("Subjonctif présent", 
            "Doute ou émotion", 
            "Formation : Radical de 'ils' au présent + -e, -es, -e, -ions, -iez, -ent.\n\n" +
            "Ex: Il faut que je parte. Je doute qu'il vienne.\n" +
            "C'est le mode du subjectif.", 
            R.drawable.n1, "Verbe", 5));

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
