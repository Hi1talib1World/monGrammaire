package com.example.mongrammaire.horisontal_cardv.categories.Mots;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mongrammaire.R;

import java.util.ArrayList;

public class MotsActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemListener {

    RecyclerView recyclerView;
    ArrayList arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mots);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        arrayList = new ArrayList();
        arrayList.add(new DataModel("Item 1", R.drawable.house, "#09A9FF"));
        arrayList.add(new DataModel("Item 2", R.drawable.house, "#3E51B1"));
        arrayList.add(new DataModel("Item 3", R.drawable.house, "#673BB7"));
        arrayList.add(new DataModel("Item 4", R.drawable.house, "#4BAA50"));
        arrayList.add(new DataModel("Item 5", R.drawable.house, "#F94336"));
        arrayList.add(new DataModel("Item 6", R.drawable.house, "#0A9B88"));
        arrayList.add(new DataModel("Item 7", R.drawable.house, "#09A9FF"));
        arrayList.add(new DataModel("Item 8", R.drawable.house, "#3E51B1"));
        arrayList.add(new DataModel("Item 9", R.drawable.house, "#673BB7"));
        arrayList.add(new DataModel("Item 10", R.drawable.house, "#4BAA50"));
        arrayList.add(new DataModel("Item 11", R.drawable.house, "#F94336"));
        arrayList.add(new DataModel("Item 12", R.drawable.house, "#0A9B88"));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, arrayList, this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

    }
    @Override
    public void onItemClick(DataModel item) {

        Toast.makeText(getApplicationContext(), item.text + " is clicked", Toast.LENGTH_SHORT).show();

    }
}
