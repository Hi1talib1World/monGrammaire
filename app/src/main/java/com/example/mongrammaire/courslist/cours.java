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

import com.example.mongrammaire.Data.Local.LessonDatabaseHelper;
import com.example.mongrammaire.Model.LessonModel;

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
    private LessonDatabaseHelper dbHelper;

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
        
        dbHelper = new LessonDatabaseHelper(requireContext());

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
            popup.getMenu().add("Vocabulaire");
            popup.getMenu().add("Orthographe");
            popup.getMenu().add("Conjugaison");
            popup.getMenu().add("Culture");
            popup.getMenu().add("Expressions");
            popup.getMenu().add("Phonétique");
            popup.getMenu().add("Littérature");
            popup.getMenu().add("Quotidien");
            popup.getMenu().add("TCF/DELF");
            
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
            boolean matchesCategory = currentCategory.equals("Tous") || model.getCategory().equalsIgnoreCase(currentCategory);
            boolean matchesSearch = model.getTitle().toLowerCase().contains(currentSearchQuery.toLowerCase());
            
            if (matchesCategory && matchesSearch) {
                filteredList.add(model);
            }
        }
        adapter.updateList(filteredList);
    }

    private ArrayList<Model> getPlayers(){
        ArrayList<Model> models = new ArrayList<>();
        List<LessonModel> dbLessons = dbHelper.getAllLessons();

        for (LessonModel lesson : dbLessons) {
            int imgRes = R.drawable.n1; // Default
            switch (lesson.getCategory().toLowerCase()) {
                case "basics": imgRes = R.drawable.n1; break;
                case "phrases": imgRes = R.drawable.n2; break;
                case "greeting": imgRes = R.drawable.n3; break;
                case "food": imgRes = R.drawable.n4; break;
                case "animal": imgRes = R.drawable.n5; break;
                case "advanced": imgRes = R.drawable.n6; break;
                case "grammaire": imgRes = R.drawable.n1; break;
                case "verbe": imgRes = R.drawable.n7; break;
                case "vocabulaire": imgRes = R.drawable.n3; break;
                case "orthographe": imgRes = R.drawable.n5; break;
                case "conjugaison": imgRes = R.drawable.n8; break;
                case "culture": imgRes = R.drawable.n1; break;
            }

            models.add(new Model(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getContent(),
                imgRes,
                mapCategoryToUI(lesson.getCategory()),
                0 // Initial progress
            ));
        }

        return models;
    }

    private String mapCategoryToUI(String dbCategory) {
        if (dbCategory == null) return "Grammaire";
        
        // Use database names directly if they already match UI labels
        switch (dbCategory) {
            case "Grammaire":
            case "Verbe":
            case "Vocabulaire":
            case "Orthographe":
            case "Conjugaison":
            case "Culture":
            case "Expressions":
            case "Phonétique":
            case "Littérature":
            case "Quotidien":
            case "TCF/DELF":
                return dbCategory;
            default:
                // Backward compatibility for legacy internal names
                switch (dbCategory.toLowerCase()) {
                    case "basics": return "Grammaire";
                    case "phrases": return "Expressions";
                    case "greeting": return "Vocabulaire";
                    case "food": return "Vocabulaire";
                    case "animal": return "Vocabulaire";
                    case "advanced": return "Grammaire";
                    default: return "Grammaire";
                }
        }
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
