package com.example.mongrammaire.settingsPackage;

import android.app.Application;

public class MyApplication  extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this))
        {
            return;
        }

        LeakCanary.install(this);
    }
}