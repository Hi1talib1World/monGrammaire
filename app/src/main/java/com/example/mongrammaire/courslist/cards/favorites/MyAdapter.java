package com.example.mongrammaire.courslist.cards.favorites;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.R;
import com.example.mongrammaire.Utils.ToastHelper;
import com.example.mongrammaire.courslist.DetailsActivity;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private Context context;
    public List<Model> models;

    public MyAdapter(List<Model> models) {
        this.models = models;
    }

    public void updateList(List<Model> newList) {
        this.models = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model model = models.get(position);
        holder.mTitleTV.setText(model.getTitle());
        holder.mDescrTV.setText(model.getDescription());
        holder.mImageIv.setImageResource(model.getImg());
        holder.progressIndicator.setProgress(model.getProgress());
        holder.categoryChip.setText(model.getCategory());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("iTitleTv", model.getTitle());
            intent.putExtra("iDescTv", model.getDescription());
            intent.putExtra("iImgTv", model.getImg()); // Passing resource ID instead of byte array for simplicity
            context.startActivity(intent);
            ToastHelper.showCustomToast(context, "Ouverture de : " + model.getTitle());
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageIv;
        public TextView mTitleTV, mDescrTV;
        public ImageView favoriteImg;
        public com.google.android.material.progressindicator.LinearProgressIndicator progressIndicator;
        public com.google.android.material.chip.Chip categoryChip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mImageIv = itemView.findViewById(R.id.img1);
            this.mTitleTV = itemView.findViewById(R.id.txt1);
            this.mDescrTV = itemView.findViewById(R.id.txt2);
            this.favoriteImg = itemView.findViewById(R.id.heart);
            this.progressIndicator = itemView.findViewById(R.id.horizontal_progress_bar);
            this.categoryChip = itemView.findViewById(R.id.chip_category);
        }
    }
}
