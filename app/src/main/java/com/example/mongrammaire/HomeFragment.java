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
    private int[] myImageList = new int[]{R.drawable.house, R.drawable.house,R.drawable.house, R.drawable.house,R.drawable.house,R.drawable.house,R.drawable.house};
    private String[] myImageNameList = new String[]{"Apple","Mango" ,"Strawberry","Pineapple","Orange","Blueberry","Watermelon"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);

        imageModelArrayList = eatFruits();
        adapter = new Adapter(getActivity(), imageModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

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

        Intent i= new Intent(getActivity(), CoursListActivity.class);
        startActivity(i);
    }


}
