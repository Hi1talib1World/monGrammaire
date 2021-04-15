package com.example.mongrammaire.horisontal_cardv.categories.conjugaison;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mongrammaire.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 16/11/17.
 */

public class verbesAdapter extends RecyclerView.Adapter<verbesAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<verbes> verbesList;
    private List<verbes> verbesListFiltered;
    private verbesAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onverbesSelected(verbesListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public verbesAdapter(Context context, List<verbes> contactList, verbesAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.verbesList = contactList;
        this.verbesListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.verbe_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final verbes contact = verbesListFiltered.get(position);
        holder.name.setText(contact.getverbe());
        holder.phone.setText(contact.getdesc());

        Glide.with(context)
                .load(contact.getlogo())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return verbesListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    verbesListFiltered = verbesList;
                } else {
                    List<verbes> filteredList = new ArrayList<>();
                    for (verbes row : verbesList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getverbe().toLowerCase().contains(charString.toLowerCase()) || row.getdesc().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    verbesListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = verbesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                verbesListFiltered = (ArrayList<verbes>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface verbesAdapterListener {
        void onverbesSelected(verbes verbes);
    }
}