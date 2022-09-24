package com.example.mongrammaire.courslist;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.Data.Repository;
import com.example.mongrammaire.R;

import org.jetbrains.annotations.NotNull;

public class LessonListActivity extends AppCompatActivity {

    public Repository repository;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list);
    }
}
