package com.example.mongrammaire.courslist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.Data.Repository;
import com.example.mongrammaire.R;
import com.example.mongrammaire.Utils.CustomProgressBar;
import com.example.mongrammaire.Utils.Injection;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LessonListActivity extends AppCompatActivity {

    @BindView(R.id.overall_progress)
    TextView overallProgressText;

    @BindView(R.id.streak_text)
    TextView streakText;

    @BindView(R.id.basic_bar)
    CustomProgressBar basicBar;
    @BindView(R.id.phrases_bar)
    CustomProgressBar phrasesBar;
    @BindView(R.id.greeting_bar)
    CustomProgressBar greetingBar;
    @BindView(R.id.food_bar)
    CustomProgressBar foodBar;
    @BindView(R.id.animal_bar)
    CustomProgressBar animalBar;
    @BindView(R.id.clothing_bar)
    CustomProgressBar clothingBar;

    @BindView(R.id.basic_check)
    ImageView basicCheck;
    @BindView(R.id.phrases_check)
    ImageView phrasesCheck;
    @BindView(R.id.greeting_check)
    ImageView greetingCheck;
    @BindView(R.id.food_check)
    ImageView foodCheck;
    @BindView(R.id.animal_check)
    ImageView animalCheck;
    @BindView(R.id.clothing_check)
    ImageView clothingCheck;

    @BindView(R.id.basic_text)
    TextView basicText;
    @BindView(R.id.phrases_text)
    TextView phrasesText;
    @BindView(R.id.greeting_text)
    TextView greetingText;
    @BindView(R.id.food_text)
    TextView foodText;
    @BindView(R.id.animal_text)
    TextView animalText;
    @BindView(R.id.clothing_text)
    TextView clothingText;

    private Repository repository;
    private String[] lessons = {"basics", "phrases", "greeting", "food", "animal", "clothing"};
    private CustomProgressBar[] progressBars;
    private ImageView[] checkmarks;
    private TextView[] lessonTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list);
        ButterKnife.bind(this);
        Hawk.init(this).build();
        repository = Injection.provideRepository();

        progressBars = new CustomProgressBar[]{basicBar, phrasesBar, greetingBar, foodBar, animalBar, clothingBar};
        checkmarks = new ImageView[]{basicCheck, phrasesCheck, greetingCheck, foodCheck, animalCheck, clothingCheck};
        lessonTextViews = new TextView[]{basicText, phrasesText, greetingText, foodText, animalText, clothingText};

        initUI();
    }

    private void initUI() {
        repository.getLessonCompleted();
        repository.getOverallProgress();
        repository.getStreak();
        loadProgress();
    }

    private void loadProgress() {
        int streak = Hawk.get("userStreak", 0);
        streakText.setText("🔥 " + streak);

        boolean allPreviousCompleted = true;
        String suggestedLesson = "";

        for (int i = 0; i < lessons.length; i++) {
            String lessonKey = lessons[i].toLowerCase();
            int progress = Hawk.get(lessonKey + "_progress", 0);
            boolean completed = Hawk.get(lessonKey + "_completed", false);

            progressBars[i].setProgress(progress);
            
            if (completed) {
                checkmarks[i].setVisibility(View.VISIBLE);
                progressBars[i].setForegroundProgressColor(Color.parseColor("#4CAF50")); // Material Green
                lessonTextViews[i].setTextColor(Color.BLACK);
            } else if (allPreviousCompleted) {
                // Highlight Current Lesson
                checkmarks[i].setVisibility(View.GONE);
                progressBars[i].setForegroundProgressColor(Color.parseColor("#FFC107")); // Material Amber
                lessonTextViews[i].setTextColor(Color.parseColor("#FF9800")); // Highlight text
                if (suggestedLesson.isEmpty()) {
                    suggestedLesson = lessons[i];
                }
            } else {
                // Locked
                checkmarks[i].setVisibility(View.GONE);
                progressBars[i].setForegroundProgressColor(Color.LTGRAY);
                lessonTextViews[i].setTextColor(Color.GRAY);
            }

            allPreviousCompleted = completed;
        }

        int overall = Hawk.get("overallProgress", 0);
        overallProgressText.setText("Progress: " + overall + "%");
        
        if (!suggestedLesson.isEmpty() && overall < 100) {
            Toast.makeText(this, "Next up: " + suggestedLesson, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.basic_bar, R.id.phrases_bar, R.id.greeting_bar, R.id.food_bar, R.id.animal_bar, R.id.clothing_bar})
    public void onLessonClicked(View view) {
        int index = -1;
        if (view.getId() == R.id.basic_bar) index = 0;
        else if (view.getId() == R.id.phrases_bar) index = 1;
        else if (view.getId() == R.id.greeting_bar) index = 2;
        else if (view.getId() == R.id.food_bar) index = 3;
        else if (view.getId() == R.id.animal_bar) index = 4;
        else if (view.getId() == R.id.clothing_bar) index = 5;

        if (index == 0 || Hawk.get(lessons[index - 1].toLowerCase() + "_completed", false)) {
            Intent intent = new Intent(this, LessonActivity1.class);
            Hawk.put("lesson", lessons[index]);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Complete previous lesson to unlock " + lessons[index], Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProgress();
    }
}
