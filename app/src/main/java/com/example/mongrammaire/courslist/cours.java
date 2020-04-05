package com.example.mongrammaire.courslist;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.mongrammaire.cards.Model;
import com.example.mongrammaire.cards.MyAdapter;
import com.example.mongrammaire.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link cours.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link cours#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cours extends Fragment implements AdapterView.OnItemClickListener,SearchView.OnQueryTextListener {


    public MyAdapter adapter;
    SearchView searchView;

    private OnFragmentInteractionListener mListener;
    //the recyclerview
    RecyclerView recyclerView;

    // TODO: Rename and change types and number of parameters
    public static cours newInstance(String param1, String param2) {
        cours fragment = new cours();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    //a list to store all the products
    List<Model> models;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cours, container, false);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter = new MyAdapter(getActivity(),getPlayesr());
        recyclerView.setAdapter(adapter);
        //initializing the productlis

        return v;
    }

    private ArrayList<Model> getPlayesr(){
        models = new ArrayList<>();
        ArrayList<Model> models = new ArrayList<>();

        //adding some items to our list
        Model p = new Model();
        p.setTitle("accords au pluriel");
        p.setDescription("a");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("prépositions");
        p.setDescription("b");
        p.setImg(R.drawable.ufo);
        models.add(p);

        p = new Model();
        p.setTitle("adjectifs possessifs");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("impératif");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("articles (indéfinis, définis, partitifs)");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("passé composé");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("négation");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("imparfait");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("phrases conditionnelles avec \"si\"");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("temps du passé");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("pronoms relatifs, simples");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("passif");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("discours rapporté au passé");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        p = new Model();
        p.setTitle("subjonctif/indicatif");
        p.setDescription("b");
        p.setImg(R.drawable.satellite);
        models.add(p);

        return models;

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings){
            Toast.makeText(getActivity(), "Parametres", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
