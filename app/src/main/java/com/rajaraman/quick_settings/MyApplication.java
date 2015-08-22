package com.rajaraman.quick_settings;

import android.app.Application;

public class MyApplication extends Application {
    private boolean mDataConnEnabled; // To hold the data connection toggle status


    public boolean isDataConnEnabled() {
        return mDataConnEnabled;
    }

    public void setDataConnStatus(boolean enabled) {
        mDataConnEnabled = enabled;
    }
}
