package com.rajaraman.qsnotificationbar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class QSNotificationBarService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
	Log.d(Constants.TAG, "QSNotificationBar Service started");

	Utils utils = Utils.getInstance();

	// Set the proper context.
	utils.setContext(getApplicationContext());

	// Create persistent notification
	utils.showPersistentNotification(Utils.NOTIFICATION_WIFI_STATE_DISABLED);

	return Service.START_STICKY; // Revisit this flag later
    }
}
