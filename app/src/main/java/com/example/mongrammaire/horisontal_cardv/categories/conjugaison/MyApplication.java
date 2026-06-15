package com.example.mongrammaire.horisontal_cardv.categories.conjugaison;


import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.color.DynamicColors;
import com.google.firebase.FirebaseApp;
import com.example.mongrammaire.notifications.NotificationHelper;
import com.orhanobut.hawk.Hawk;

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // Initialize Hawk for storage
        Hawk.init(this).build();

        // Initialize Notifications
        NotificationHelper.createNotificationChannel(this);
        NotificationHelper.scheduleDailyNotification(this);

        // Apply Material 3 Dynamic Colors
        DynamicColors.applyToActivitiesIfAvailable(this);

        // Initialize Firebase
        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize AdMob SDK
        new Thread(() -> {
            MobileAds.initialize(this, initializationStatus -> {});
        }).start();
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}