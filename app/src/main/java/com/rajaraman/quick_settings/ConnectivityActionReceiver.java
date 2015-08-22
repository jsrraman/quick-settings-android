package com.rajaraman.quick_settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class ConnectivityActionReceiver extends BroadcastReceiver {

    private static final String TAG = ConnectivityActionReceiver.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

            Log.d(Constants.TAG, "onReceive called with ConnectivityManager.CONNECTIVITY_ACTION");

            Utils utils = Utils.getInstance();

            // Set the proper context.
            utils.setContext(context.getApplicationContext());

            // Update persistent notification bar
            utils.updatePersistentNotificationBar();
        }
    }
}