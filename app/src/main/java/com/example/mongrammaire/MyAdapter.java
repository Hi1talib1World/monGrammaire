package com.example.mongrammaire;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<mycardsHolder> implements Filterable {
    Context c;
    ArrayList<Model> models,filterList;
    CustomFilter filter;

    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
        this.filterList = models;
    }

    @NonNull
    @Override
    public mycardsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, null);
        return new mycardsHolder(v);
    }

    @Override
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

                Intent intent = new Intent(c,DetailsActivity.class);
                intent.putExtra("iTitleTv",title);
                intent.putExtra("iDescTv",descr);
                intent.putExtra("iImgTv",bytes);
                c.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public Filter getFilter() {
        if (filter ==null){
            filter = new CustomFilter(filterList,this);
        }
        return filter;
    }
}
