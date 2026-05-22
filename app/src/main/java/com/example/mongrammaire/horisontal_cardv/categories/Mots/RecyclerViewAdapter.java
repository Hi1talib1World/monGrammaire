package com.example.mongrammaire.horisontal_cardv.categories.Mots;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<DataModel> mValues;
    private Context mContext;
    private ItemListener mListener;
    private int selectedPosition = -1;

    public RecyclerViewAdapter(Context context, ArrayList<DataModel> values, ItemListener itemListener) {
        this.mValues = values;
        this.mContext = context;
        this.mListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public MaterialCardView cardView;
        DataModel item;

        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textView);
            imageView = v.findViewById(R.id.imageView);
            cardView = v.findViewById(R.id.cardView);

            v.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = position;
                    notifyItemChanged(selectedPosition);
                    
                    if (mListener != null) {
                        mListener.onItemClick(mValues.get(position));
                    }
                }
            });
        }

        public void bind(DataModel item, boolean isSelected) {
            this.item = item;
            textView.setText(item.text);
            
            if (item.drawable != 0) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(item.drawable);
            } else {
                imageView.setVisibility(View.GONE);
            }

            if (isSelected) {
                cardView.setStrokeColor(ContextCompat.getColor(mContext, R.color.primary));
                cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.primaryContainer));
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.onPrimaryContainer));
            } else {
                cardView.setStrokeColor(ContextCompat.getColor(mContext, R.color.surfaceVariant));
                cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.surface));
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.onSurface));
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mValues.get(position), position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(DataModel item);
    }
}
