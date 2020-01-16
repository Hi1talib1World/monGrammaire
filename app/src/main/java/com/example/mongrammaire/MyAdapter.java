package com.example.mongrammaire;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<mycardsHolder> {
    Context c;
    ArrayList<Model> models;

    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public mycardsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, null);
        return new mycardsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull mycardsHolder mycardsHolder, int i) {
        mycardsHolder.mTitleTV.setText(models.get(i).getTitle());
        mycardsHolder.mDescrTV.setText(models.get(i).getTitle());
        mycardsHolder.mImageIv.setImageResource(models.get(i).getImg());

        mycardsHolder.setItemClickListener(new itemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
