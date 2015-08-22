package com.rajaraman.quick_settings;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class Utils {

    public static final int NOTIFICATION_ID = 1;

    // Supported toggles
    public static final int NOTIFICATION_WIFI_TOGGLE = 0;
    public static final int NOTIFICATION_DATA_CONN_TOGGLE = 1;
    public static final int NOTIFICATION_2G_3G_DATA_CONN_TOGGLE = 2;
    public static final int NOTIFICATION_AIRPLANE_MODE_TOGGLE = 3;

    public static final String TAG = "QSNotificationBar";

    private static Utils mUtils = null;

    private Context mContext = null;

    private NotificationManager mNotificationManager = null;

    private Notification mNotification = null;

    private Utils() {
    }

    public static Utils getInstance() {
        if (mUtils == null) {
            mUtils = new Utils();
        }

        return mUtils;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void showPersistentNotification(NetworkStatus networkStatus) {

        // Build the notification - Builder pattern :)
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(mContext).setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Quick Settings")
                        .setContentText("Quick Settings Notification");

        // Get the notification object
        mNotification = notificationBuilder.build();

        // Create the content view to be displayed in the notification bar
        RemoteViews contentView =
                new RemoteViews(mContext.getPackageName(), R.layout.custom_notification_layout);

        // Set up action for the individual control clicks in the controls of
        // the custom layout
        setupActionForNotificationContentViewControlClicks(contentView, networkStatus);

        mNotification.contentView = contentView;

        // Create the intent to be displayed when the notification area is
        // clicked
        final Intent notificationIntent = new Intent(mContext, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        mNotification.contentIntent = contentIntent;

        // Set the notification flags Notification should not be cleared
        mNotification.flags |= Notification.FLAG_NO_CLEAR;

        // Get the notification manager
        mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Notify the user
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    private void setupActionForNotificationContentViewControlClicks(RemoteViews contentView,
                                                                    NetworkStatus networkStatus) {

        // Set the icons based on system state
        int imgviewWifiId;
        int imgviewDataConnId;
        int imgview2g3gDataConnId;
        int imgViewAirplaneModeId = R.drawable.ic_airplane_mode_off;

        // Set icon for Wi-Fi state
        if (networkStatus.getWifiState() == NetworkStatus.NETWORK_WIFI_CONN_OFF) {
            imgviewWifiId = R.drawable.ic_wifi_off;
        } else {
            imgviewWifiId = R.drawable.ic_wifi_on;
        }

        if (networkStatus.getDataConnState() == NetworkStatus.NETWORK_DATA_CONN_OFF) {
            imgviewDataConnId = R.drawable.ic_data_conn_off;
            imgview2g3gDataConnId = R.drawable.ic_2g_3g_data_conn_both_off;
        } else {
            imgviewDataConnId = R.drawable.ic_data_conn_on;

            if (networkStatus.getDataConnSubTypeState() == NetworkStatus.NETWORK_2G_3G_DATA_CONN_2G_ON) {
                imgview2g3gDataConnId = R.drawable.ic_2g_3g_data_conn_2g_on;
            } else {
                imgview2g3gDataConnId = R.drawable.ic_2g_3g_data_conn_3g_on;
            }
        }

        setupIntentInfoForNotificationContentViewControlClicks(NOTIFICATION_WIFI_TOGGLE, contentView,
                R.id.image_view_wifi, imgviewWifiId);

        setupIntentInfoForNotificationContentViewControlClicks(NOTIFICATION_DATA_CONN_TOGGLE,
                contentView, R.id.image_view_data_conn, imgviewDataConnId);

        setupIntentInfoForNotificationContentViewControlClicks(NOTIFICATION_2G_3G_DATA_CONN_TOGGLE,
                contentView, R.id.image_view_2g_3g_data_conn, imgview2g3gDataConnId);

        setupIntentInfoForNotificationContentViewControlClicks(NOTIFICATION_AIRPLANE_MODE_TOGGLE,
                contentView, R.id.image_view_airplane_mode, imgViewAirplaneModeId);
    }

    public void cancelPersistentNotification() {
        // TODO Auto-generated method stub
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    void setupIntentInfoForNotificationContentViewControlClicks(int iNotificationToggle,
                                                                RemoteViews contentView, int iResourceId, int iResourceImageViewId) {

        Intent intent = new Intent(mContext, QSNotificationBarReceiver.class);


        // Setup intent action for the respective toggle click
        switch (iNotificationToggle) {

            case NOTIFICATION_WIFI_TOGGLE: {
                intent.setAction(Constants.ACTION_WIFI_TOGGLE_CLICKED);
                intent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT, "Wi-Fi Button Clicked");
                break;
            }

            case NOTIFICATION_DATA_CONN_TOGGLE: {
                intent.setAction(Constants.ACTION_DATA_CONN_TOGGLE_CLICKED);
                intent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT, "Data Connection Button Clicked");
                break;
            }

            case NOTIFICATION_2G_3G_DATA_CONN_TOGGLE: {
                intent.setAction(Constants.ACTION_2G_3G_DATA_CONN_TOGGLE_CLICKED);
                intent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT, "2G/3G Data Connection Button Clicked");
                break;
            }

            case NOTIFICATION_AIRPLANE_MODE_TOGGLE: {
                intent.setAction(Constants.ACTION_AIRPLANE_MODE_TOGGLE_CLICKED);
                intent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT, "Airplane Mode Button Clicked");
                break;
            }
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        contentView.setOnClickPendingIntent(iResourceId, pendingIntent);
        contentView.setImageViewResource(iResourceId, iResourceImageViewId);
    }

    public NetworkStatus getNetworkStatus() {

        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkStatus networkStatus = new NetworkStatus();

        // Wi-Fi connection state
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo.DetailedState wifiInfoDetailedState = wifiInfo.getDetailedState();
        Log.d(TAG, "Wifi Connection State = " + wifiInfoDetailedState.toString());

        if (wifiInfoDetailedState == NetworkInfo.DetailedState.DISCONNECTED) {
            networkStatus.setWifiState(NetworkStatus.NETWORK_WIFI_CONN_OFF);
        } else {
            networkStatus.setWifiState(NetworkStatus.NETWORK_WIFI_CONN_ON);
        }

        // Data connection state
        NetworkInfo dataConnInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo.DetailedState dataConnInfoDetailedState = dataConnInfo.getDetailedState();
        Log.d(TAG, "Data Connection State = " + dataConnInfoDetailedState.toString());

        if (dataConnInfoDetailedState == NetworkInfo.DetailedState.DISCONNECTED) {
            networkStatus.setDataConnState(NetworkStatus.NETWORK_DATA_CONN_OFF);
            networkStatus.setDataConnSubTypeState(NetworkStatus.NETWORK_2G_3G_DATA_CONN_BOTH_OFF);
        } else {
            networkStatus.setDataConnState(NetworkStatus.NETWORK_DATA_CONN_ON);

            Log.d(TAG, "Data Connection Sub Type = " + dataConnInfo.getSubtypeName());

            if (dataConnInfo.getSubtypeName().equals("EDGE"))
                networkStatus.setDataConnSubTypeState(NetworkStatus.NETWORK_2G_3G_DATA_CONN_2G_ON);
            else
                networkStatus.setDataConnSubTypeState(NetworkStatus.NETWORK_2G_3G_DATA_CONN_3G_ON);
        }

        // Airplane mode
        networkStatus.setAirplaneModeState(NetworkStatus.NETWORK_AIRPLANE_MODE);

        return networkStatus;
    }

    public void updatePersistentNotificationBar() {
        NetworkStatus networkStatus = getNetworkStatus();
        showPersistentNotification(networkStatus);
    }

    public String getContactName(Context context, String phoneNumber) {

        ContentResolver cr = context.getContentResolver();

        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor == null) {
            return null;
        }

        String contactName = null;

        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }
}
