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
    private SharedPreference sharedPreference;
    private boolean isFavoriteList = false;

    public MyAdapter(List<Model> models) {
        this.models = models;
    }

    public MyAdapter(List<Model> models, boolean isFavoriteList) {
        this.models = models;
        this.isFavoriteList = isFavoriteList;
    }

    public void updateList(List<Model> newList) {
        this.models = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sharedPreference = new SharedPreference();
        int layoutId = isFavoriteList ? R.layout.item_favorite_lesson : R.layout.card_layout;
        View v = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model model = models.get(position);
        holder.mTitleTV.setText(model.getTitle());
        holder.mDescrTV.setText(isFavoriteList ? model.getCategory() : model.getDescription());
        
        if (!isFavoriteList) {
            holder.categoryChip.setText(model.getCategory());
        }

        // Favorite logic
        boolean isFavorite = checkFavoriteItem(model);
        if (isFavorite) {
            holder.favoriteImg.setImageResource(R.drawable.red_heart);
            holder.favoriteImg.setTag("red");
        } else {
            holder.favoriteImg.setImageResource(R.drawable.heart_grey);
            holder.favoriteImg.setTag("grey");
        }

        holder.favoriteImg.setOnClickListener(v -> {
            String tag = (String) holder.favoriteImg.getTag();
            if (tag.equalsIgnoreCase("grey")) {
                sharedPreference.addFavorite(context, model);
                ToastHelper.showCustomToast(context, "Ajouté aux favoris");
                holder.favoriteImg.setTag("red");
                holder.favoriteImg.setImageResource(R.drawable.red_heart);
            } else {
                sharedPreference.removeFavorite(context, model);
                ToastHelper.showCustomToast(context, "Retiré des favoris");
                
                if (isFavoriteList) {
                    models.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, models.size());
                    
                    // If list becomes empty, we should notify the fragment to show empty state
                    // But for now, let's just update the list.
                } else {
                    holder.favoriteImg.setTag("grey");
                    holder.favoriteImg.setImageResource(R.drawable.heart_grey);
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("iId", model.getId());
            intent.putExtra("iTitleTv", model.getTitle());
            intent.putExtra("iDescTv", model.getDescription());
            intent.putExtra("iContent", model.getContent());
            intent.putExtra("iImgTv", model.getImg()); 
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(context)
                .setTitle("Réinitialiser")
                .setMessage("Voulez-vous réinitialiser votre progression pour cette leçon ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    com.example.mongrammaire.Data.Local.LessonDatabaseHelper db = 
                        new com.example.mongrammaire.Data.Local.LessonDatabaseHelper(context);
                    db.resetLessonProgress(model.getId());
                    model.setProgress(0);
                    notifyItemChanged(position);
                    com.example.mongrammaire.Utils.ToastHelper.showCustomToast(context, "Progression réinitialisée");
                })
                .setNegativeButton("Annuler", null)
                .show();
            return true;
        });
    }

    public boolean checkFavoriteItem(Model checkProduct) {
        List<Model> favorites = sharedPreference.getFavorites(context);
        if (favorites != null) {
            for (Model product : favorites) {
                if (product.getTitle().equals(checkProduct.getTitle())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTV, mDescrTV;
        public ImageView favoriteImg;
        public com.google.android.material.chip.Chip categoryChip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTitleTV = itemView.findViewById(R.id.txt1);
            this.mDescrTV = itemView.findViewById(R.id.txt2);
            this.favoriteImg = itemView.findViewById(R.id.heart);
            
            View chipView = itemView.findViewById(R.id.chip_category);
            if (chipView instanceof com.google.android.material.chip.Chip) {
                this.categoryChip = (com.google.android.material.chip.Chip) chipView;
            }
        }
    }
}
