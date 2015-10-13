package com.rajaraman.quick_settings;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.noveogroup.android.log.Log;

public class QSNotificationBarService extends Service {
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
        Log.d("QSNotificationBar Service started");

        setupTelephonyManagerListener();

        Utils utils = Utils.getInstance();

        // Set the proper context.
        utils.setContext(getApplicationContext());

        // Update persistent notification bar
        utils.updatePersistentNotificationBar();

        return Service.START_STICKY; // Revisit this flag later
    }

    private void setupTelephonyManagerListener() {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(new PhoneDataConnectionStateListener(),
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
    }

    private class PhoneDataConnectionStateListener extends PhoneStateListener {
        @Override
        public void onDataConnectionStateChanged(int state, int networkType) {
            super.onDataConnectionStateChanged(state, networkType);
            Log.d("Network Type->" + networkType + ",State->" + state);

            Utils utils = Utils.getInstance();

            // Set the proper context.
            utils.setContext(getApplicationContext());

            // Update persistent notification bar
            utils.updatePersistentNotificationBar();
        }
    }
}