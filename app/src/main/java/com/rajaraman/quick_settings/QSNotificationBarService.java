package com.rajaraman.quick_settings;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class QSNotificationBarService extends Service {

    private static final String TAG = QSNotificationBarService.class.getCanonicalName();

    //private int NETWORK_STATUS_CHECK_FREQUENCY = 1000; // 1 second
    //private Timer timer = new Timer();

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

       // timer.scheduleAtFixedRate(new NetworkStatusCheckTimerTask(), 0, NETWORK_STATUS_CHECK_FREQUENCY);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.TAG, "QSNotificationBar Service started");

        Utils utils = Utils.getInstance();

        // Set the proper context.
        utils.setContext(getApplicationContext());

        // Update persistent notification bar
        utils.updatePersistentNotificationBar();

        return Service.START_STICKY; // Revisit this flag later
    }

//    // Poll the network status at the defined NETWORK_STATUS_CHECK_FREQUENCY in order to
//    // update the data connection sub type (i.e 2G/3G). The ConnectivityManager.CONNECTIVITY_ACTION
//    // current does not provide an event for change in 2G/3G while data connection is ON.
//    private class NetworkStatusCheckTimerTask extends TimerTask
//    {
//        public void run()
//        {
//            Log.d(TAG, "Inside NetworkStatusCheckTimerTask");
//
//            Utils utils = Utils.getInstance();
//
//            // Set the proper context.
//            utils.setContext(getApplicationContext());
//
//            // Update persistent notification bar
//            utils.updatePersistentNotificationBar();
//        }
//    }
}
