package com.rajaraman.quick_settings;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private boolean mDataConnEnabled; // To hold the data connection toggle status
    private static Context mContext;

    public boolean isDataConnEnabled() {
        return mDataConnEnabled;
    }

    public void setDataConnStatus(boolean enabled) {
        mDataConnEnabled = enabled;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}