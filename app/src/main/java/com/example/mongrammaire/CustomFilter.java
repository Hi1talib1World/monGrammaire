package com.example.mongrammaire;

import android.widget.Filter;

import com.example.mongrammaire.cards.Model;
import com.example.mongrammaire.cards.MyAdapter;

import java.util.ArrayList;

public class CustomFilter extends Filter {
    MyAdapter adapter;
    ArrayList<Model> filterlist;

    public CustomFilter( ArrayList<Model> filterlist, MyAdapter adapter) {
        this.adapter = adapter;
        this.filterlist = filterlist;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length()>0){

            constraint = constraint.toString().toUpperCase();

            ArrayList<Model> filtredModels = new ArrayList<>();

            for (int i=0;i<filterlist.size(); i++){
                if(filterlist.get(i).getTitle().toUpperCase().contains(constraint)){

                    filtredModels.add(filterlist.get(i));

                }
            }
            results.count = filtredModels.size();
            results.values = filtredModels;
        } else{
            results.count = filterlist.size();
            results.values = filterlist;
        }
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.models = (ArrayList<Model>) results.values;
        adapter.notifyDataSetChanged();

    }
}
