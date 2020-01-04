package com.example.mongrammaire;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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
public class cours extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ProductAdapter adapter;
    private OnFragmentInteractionListener mListener;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cours.
     */
    // TODO: Rename and change types and number of parameters
    public static cours newInstance(String param1, String param2) {
        cours fragment = new cours();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    //a list to store all the products
    List<product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


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

        //initializing the productlist
        productList = new ArrayList<>();


        //adding some items to our list
        productList.add(
                new product(
                        1,
                        "Niveau 1",
                        "statut :",
                        4.3,
                        60000,
                        R.drawable.galaxy));

        productList.add(
                new product(
                        1,
                        "Niveau 2",
                        "statut :",
                        4.3,
                        60000,
                        R.drawable.satellite));

        productList.add(
                new product(
                        1,
                        "Niveau 3",
                        "statut :",
                        4.3,
                        60000,
                        R.drawable.ufo));
        productList.add(
                new product(
                        1,
                        "Niveau 4",
                        "statut :",
                        4.3,
                        60000,
                        R.drawable.ufo));
        productList.add(
                new product(
                        1,
                        "Niveau 5",
                        "statut :",
                        4.3,
                        60000,
                        R.drawable.ufo));
        productList.add(
                new product(
                        1,
                        "Niveau 6",
                        "statut :",
                        4.3,
                        60000,
                        R.drawable.ufo));
        productList.add(
                new product(
                        1,
                        "Niveau 7",
                        "statut :",
                        4.3,
                        60000,
                        R.drawable.ufo));

        //creating recyclerview adapter
        adapter = new ProductAdapter(getActivity(), productList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        return v;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
