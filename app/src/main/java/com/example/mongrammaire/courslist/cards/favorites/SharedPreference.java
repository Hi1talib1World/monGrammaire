package com.example.mongrammaire.courslist.cards.favorites;

import android.content.Context;
import android.content.SharedPreferences;

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

    public void saveFavorites(Context context, List<Model> favorites) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(FAVORITES, jsonFavorites);
        editor.apply();
    }

    public void addFavorite(Context context, Model product) {
        ArrayList<Model> favorites = getFavorites(context);
        if (favorites == null) favorites = new ArrayList<>();
        
        for (Model m : favorites) {
            if (m.getTitle().equals(product.getTitle())) return;
        }
        
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Model product) {
        ArrayList<Model> favorites = getFavorites(context);
        if (favorites != null) {
            Model toRemove = null;
            for (Model m : favorites) {
                if (m.getTitle().equals(product.getTitle())) {
                    toRemove = m;
                    break;
                }
            }
            if (toRemove != null) {
                favorites.remove(toRemove);
                saveFavorites(context, favorites);
            }
        }
    }

    public ArrayList<Model> getFavorites(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Model[] favoriteItems = gson.fromJson(jsonFavorites, Model[].class);

            if (favoriteItems == null) return new ArrayList<>();
            return new ArrayList<>(Arrays.asList(favoriteItems));
        } else {
            return new ArrayList<>();
        }
    }
}
