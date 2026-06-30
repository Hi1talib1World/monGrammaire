package com.example.mongrammaire.courslist.cards.favorites;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import com.example.mongrammaire.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListFragment extends Fragment {

    private RecyclerView recyclerView;
    private View emptyState;
    private SharedPreference sharedPreference;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);

        recyclerView = view.findViewById(R.id.favorite_recycler_view);
        emptyState = view.findViewById(R.id.empty_state);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        sharedPreference = new SharedPreference();
        
        ChipGroup chipGroup = view.findViewById(R.id.filter_chip_group);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            refreshList();
        });

        refreshList();

        view.findViewById(R.id.btn_explore).setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, new com.example.mongrammaire.courslist.cours())
                        .commit();
            }
        });

        return view;
    }

    private void refreshList() {
        if (getView() == null) return;
        List<Model> favorites = sharedPreference.getFavorites(activity);
        
        ChipGroup chipGroup = getView().findViewById(R.id.filter_chip_group);
        int checkedId = chipGroup.getCheckedChipId();
        
        List<Model> displayList = new ArrayList<>();
        if (favorites != null) {
            if (checkedId == R.id.chip_all) {
                displayList.addAll(favorites);
            } else {
                String filter = "";
                if (checkedId == R.id.chip_grammaire) filter = "Grammaire";
                else if (checkedId == R.id.chip_verbe) filter = "Verbe";
                else if (checkedId == R.id.chip_vocabulaire) filter = "Vocabulaire";
                
                for (Model m : favorites) {
                    if (m.getCategory() != null && m.getCategory().equalsIgnoreCase(filter)) {
                        displayList.add(m);
                    }
                }
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        
        if (displayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
            
            MyAdapter adapter = new MyAdapter(displayList, true);
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    checkEmpty(adapter);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    checkEmpty(adapter);
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    private void checkEmpty(MyAdapter adapter) {
        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
