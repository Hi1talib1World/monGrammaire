package com.example.mongrammaire.courslist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mongrammaire.R;

public class DetailsActivity extends AppCompatActivity {

    TextView mTitleTv, mDescTv;
    ImageView iImageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = getSupportActionBar();

        mTitleTv = findViewById(R.id.dtxt1);
        mDescTv = findViewById(R.id.dtxt2);
        iImageTV = findViewById(R.id.dimg1);

        Intent intent = getIntent();
        String mTitle = intent.getStringExtra("iTitleTv");
        String mDescr = intent.getStringExtra("iDescTv");
        byte[] mBytes = getIntent().getByteArrayExtra("iImgTv");

        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0,mBytes.length);


        mTitleTv.setText(mTitle);
        iImageTV.setImageBitmap(bitmap);
        mDescTv.setText(mDescr);
    }
}
