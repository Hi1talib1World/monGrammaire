package com.example.mongrammaire.horisontal_cardv.categories.subordonnes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.mongrammaire.R;
import com.example.mongrammaire.cards.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class subordonnes extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<subs> callsList;
    private subsAdapter callsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordonnes);
        // Set Layout Manager
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Limiting the size
        recyclerView.setHasFixedSize(true);

        // Initialize list items
        init();

    }
    private void init(){
    subsList = new ArrayList<subs>();

    // Initiating Adapter
    subsAdapter =new subsAdapter(RecyclerViewActivity.this);
        recyclerView.setAdapter(subsAdapter);

    // Adding some demo data(Call Objects).
    // You can get them from your data server
        subsAdapter.add(new subs("John","9:30 AM"));
        subsAdapter.add(new subs("Rob","9:40 AM"));
        subsAdapter.add(new subs("Peter","9:45 AM"));
        subsAdapter.add(new subs("Jack","9:50 AM"));
        subsAdapter.add(new subs("Bob","9:55 AM"));
        subsAdapter.add(new subs("Sandy","10:00 AM"));
        subsAdapter.add(new subs("Kate","10:05 AM"));
        subsAdapter.add(new subs("Daniel","10:10 AM"));
        subsAdapter.add(new subs("Roger","10:15 AM"));
        subsAdapter.add(new subs("Sid","10:20 AM"));
        subsAdapter.add(new subs("Kora","10:25 AM"));
        subsAdapter.add(new subs("Nick","10:30 AM"));
        subsAdapter.add(new subs("Rose","10:35 AM"));
        subsAdapter.add(new subs("Mia","10:40 AM"));
        subsAdapter.add(new subs("Scott","10:45 AM"));

    // Set items to adapter
        callsAdapter.setCallsFeed(callsList);
        callsAdapter.notifyDataSetChanged();
}
}
