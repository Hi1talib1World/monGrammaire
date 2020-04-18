package com.example.mongrammaire.horisontal_cardv.favorites;

import android.support.v4.app.Fragment;

public class FavoriteListFragment extends Fragment {
    public static final String ARG_ITEM_ID = "favorite_list";

    ListView favoriteList;
    SharedPreference sharedPreference;
    List<Product> favorites;

    Activity activity;
    ProductListAdapter productListAdapter;
}
