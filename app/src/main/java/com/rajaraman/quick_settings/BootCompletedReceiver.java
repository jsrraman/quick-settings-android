package com.rajaraman.quick_settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.noveogroup.android.log.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Boot completed event received");

        // TODO: move mQSNotificationBarIntent to application context so that this can be used
        // while stopping the service
        Intent mQSNotificationBarIntent = new Intent(context, QSNotificationBarService.class);
        context.startService(mQSNotificationBarIntent);
    }
}
