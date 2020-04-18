package com.example.mongrammaire.horisontal_cardv.categories.subordonnes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mongrammaire.R;
import com.example.mongrammaire.cards.MyAdapter;

public class subordonnes extends AppCompatActivity {

    private RecyclerView srecyclerView;
    private RecyclerView.Adapter sAdapter;
    private RecyclerView.LayoutManager slayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordonnes);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        srecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        slayoutManager = new LinearLayoutManager(this);
        srecyclerView.setLayoutManager(slayoutManager);

        // specify an adapter (see also next example)
        sAdapter = new MyAdapter(myDataset);
        srecyclerView.setAdapter(sAdapter);
    }
}
