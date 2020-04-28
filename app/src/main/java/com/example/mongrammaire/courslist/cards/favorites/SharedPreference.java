package com.example.mongrammaire.courslist.cards.favorites;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mongrammaire.courslist.cards.Model;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {
    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Model> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Model Model) {
        List<Model> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Model>();
        favorites.add(Model);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Model Model) {
        ArrayList<Model> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(Model);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Model> getFavorites(Context context) {
        SharedPreferences settings;
        List<Model> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Model[] favoriteItems = gson.fromJson(jsonFavorites,
                    Model[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Model>(favorites);
        } else
            return null;

        return (ArrayList<Model>) favorites;
    }
}
