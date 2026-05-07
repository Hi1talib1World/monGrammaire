package com.example.mongrammaire.courslist;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mongrammaire.Data.Local.LessonDatabaseHelper;
import com.example.mongrammaire.Data.Repository;
import com.example.mongrammaire.Model.LessonModel;
import com.example.mongrammaire.R;
import com.example.mongrammaire.Utils.Injection;
import com.example.mongrammaire.databinding.ActivityCour1Binding;
import com.orhanobut.hawk.Hawk;

import java.util.List;

public class LessonActivity1 extends AppCompatActivity {

    private ActivityCour1Binding binding;
    private Repository repository;
    private LessonDatabaseHelper dbHelper;
    
    private int dailyGoal;
    private String currentLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCour1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        applyFadeInAnimation();
    }

    private void applyFadeInAnimation() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        binding.mainLayout.startAnimation(fadeIn);
    }

    private void initData() {
        Hawk.init(this).build();
        repository = Injection.provideRepository();
        dbHelper = new LessonDatabaseHelper(this);
        
        dailyGoal = Hawk.get("dailyGoal", 100);
        currentLesson = Hawk.get("lesson", "basics");

        binding.continueButton.setOnClickListener(v -> {
            updateLessonProgress();
            finish();
        });

        setupRecyclerView();
        setupProgressBar();
    }

    private void setupRecyclerView() {
        List<LessonModel> lessons = dbHelper.getLessonsByCategory(currentLesson);
        SubLessonAdapter adapter = new SubLessonAdapter(lessons, lesson ->
            Toast.makeText(this, "Focusing on: " + lesson.getTitle(), Toast.LENGTH_SHORT).show()
        );
        binding.subLessonRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Add layout animation to RecyclerView
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
        binding.subLessonRecyclerView.setLayoutAnimation(animation);

        binding.subLessonRecyclerView.setAdapter(adapter);
    }

    private void updateLessonProgress() {
        int progress = Hawk.get(currentLesson.toLowerCase() + "_progress", 0);
        progress += 20; 
        
        if (progress >= 100) {
            progress = 100;
            repository.setLessonComplete(currentLesson, true);
            Hawk.put(currentLesson.toLowerCase() + "_completed", true);
            Toast.makeText(this, "Module Completed!", Toast.LENGTH_SHORT).show();
        }
        
        Hawk.put(currentLesson.toLowerCase() + "_progress", progress);
        repository.setLessonProgress(currentLesson, progress);
        
        updateOverallProgress();
    }

    private void updateOverallProgress() {
        String[] lessonCategories = {"basics", "phrases", "greeting", "food", "animal", "clothing"};
        int total = 0;
        for (String l : lessonCategories) {
            total += Hawk.get(l.toLowerCase() + "_progress", 0);
        }
        int overall = total / lessonCategories.length;
        Hawk.put("overallProgress", overall);
    }

    private void setupProgressBar() {
        int dailyXp = Hawk.get("dailyXp", 0);
        dailyXp += 10;
        Hawk.put("dailyXp", dailyXp);
        repository.setDailyXp(dailyXp);

        binding.userProgressBar.setMax(dailyGoal);
        binding.userProgressBar.setProgressWithAnimation(dailyXp, 2000);
    }
}
