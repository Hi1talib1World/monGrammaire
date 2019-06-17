package com.example.mongrammaire;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>  {

    private OnItemClickListener mListener;





    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void SetOnItemClickListener (OnItemClickListener listener){
        mListener = listener ;
    }
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<product> productList;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.card_layout, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        product product = productList.get(position);
        final int pos = position;

        //binding the data with the viewholder views
        holder.textViewTitle.setText(product.getTitle());
        holder.textViewShortDesc.setText(product.getShortdesc());

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos == 0){
                    view.getContext().startActivity(new Intent(view.getContext(),level1.class));
                }else if(pos == 1){
                    view.getContext().startActivity(new Intent(view.getContext(),level2.class));

            }else if(pos ==2){
                view.getContext().startActivity(new Intent(view.getContext(),level3.class));
            }
            }
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;
        RelativeLayout relative;
        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.txt1);
            textViewShortDesc = itemView.findViewById(R.id.txt2);
             relative = itemView.findViewById(R.id.RelativeLayout);
         //   textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.img1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}