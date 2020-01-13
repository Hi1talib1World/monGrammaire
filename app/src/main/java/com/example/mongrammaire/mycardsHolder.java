package com.example.mongrammaire;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class mycardsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImageIv;
    TextView mTitleTV,mDescrTV;
    itemClickListener itemClickListener ;
    public mycardsHolder(@NonNull View itemView) {
        super(itemView);

    }

    @Override
    public void onClick(View v) {

    }
}
