package com.example.mongrammaire.horisontal_cardv.categories.subordonnes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.R;

import java.util.ArrayList;
import java.util.List;

public class subsAdapter extends RecyclerView.Adapter<subsAdapter.ViewHolder>{

    private List<subs> subsFeed=new ArrayList();
    // Context is not used here but may be required to
    // perform complex operations or call methods from outside
    private Context context;

    // Constructor
    public subsAdapter(Context context){
        this.context=context;
    }

    public void setsubsFeed(List<subs> callsFeed){
        this.subsFeed=callsFeed;
    }

    // Invoked by layout manager to replace the contents of the views
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        subs subs = subsFeed.get(position);
        holder.showCallDetails(subs);
    }

    @Override
    public int getItemCount(){return subsFeed.size();}

    // Invoked by layout manager to create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Attach layout for single cell
        int layout = R.layout.subs_feed_layout;
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    // Reference to the views for each items to display desired information
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView subsNameTextView,descTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initiate view
            subsNameTextView=(TextView)itemView.findViewById(R.id.subsName);
            descTextView=(TextView)itemView.findViewById(R.id.subsTime);
        }


        public void showCallDetails(subs subs){
            // Attach values for each item
            String subsName   =subs.getsubsName();
            String desc     =subs.getdesc();
            subsNameTextView.setText(subsName);
            descTextView.setText(desc);
        }
    }
}
