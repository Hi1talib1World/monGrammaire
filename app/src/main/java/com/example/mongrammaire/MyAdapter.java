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

import java.io.ByteArrayOutputStream;
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
    public void onBindViewHolder(@NonNull final mycardsHolder mycardsHolder, int i) {
        mycardsHolder.mTitleTV.setText(models.get(i).getTitle());
        mycardsHolder.mDescrTV.setText(models.get(i).getTitle());
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
                intent.putExtra("mTitleTV",title);
                intent.putExtra("mDescTV",descr);
                intent.putExtra("mImgTV",bytes);
                c.startActivity(intent);

            }
        });

        mycardsHolder.setItemClickListener(new itemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if (models.get(pos).getTitle().equals("a")){

                }
                if (models.get(pos).getTitle().equals("b")){

                }if (models.get(pos).getTitle().equals("c")){

                }if (models.get(pos).getTitle().equals("d")){

                }if (models.get(pos).getTitle().equals("e")){

                }if (models.get(pos).getTitle().equals("f")){

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
