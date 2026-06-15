package com.example.mongrammaire.horisontal_cardv.categories;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.R;

public class PhrasesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);

        if (findViewById(R.id.btn_back) != null) {
            findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
        }
    }
}
