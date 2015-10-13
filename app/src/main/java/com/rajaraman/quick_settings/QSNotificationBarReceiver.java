package com.rajaraman.quick_settings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.noveogroup.android.log.Log;

public class QSNotificationBarReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) {
            Log.d("intent is null. This is unexpected");
            return;
        }

        Log.i("onReceive called with " + intent.getAction());

        if (intent.getAction().equals(Constants.ACTION_WIFI_TOGGLE_CLICKED)) {
            String strText = intent.getExtras().getString(Constants.INTENT_EXTRA_WIDGET_TEXT);
            Log.d(strText);

            // Toggle Wi-Fi
            handleWifiToggle(context);

        } else if (intent.getAction().equals(Constants.ACTION_DATA_CONN_TOGGLE_CLICKED)) {
            String strText = intent.getExtras().getString(Constants.INTENT_EXTRA_WIDGET_TEXT);
            Log.d(strText);

            // From Android 5.0, the private class which is used to programmatically toggle the
            // connection is not available via reflection, so just open the relevant activity from
            // where user will be select data connection
            // Toggle data connection
            //new DataConnToggleTask(context).execute();

            openMobileDataEnableSettingsPage(context);

        } else if (intent.getAction().equals(Constants.ACTION_2G_3G_DATA_CONN_TOGGLE_CLICKED)) {
            String strText = intent.getExtras().getString(Constants.INTENT_EXTRA_WIDGET_TEXT);
            Log.d(strText);

            openMobile2g3gEnableSettingsPage(context);

        } else if (intent.getAction().equals(Constants.ACTION_AIRPLANE_MODE_TOGGLE_CLICKED)) {

            String strText = intent.getExtras().getString(Constants.INTENT_EXTRA_WIDGET_TEXT);
            Log.d(strText);

            // From 4.2, system settings in read only and Airplane mode is under system settings
            // http://stackoverflow.com/questions/5533881/toggle-airplane-mode-in-android
            // so you can open only the wireless network settings page
            openWirelessNetworkSettingsPage(context);
        }
    }

    // Handle Wi-Fi toggle action
    void handleWifiToggle(Context context) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

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
//  void handleDataConnToggle(Context context, boolean toggleStatus) {
//
//    final ConnectivityManager cm =
//        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//    Class cmClass = null;
//
//    try {
//      cmClass = Class.forName(cm.getClass().getName());
//    } catch (ClassNotFoundException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//
//    Field cmField = null;
//
//    try {
//      cmField = cmClass.getDeclaredField("mService");
//    } catch (NoSuchFieldException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//
//    cmField.setAccessible(true);
//
//    Object objCm = null;
//
//    try {
//      objCm = cmField.get(cm);
//    } catch (IllegalArgumentException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (IllegalAccessException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//
//    Class objCmClass = null;
//
//    try {
//      objCmClass = Class.forName(objCm.getClass().getName());
//    } catch (ClassNotFoundException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//
//    Method setMobileDataEnabledMethod = null;
//
//    try {
//      setMobileDataEnabledMethod =
//          objCmClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
//    } catch (NoSuchMethodException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//
//    setMobileDataEnabledMethod.setAccessible(true);
//
//    try {
//      setMobileDataEnabledMethod.invoke(objCm, toggleStatus);
//    } catch (IllegalArgumentException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (IllegalAccessException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (InvocationTargetException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//
//    // If you are here that means setMobileDataEnabledMethod method
//    // invocation through reflection has been successful and remember the value in the
//    // application object
//    // Get the application object. Remember the context is a broadcast
//    // receiver so use getApplicationContext to get the application object
//    MyApplication myApp = (MyApplication) context.getApplicationContext();
//    myApp.setDataConnStatus(toggleStatus);
//
//    // Change the notification bar data connection icon as well
//    Utils utils = Utils.getInstance();
//    utils.setContext(context.getApplicationContext());
//
//    if (toggleStatus)
//      utils.showPersistentNotification(Utils.NOTIFICATION_DATA_CONN_ENABLED);
//    else
//      utils.showPersistentNotification(Utils.NOTIFICATION_DATA_CONN_DISABLED);
//  }

    // Open the relevant system activity from where user will enable the data connection
    void openMobileDataEnableSettingsPage(Context context) {

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings",
                "com.android.settings.Settings$DataUsageSummaryActivity"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        collapseStatusBar(context);
    }

    // Open the relevant system activity from where user will enable 2G/3G
    void openMobile2g3gEnableSettingsPage(Context context) {

        Intent intent = new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
        intent.setClassName("com.android.phone", "com.android.phone.MobileNetworkSettings");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        collapseStatusBar(context);
    }

    void openWirelessNetworkSettingsPage(Context context) {

        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        collapseStatusBar(context);
    }

    // This is hacky !!!
    void collapseStatusBar(Context context) {
        // Get access to system status bar. This is not public so may be deprecated in the future
        Object service = context.getSystemService("statusbar");

        Class<?> statusbarManager = null;

        try {
            statusbarManager = Class.forName("android.app.StatusBarManager");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Till 4.1 (JB), StatusBarManager collapse method did the job but from 4.2 it's been
        // changed to collpasePanels, so perform the job accordingly
        if (Build.VERSION.SDK_INT <= 16) {
            Method collapseMethod = null;

            try {
                collapseMethod = statusbarManager.getMethod("collapse");
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            collapseMethod.setAccessible(true);

            try {
                collapseMethod.invoke(service);
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
        } else {
            Method collapsePanelsMethod = null;

            try {
                collapsePanelsMethod = statusbarManager.getMethod("collapsePanels");
            } catch (NoSuchMethodException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            collapsePanelsMethod.setAccessible(true);

            try {
                collapsePanelsMethod.invoke(service);
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
        }
    }

//  private class DataConnToggleTask extends AsyncTask<Void, Void, Void> {
//
//    // Use this to hold the values from the caller
//    Context context;
//
//    DataConnToggleTask(Context context) {
//      this.context = context;
//    }
//
//    @Override
//    protected Void doInBackground(Void... nothing) {
//
//      // Get the application object. Remember the context is a broadcast
//      // receiver so use
//      // getApplicationContext to get the application object
//      MyApplication myApp = (MyApplication) context.getApplicationContext();
//
//      if (myApp.isDataConnEnabled())
//        handleDataConnToggle(context, false);
//      else
//        handleDataConnToggle(context, true);
//      return null;
//    }
//  }
}
