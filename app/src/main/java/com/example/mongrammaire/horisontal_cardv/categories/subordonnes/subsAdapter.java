package com.example.mongrammaire.horisontal_cardv.categories.subordonnes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mongrammaire.R;
import com.example.mongrammaire.cards.MyAdapter;

public class subsAdapter {

    private List<Call> callsFeed=new ArrayList();
    // Context is not used here but may be required to
    // perform complex operations or call methods from outside
    private Context context;

    // Constructor
    public CallsAdapter(Context context){
        this.context=context;
    }

    public void setCallsFeed(List<Call> callsFeed){
        this.callsFeed=callsFeed;
    }

    // Invoked by layout manager to replace the contents of the views
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Call call = callsFeed.get(position);
        holder.showCallDetails(call);
    }

    @Override
    public int getItemCount(){return callsFeed.size();}

    // Invoked by layout manager to create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Attach layout for single cell
        int layout = R.layout.calls_feed_layout;
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    // Reference to the views for each items to display desired information
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView callerNameTextView,callTimeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initiate view
            callerNameTextView=(TextView)itemView.findViewById(R.id.callerName);
            callTimeTextView=(TextView)itemView.findViewById(R.id.callTime);
        }


        public void showCallDetails(Call call){
            // Attach values for each item
            String callerName   =call.getCallerName();
            String callTime     =call.getCallTime();
            callerNameTextView.setText(callerName);
            callTimeTextView.setText(callTime);
        }
    }
}
