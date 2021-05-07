package com.example.mongrammaire.courslist.cards.favorites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.horisontal_cardv.CustomFilter;
import com.example.mongrammaire.R;
import com.example.mongrammaire.courslist.DetailsActivity;
import com.example.mongrammaire.itemClickListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private Context c;
    public List<Model> models;
    CustomFilter filter;

    SharedPreference sharedPreference;

    public MyAdapter( List<Model> model) {

        this.models = model;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ImageView favoriteImg;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, null);
        return new ViewHolder(v);
    }
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int i) {
        holder.mTitleTV.setText(models.get(i).getTitle());
        holder.mDescrTV.setText(models.get(i).getDescription());
        holder.mImageIv.setImageResource(models.get(i).getImg());

        /*holder.setItemClickListener(new itemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                String title = models.get(pos).getTitle();
                String descr = models.get(pos).getDescription();
                BitmapDrawable bitmapDrawable = (BitmapDrawable)holder.mImageIv.getDrawable();

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
        });*/



       /* Model model = (Model) getItem(i);

        if (checkFavoriteItem(model)) {
            holder.favoriteImg.setImageResource(R.drawable.red_heart);
            holder.favoriteImg.setTag("red");
        } else {
            holder.favoriteImg.setImageResource(R.drawable.heart_grey);
            holder.favoriteImg.setTag("grey");
        }*/


    }



    @Override
    public int getItemCount() {
        return models.size();
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



    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView mImageIv;
        public TextView mTitleTV,mDescrTV;
        public ImageView favoriteImg;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mImageIv = itemView.findViewById(R.id.img1);//
            this.mTitleTV = itemView.findViewById(R.id.txt1);
            this.mDescrTV = itemView.findViewById(R.id.txt2);
            this.favoriteImg = itemView.findViewById(R.id.heart);
            //itemView.setOnClickListener(this);

        }

    }
    public interface OnCardListener{
        void onCardClick(int position);
    }
}
