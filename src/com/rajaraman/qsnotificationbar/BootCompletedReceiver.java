package com.rajaraman.qsnotificationbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {

    Log.d(Constants.TAG, "Boot completed event received");
    
    // TODO: move mQSNotificationBarIntent to application context so that this can be used
    // while stopping the service
    Intent mQSNotificationBarIntent = new Intent(context, QSNotificationBarService.class);
    context.startService(mQSNotificationBarIntent);
  }
}
