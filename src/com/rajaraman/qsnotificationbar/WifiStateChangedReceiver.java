package com.rajaraman.qsnotificationbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiStateChangedReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    // TODO Auto-generated method stub

    if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {

      Log.d(Constants.TAG, "onReceive called with WifiManager.WIFI_STATE_CHANGED_ACTION");

      int iWifiState =
          intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

      Utils utils = Utils.getInstance();

      // Set the proper context. Remember this is a broad cast receiver,
      // so use
      // getApplicationContext
      utils.setContext(context.getApplicationContext());

      switch (iWifiState) {
        case WifiManager.WIFI_STATE_DISABLED: {
          Log.d(Constants.TAG, "WIFI_STATE_DISABLED");

          utils.showPersistentNotification(Utils.NOTIFICATION_WIFI_STATE_DISABLED);

          break;
        }

        case WifiManager.WIFI_STATE_DISABLING: {
          Log.d(Constants.TAG, "WIFI_STATE_DISABLING");
          break;
        }

        case WifiManager.WIFI_STATE_ENABLED: {
          Log.d(Constants.TAG, "WIFI_STATE_ENABLED");

          utils.showPersistentNotification(Utils.NOTIFICATION_WIFI_STATE_ENABLED);

          break;
        }

        case WifiManager.WIFI_STATE_ENABLING: {
          Log.d(Constants.TAG, "WIFI_STATE_ENABLING");
          break;
        }

        default:
        case WifiManager.WIFI_STATE_UNKNOWN: {
          Log.d(Constants.TAG, "WIFI_STATE_UNKNOWN");
          break;
        }
      }
    }
  }
}
