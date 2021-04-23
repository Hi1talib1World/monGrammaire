package com.example.mongrammaire.courslist.cards.favorites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mongrammaire.courslist.cards.mycardsHolder;
import com.example.mongrammaire.horisontal_cardv.CustomFilter;
import com.example.mongrammaire.R;
import com.example.mongrammaire.courslist.DetailsActivity;
import com.example.mongrammaire.itemClickListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Model> {
    private Context c;
    public List<Model> models;
    CustomFilter filter;

    SharedPreference sharedPreference;


    public MyAdapter(Context c, List<Model> models) {
        super(c , R.layout.card_layout, models);
        this.c = c;
        this.models = models;
        sharedPreference = new SharedPreference();

    }

    @NonNull
    public mycardsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ImageView favoriteImg;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, null);
        return new mycardsHolder(v);
    }


    public Model getItem(int i) {
        return models.get(i);
    }

    public void onBindViewHolder(@NonNull final mycardsHolder mycardsHolder, int i) {
        mycardsHolder.mTitleTV.setText(models.get(i).getTitle());
        mycardsHolder.mDescrTV.setText(models.get(i).getDescription());
        mycardsHolder.mImageIv.setImageResource(models.get(i).getImg());

        mycardsHolder.setItemClickListener(new itemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                String title = models.get(pos).getTitle();
                String descr = models.get(pos).getDescription();
                BitmapDrawable bitmapDrawable = (BitmapDrawable)mycardsHolder.mImageIv.getDrawable();

                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] bytes = stream.toByteArray();

                Intent intent = new Intent(c, DetailsActivity.class);
                intent.putExtra("iTitleTv",title);
                intent.putExtra("iDescTv",descr);
                intent.putExtra("iImgTv",bytes);
                c.startActivity(intent);

            }
        });



        Model model = (Model) getItem(i);

        if (checkFavoriteItem(model)) {
            mycardsHolder.favoriteImg.setImageResource(R.drawable.red_heart);
            mycardsHolder.favoriteImg.setTag("red");
        } else {
            mycardsHolder.favoriteImg.setImageResource(R.drawable.heart_grey);
            mycardsHolder.favoriteImg.setTag("grey");
        }


    }




    public boolean checkFavoriteItem(Model checkModel) {
        boolean check = false;
        List<Model> favorites = sharedPreference.getFavorites(c);
        if (favorites != null) {
            for (Model Model : favorites) {
                if (Model.equals(checkModel)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    @Override
    public void add(Model model) {
        super.add(model);
        models.add(model);
        notifyDataSetChanged();
    }

    @Override
    public void remove(Model model) {
        super.remove(model);
        models.remove(model);
        notifyDataSetChanged();
    }
}
