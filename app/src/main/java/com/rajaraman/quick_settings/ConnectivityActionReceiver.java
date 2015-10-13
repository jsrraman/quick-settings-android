package com.rajaraman.quick_settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.noveogroup.android.log.Log;

public class ConnectivityActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.d("onReceive called with ConnectivityManager.CONNECTIVITY_ACTION");

            Utils utils = Utils.getInstance();

            // Set the proper context
            utils.setContext(context.getApplicationContext());

            // Update persistent notification bar
            utils.updatePersistentNotificationBar();
        }
    }
}