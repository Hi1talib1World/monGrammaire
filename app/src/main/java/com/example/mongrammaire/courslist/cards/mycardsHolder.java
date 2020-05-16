package com.example.mongrammaire.courslist.cards;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mongrammaire.R;
import com.example.mongrammaire.itemClickListener;

public class mycardsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImageIv;
    TextView mTitleTV,mDescrTV;
    ImageView favoriteImg;

    com.example.mongrammaire.itemClickListener itemClickListener ;

    public mycardsHolder(@NonNull View itemView) {
        super(itemView);
        this.mImageIv = itemView.findViewById(R.id.img1);//
        this.mTitleTV = itemView.findViewById(R.id.txt1);
        this.mDescrTV = itemView.findViewById(R.id.txt2);
        this.favoriteImg = itemView.findViewById(R.id.)
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v, getLayoutPosition());
    }
    public void setItemClickListener(itemClickListener ic){
        this.itemClickListener = ic;
    }


}
