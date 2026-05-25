package com.example.mongrammaire.courslist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mongrammaire.R;
import com.example.mongrammaire.Utils.ToastHelper;
import com.example.mongrammaire.courslist.cards.favorites.Model;
import com.example.mongrammaire.courslist.cards.favorites.SharedPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailsActivity extends AppCompatActivity {

    TextView mTitleTv, mDescTv, mContentTv;
    ImageView iImageTV;
    FloatingActionButton fabFavorite;
    SharedPreference sharedPreference;
    Model currentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mTitleTv = findViewById(R.id.dtxt1);
        mDescTv = findViewById(R.id.dtxt2);
        mContentTv = findViewById(R.id.content_text);
        iImageTV = findViewById(R.id.dimg1);
        fabFavorite = findViewById(R.id.fab_favorite);
        sharedPreference = new SharedPreference();

        Intent intent = getIntent();
        String mTitle = intent.getStringExtra("iTitleTv");
        String mDescr = intent.getStringExtra("iDescTv");
        String mContent = intent.getStringExtra("iContent");
        int mImgRes = intent.getIntExtra("iImgTv", R.drawable.logo);

        currentModel = new Model(mTitle, mDescr, mContent, mImgRes, "", 0);

        mTitleTv.setText(mTitle);
        mDescTv.setText(mDescr);
        mContentTv.setText(mContent != null ? mContent : "Pas de contenu supplémentaire disponible pour cette leçon.");
        iImageTV.setImageResource(mImgRes);

        updateFavoriteFab();

        fabFavorite.setOnClickListener(v -> {
            if (isFavorite()) {
                sharedPreference.removeFavorite(this, currentModel);
                ToastHelper.showCustomToast(this, "Retiré des favoris");
            } else {
                sharedPreference.addFavorite(this, currentModel);
                ToastHelper.showCustomToast(this, "Ajouté aux favoris");
            }
            updateFavoriteFab();
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        findViewById(R.id.button3).setOnClickListener(v -> {
            Intent quizIntent = new Intent(this, com.example.mongrammaire.Quiz.MainGameActivity.class);
            startActivity(quizIntent);
        });
    }

    private boolean isFavorite() {
        return sharedPreference.getFavorites(this).stream()
                .anyMatch(m -> m.getTitle().equals(currentModel.getTitle()));
    }

    private void updateFavoriteFab() {
        if (isFavorite()) {
            fabFavorite.setImageResource(R.drawable.red_heart);
        } else {
            fabFavorite.setImageResource(R.drawable.heart_grey);
        }
    }
}
