package com.example.mongrammaire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

    }
}
