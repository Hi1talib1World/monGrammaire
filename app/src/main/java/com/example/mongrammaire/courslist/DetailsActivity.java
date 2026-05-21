package com.example.mongrammaire.courslist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.R;

public class DetailsActivity extends AppCompatActivity {

    TextView mTitleTv, mDescTv;
    ImageView iImageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTitleTv = findViewById(R.id.dtxt1);
        mDescTv = findViewById(R.id.dtxt2);
        iImageTV = findViewById(R.id.dimg1);

        Intent intent = getIntent();
        String mTitle = intent.getStringExtra("iTitleTv");
        String mDescr = intent.getStringExtra("iDescTv");
        int mImgRes = intent.getIntExtra("iImgTv", R.drawable.logo);

        mTitleTv.setText(mTitle);
        iImageTV.setImageResource(mImgRes);
        mDescTv.setText(mDescr);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        findViewById(R.id.button3).setOnClickListener(v -> {
            // Start Quiz as a placeholder for "Plus" or "Individual Lesson"
            Intent quizIntent = new Intent(this, com.example.mongrammaire.Quiz.MainGameActivity.class);
            startActivity(quizIntent);
        });
    }
}
