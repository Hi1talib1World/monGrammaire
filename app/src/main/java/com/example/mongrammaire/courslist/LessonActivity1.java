package com.example.mongrammaire.courslist;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.Data.Local.LessonDatabaseHelper;
import com.example.mongrammaire.Data.Repository;
import com.example.mongrammaire.Model.LessonModel;
import com.example.mongrammaire.R;
import com.example.mongrammaire.Utils.CustomProgressBar;
import com.example.mongrammaire.Utils.Injection;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LessonActivity1 extends AppCompatActivity {

    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;

    @BindView(R.id.user_progress_bar)
    CustomProgressBar dailyProgressBar;

    @BindView(R.id.continue_button)
    Button continueButton;

    @BindView(R.id.sub_lesson_recycler_view)
    RecyclerView recyclerView;

    Repository repository;
    LessonDatabaseHelper dbHelper;
    SubLessonAdapter adapter;
    
    int dailyGoal;
    String currentLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cour1);
        ButterKnife.bind(this);

        initData();
        applyFadeInAnimation();
    }

    private void applyFadeInAnimation() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        mainLayout.startAnimation(fadeIn);
    }

    private void initData() {
        Hawk.init(this).build();
        repository = Injection.provideRepository();
        dbHelper = new LessonDatabaseHelper(this);
        
        dailyGoal = Hawk.get("dailyGoal", 100);
        currentLesson = Hawk.get("lesson", "basics");

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLessonProgress();
                finish();
            }
        });

        setupRecyclerView();
        setupProgressBar();
    }

    private void setupRecyclerView() {
        List<LessonModel> lessons = dbHelper.getLessonsByCategory(currentLesson);
        adapter = new SubLessonAdapter(lessons, lesson -> {
            Toast.makeText(this, "Focusing on: " + lesson.getTitle(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Add layout animation to RecyclerView
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(animation);

        recyclerView.setAdapter(adapter);
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

        dailyProgressBar.setMax(dailyGoal);
        dailyProgressBar.setProgressWithAnimation(dailyXp, 2000);
    }
}
