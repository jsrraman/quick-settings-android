
package com.rajaraman.qsnotificationbar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

public class QSBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(Constants.TAG, "onReceive called with " + intent.getAction());

        if (intent.getAction().equals(Constants.ACTION_WIFI_TOGGLE_CLICKED)) {
            String strText = intent.getExtras().getString(Constants.INTENT_EXTRA_WIDGET_TEXT);
            Log.d(Constants.TAG, strText);

            // Toggle Wi-Fi
            handleWifiToggle(context);

        } else if (intent.getAction().equals(Constants.ACTION_DATA_CONN_TOGGLE_CLICKED)) {
            String strText = intent.getExtras().getString(Constants.INTENT_EXTRA_WIDGET_TEXT);
            Log.d(Constants.TAG, strText);

            // Toggle data connection
            new DataConnToggleTask(context).execute();
        }
    }

    // Handle Wi-Fi toggle action
    void handleWifiToggle(Context context) {

        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        } else {
            wifiManager.setWifiEnabled(true);
        }
    }

    // Handle data connection toggle action
    // Note: This uses private class (at run time using reflection) to
    // enable/disable data connection...
    // May not work in the future :)
    void handleDataConnToggle(Context context, boolean toggleStatus) {

        final ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        Class cmClass = null;

        try {
            cmClass = Class.forName(cm.getClass().getName());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Field cmField = null;

        try {
            cmField = cmClass.getDeclaredField("mService");
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cmField.setAccessible(true);

        Object objCm = null;

        try {
            objCm = cmField.get(cm);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Class objCmClass = null;

        try {
            objCmClass = Class.forName(objCm.getClass().getName());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Method setMobileDataEnabledMethod = null;

        try {
            setMobileDataEnabledMethod = objCmClass.getDeclaredMethod("setMobileDataEnabled",
                    Boolean.TYPE);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        setMobileDataEnabledMethod.setAccessible(true);

        try {
            setMobileDataEnabledMethod.invoke(objCm, toggleStatus);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // If you are here that means setMobileDataEnabledMethod method
        // invocation through
        // reflection has been successful and remember the value in the
        // application object
        // Get the application object. Remember the context is a broadcast
        // receiver so use
        // getApplicationContext to get the application object
        MyApplication myApp = (MyApplication)context.getApplicationContext();
        myApp.setDataConnStatus(toggleStatus);

        // Change the notification bar data connection icon as well
        Utils utils = Utils.getInstance();
        utils.setContext(context.getApplicationContext());

        if (toggleStatus)
            utils.showPersistentNotification(Utils.NOTIFICATION_DATA_CONN_ENABLED);
        else
            utils.showPersistentNotification(Utils.NOTIFICATION_DATA_CONN_DISABLED);
    }

    private class DataConnToggleTask extends AsyncTask<Void, Void, Void> {

        // Use this to hold the values from the caller
        Context context;

        DataConnToggleTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... nothing) {

            // Get the application object. Remember the context is a broadcast
            // receiver so use
            // getApplicationContext to get the application object
            MyApplication myApp = (MyApplication)context.getApplicationContext();

            if (myApp.isDataConnEnabled())
                handleDataConnToggle(context, false);
            else
                handleDataConnToggle(context, true);
            return null;
        }
    }
}
