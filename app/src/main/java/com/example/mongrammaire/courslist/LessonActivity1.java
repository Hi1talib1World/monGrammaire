package com.example.mongrammaire.courslist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.Data.Repository;
import com.example.mongrammaire.R;
import com.example.mongrammaire.Utils.CustomProgressBar;
import com.example.mongrammaire.Utils.Injection;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LessonActivity1 extends AppCompatActivity {

    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;

    @BindView(R.id.user_progress_bar)
    CustomProgressBar dailyProgressBar;

    @BindView(R.id.continue_button)
    Button continueButton;

    @BindView(R.id.monday_bar)
    CustomProgressBar mondayBar;

    @BindView(R.id.tuesday_bar)
    CustomProgressBar tuesdayBar;

    @BindView(R.id.wednesday_bar)
    CustomProgressBar wednesdayBar;

    @BindView(R.id.thursday_bar)
    CustomProgressBar thursdayBar;

    @BindView(R.id.friday_bar)
    CustomProgressBar fridayBar;

    @BindView(R.id.saturday_bar)
    CustomProgressBar saturdayBar;

    @BindView(R.id.sunday_bar)
    CustomProgressBar sundayBar;

    Repository repository;

    int mondayProgress,
            tuesdayProgress,
            wednesdayProgress,
            thursdayProgress,
            fridayProgress,
            saturdayProgress,
            sundayProgress;

    int dailyGoal;
    String currentLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cour1);
        ButterKnife.bind(this);

        initData();
    }
    private void initData() {

        Hawk.init(this).build();
        repository = Injection.provideRepository();
        dailyGoal = Hawk.get("dailyGoal", 100);
        currentLesson = Hawk.get("lesson", "basics");

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic for moving to next lesson can be added here
                finish();
            }
        });

        updateLessonProgress();
        setupProgressBar();
        setupWeekBar();
    }

    private void updateLessonProgress() {
        int progress = Hawk.get(currentLesson.toLowerCase() + "_progress", 0);
        progress += 20; // Increment progress by 20% each time for this example
        
        if (progress >= 100) {
            progress = 100;
            repository.setLessonComplete(currentLesson, true);
            Hawk.put(currentLesson.toLowerCase() + "_completed", true);
            Toast.makeText(this, "Lesson Completed!", Toast.LENGTH_SHORT).show();
        }
        
        Hawk.put(currentLesson.toLowerCase() + "_progress", progress);
        repository.setLessonProgress(currentLesson, progress);
        
        // Update overall progress (simple average for this example)
        updateOverallProgress();
    }

    private void updateOverallProgress() {
        String[] lessons = {"basics", "phrases", "greeting", "food", "animal", "clothing"};
        int total = 0;
        for (String l : lessons) {
            total += Hawk.get(l.toLowerCase() + "_progress", 0);
        }
        int overall = total / lessons.length;
        Hawk.put("overallProgress", overall);
        // Assuming repository has a way to set overall progress, or it's calculated server-side
    }

    private void setupProgressBar() {

        int dailyXp;

        if (Hawk.get("dailyXp") != null) {

            dailyXp = Hawk.get("dailyXp");

            dailyXp += 10;

            Hawk.put("dailyXp", dailyXp);
            repository.setDailyXp(dailyXp);

        } else {

            dailyXp = 10;

            Hawk.put("dailyXp", dailyXp);
            repository.setDailyXp(dailyXp);
        }

        dailyProgressBar.setMax(dailyGoal);

        dailyProgressBar.setProgressWithAnimation(dailyXp, 2000);
    }

    private void setupWeekBar() {

        if (Hawk.get("mondayXp") != null) {
            mondayProgress = (int) Hawk.get("mondayXp");
        } else {
            mondayProgress = 0;
        }
        if (Hawk.get("tuesdayXp") != null) {
            tuesdayProgress = (int) Hawk.get("tuesdayXp");
        } else {
            tuesdayProgress = 0;
        }
        if (Hawk.get("wednesdayXp") != null) {
            wednesdayProgress = (int) Hawk.get("wednesdayXp");
        } else {
            wednesdayProgress = 0;
        }
        if (Hawk.get("thursdayXp") != null) {
            thursdayProgress = (int) Hawk.get("thursdayXp");
        } else {
            thursdayProgress = 0;
        }
        if (Hawk.get("fridayXp") != null) {
            fridayProgress = (int) Hawk.get("fridayXp");
        } else {
            fridayProgress = 0;
        }
        if (Hawk.get("saturdayXp") != null) {
            saturdayProgress = (int) Hawk.get("saturdayXp");
        } else {
            saturdayProgress = 0;
        }
        if (Hawk.get("sundayXp") != null) {
            sundayProgress = (int) Hawk.get("sundayXp");
        } else {
            sundayProgress = 0;
        }

        mondayBar.setProgress(mondayProgress);
        tuesdayBar.setProgress(tuesdayProgress);
        wednesdayBar.setProgress(wednesdayProgress);
        thursdayBar.setProgress(thursdayProgress);
        fridayBar.setProgress(fridayProgress);
        saturdayBar.setProgress(saturdayProgress);
        sundayBar.setProgress(sundayProgress);

        mondayBar.setMax(dailyGoal);
        tuesdayBar.setMax(dailyGoal);
        wednesdayBar.setMax(dailyGoal);
        thursdayBar.setMax(dailyGoal);
        fridayBar.setMax(dailyGoal);
        saturdayBar.setMax(dailyGoal);
        sundayBar.setMax(dailyGoal);
    }
}
