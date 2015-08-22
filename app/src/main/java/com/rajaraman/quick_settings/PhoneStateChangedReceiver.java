package com.rajaraman.quick_settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;


public class PhoneStateChangedReceiver extends BroadcastReceiver {

    Bundle mExtras = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        Log.d(Constants.TAG, "Phoone state change event received");

        mExtras = intent.getExtras();

        if (mExtras != null) {

            String state = mExtras.getString(TelephonyManager.EXTRA_STATE);

            // If there is an ongoing call
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            }
            // If the call has been attended
            else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                String phoneNumber = mExtras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                // Get the contact name for this number
                String contactName = Utils.getInstance().getContactName(context, phoneNumber);

                Log.d(Constants.TAG, "Missed call from -" + contactName);
            }
        }
    }
}
