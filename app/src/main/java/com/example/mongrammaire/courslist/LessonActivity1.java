package com.example.mongrammaire.courslist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.R;
import com.example.mongrammaire.Utils.CustomProgressBar;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cour1);
    }
}
