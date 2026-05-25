package com.example.mongrammaire;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.Quiz.MainGameActivity;
import com.example.mongrammaire.courslist.Cours2ListActivity;
import com.example.mongrammaire.courslist.CoursListActivity;
import com.example.mongrammaire.horisontal_cardv.Adapter;
import com.example.mongrammaire.horisontal_cardv.Model;
import com.example.mongrammaire.Utils.ProgressionManager;
import com.example.mongrammaire.Utils.AdManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Model> imageModelArrayList;
    private TextView listView, listView2;
    private TextView streakValue, scoreValue, levelValue;
    private MaterialButton btnStartQuiz, btnMenu;
    public Adapter adapter;
    private int[] myImageList = new int[]{R.drawable.n1, R.drawable.n2,R.drawable.n3, R.drawable.n4,R.drawable.n5,R.drawable.n6,R.drawable.n7,R.drawable.n8};
    private String[] myImageNameList = new String[]{"Niveau 1","Niveau 2" ,"Niveau 3","Niveau 4","Niveau 5","Niveau 6","Niveau 7","Niveau 8"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        
        ProgressionManager.init(getContext());
        
        // Initialize Views
        recyclerView = v.findViewById(R.id.recycler);
        listView = v.findViewById(R.id.listView);
        listView2 = v.findViewById(R.id.listView2);
        streakValue = v.findViewById(R.id.streakValue);
        scoreValue = v.findViewById(R.id.scoreValue);
        levelValue = v.findViewById(R.id.levelValue);
        btnStartQuiz = v.findViewById(R.id.btnStartQuiz);
        btnMenu = v.findViewById(R.id.btnMenu);

        // Load Banner Ad
        AdView adView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Preload Interstitial
        AdManager.INSTANCE.loadInterstitialAd(requireContext());

        updateStats();

        // Setup Recycler
        imageModelArrayList = eatFruits();
        adapter = new Adapter(getContext(), imageModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        
        // Listeners
        btnMenu.setOnClickListener(view -> {
            androidx.drawerlayout.widget.DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
            if (drawer != null) {
                drawer.openDrawer(androidx.core.view.GravityCompat.START);
            }
        });
        listView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CoursListActivity.class);
            startActivity(intent);
        });

        listView2.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), Cours2ListActivity.class);
            startActivity(intent);
        });

        btnStartQuiz.setOnClickListener(view -> {
            AdManager.INSTANCE.showInterstitial(requireActivity(), () -> {
                Intent intent = new Intent(getContext(), MainGameActivity.class);
                startActivity(intent);
                return null;
            });
        });

        startAnimations(v);

        return v;
    }

    private void updateStats() {
        int streak = ProgressionManager.getStreak();
        int score = ProgressionManager.getUserScore();
        int unlocked = ProgressionManager.getUnlockedLevel();

        streakValue.setText(String.valueOf(streak));
        scoreValue.setText(String.valueOf(score));
        levelValue.setText(getString(R.string.level_format, unlocked));
    }

    private void startAnimations(View v) {
        View statsCard = v.findViewById(R.id.statsCard);
        View quizCard = v.findViewById(R.id.cardDailyQuiz);

        statsCard.setAlpha(0f);
        statsCard.setTranslationY(20f);
        quizCard.setAlpha(0f);
        quizCard.setTranslationY(20f);
        recyclerView.setAlpha(0f);
        recyclerView.setTranslationX(100f);

        statsCard.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(100).start();
        quizCard.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(300).start();
        recyclerView.animate().alpha(1f).translationX(0f).setDuration(800).setStartDelay(500).start();
    }

    private ArrayList<Model> eatFruits(){
        ArrayList<Model> list = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            Model fruitModel = new Model();
            fruitModel.setName(myImageNameList[i]);
            fruitModel.setImage_drawable(myImageList[i]);
            list.add(fruitModel);
        }
        return list;
    }
}
