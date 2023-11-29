package com.example.mongrammaire;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.courslist.Cours2ListActivity;
import com.example.mongrammaire.courslist.CoursListActivity;
import com.example.mongrammaire.horisontal_cardv.Adapter;
import com.example.mongrammaire.horisontal_cardv.Model;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Model> imageModelArrayList;
    TextView listView, listView2;
    public Adapter adapter;
    private int[] myImageList = new int[]{R.drawable.n1, R.drawable.n2,R.drawable.n3, R.drawable.n4,R.drawable.n5,R.drawable.n6,R.drawable.n7,R.drawable.n8};
    private String[] myImageNameList = new String[]{"Niveau 1","Niveau 2" ,"Niveau 3","Niveau 4","Niveau 5","Niveau 6","Niveau 7","Niveau 8"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);

        imageModelArrayList = eatFruits();
        adapter = new Adapter(getContext(), imageModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        listView = (TextView) v.findViewById(R.id.listView);
        listView2 = (TextView) v.findViewById(R.id.listView2);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), CoursListActivity.class);
                v.getContext().startActivity(intent);

            }
        });

        listView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), Cours2ListActivity.class);
                v.getContext().startActivity(intent);

            }
        });


        listView = (TextView) v.findViewById(R.id.listView2);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), CoursListActivity.class);
                v.getContext().startActivity(intent);

            }
        });


        return v;
    }
    private ArrayList<Model> eatFruits(){

        ArrayList<Model> list = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            Model fruitModel = new Model();
            fruitModel.setName(myImageNameList[i]);
            fruitModel.setImage_drawable(myImageList[i]);
            list.add(fruitModel);
        }

        return list;
    }

    public void onClick(View v) {

        Intent i= new Intent(getContext(), CoursListActivity.class);
        startActivity(i);
    }


}
