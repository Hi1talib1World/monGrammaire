package com.example.mongrammaire.courslist.cards.favorites;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mongrammaire.R;

import java.util.List;

public class FavoriteListFragment  extends Fragment {

    public static final String ARG_ITEM_ID = "favorite_list";

    ListView favoriteList;
    SharedPreference sharedPreference;
    List<Model> favorites;

    Activity activity;
    MyAdapter productListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container,
                false);
        // Get favorite items from SharedPreferences.
        sharedPreference = new SharedPreference();
        favorites = sharedPreference.getFavorites(activity);

        if (favorites == null) {
            showAlert(getResources().getString(R.string.no_favorites_items),
                    getResources().getString(R.string.no_favorites_msg));
        } else {

            if (favorites.size() == 0) {
                showAlert(
                        getResources().getString(R.string.no_favorites_items),
                        getResources().getString(R.string.no_favorites_msg));
            }


            /*if (favorites != null) {
                productListAdapter = new MyAdapter(activity, favorites);
                favoriteList.setAdapter(productListAdapter);

                favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View arg1,
                                            int position, long arg3) {

                    }
                });

                favoriteList
                        .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                            @Override
                            public boolean onItemLongClick(
                                    AdapterView<?> parent, View view,
                                    int position, long id) {

                                ImageView button = (ImageView) view
                                        .findViewById(R.id.imgbtn_favorite);

                                String tag = button.getTag().toString();
                                if (tag.equalsIgnoreCase("grey")) {
                                    sharedPreference.addFavorite(activity,
                                            favorites.get(position));
                                    Toast.makeText(
                                            activity,
                                            activity.getResources().getString(
                                                    R.string.add_favr),
                                            Toast.LENGTH_SHORT).show();

                                    button.setTag("red");
                                    button.setImageResource(R.drawable.red_heart);
                                } else {
                                    sharedPreference.removeFavorite(activity,
                                            favorites.get(position));
                                    button.setTag("grey");
                                    button.setImageResource(R.drawable.heart_grey);
                                    productListAdapter.remove(favorites
                                            .get(position));
                                    Toast.makeText(
                                            activity,
                                            activity.getResources().getString(
                                                    R.string.remove_favr),
                                            Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            }
                        });
            }*/
        }
        return view;
    }

    public void showAlert(String title, String message) {
        if (activity != null && !activity.isFinishing()) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);

            // setting OK Button
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // activity.finish();
                            getFragmentManager().popBackStackImmediate();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    public void onResume() {
        getActivity().setTitle(R.string.favorites);
        getActivity().getActionBar().setTitle(R.string.favorites);
        super.onResume();
    }
}