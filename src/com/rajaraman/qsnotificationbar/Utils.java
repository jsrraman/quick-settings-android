
package com.rajaraman.qsnotificationbar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class Utils {

    public static final int NOTIFICATION_ID = 1;

    // Toggle related constants
    public static final int NOTIFICATION_DEFAULT = 0;

    public static final int NOTIFICATION_WIFI_STATE_DISABLED = 1;

    public static final int NOTIFICATION_WIFI_STATE_ENABLED = 2;

    public static final int NOTIFICATION_DATA_CONN_DISABLED = 3;

    public static final int NOTIFICATION_DATA_CONN_ENABLED = 4;

    public static final String TAG = "QSNotificationBar";

    private static Utils mUtils = null;

    private Context mContext = null;

    WifiStateChangedBroadCastReceiver objWifiStateChangedBroadCastReceiver;

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

    public void showPersistentNotification(int notificationBarState) {
        // Build the notification - Builder pattern :)
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_launcher).setContentTitle("Sample Notification")
                .setContentText("This is a sample notification");

        // Get the notification object
        mNotification = notificationBuilder.build();

        // Create the content view to be displayed in the notification bar
        RemoteViews contentView = new RemoteViews(mContext.getPackageName(),
                R.layout.custom_notification_layout);

        // Set up action for the individual control clicks in the controls of
        // the custom layout
        setupActionForNotificationContentViewControlClicks(contentView, notificationBarState);

        mNotification.contentView = contentView;

        // Create the intent to be displayed when the notification area is
        // clicked
        final Intent notificationIntent = new Intent(mContext, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        mNotification.contentIntent = contentIntent;

        // Set the notification flags
        // Notification should not be cleared
        mNotification.flags |= Notification.FLAG_NO_CLEAR;

        // Get the notification manager
        mNotificationManager = (NotificationManager)mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Notify the user
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    private void setupActionForNotificationContentViewControlClicks(RemoteViews contentView,
            int notificationBarState) {

        // Set the image based on system state
        int imgviewWifiId = R.drawable.ic_wifi_off;
        int imgviewDataConnId = R.drawable.ic_data_conn_off;

        switch (notificationBarState) {

            case NOTIFICATION_DEFAULT: {
                imgviewWifiId = R.drawable.ic_wifi_off;
                imgviewDataConnId = R.drawable.ic_data_conn_off;
                break;
            }

            case NOTIFICATION_WIFI_STATE_DISABLED: {
                imgviewWifiId = R.drawable.ic_wifi_off;
                break;
            }

            case NOTIFICATION_WIFI_STATE_ENABLED: {
                imgviewWifiId = R.drawable.ic_wifi_on;
                break;
            }

            case NOTIFICATION_DATA_CONN_DISABLED: {
                imgviewDataConnId = R.drawable.ic_data_conn_off;
                break;
            }

            case NOTIFICATION_DATA_CONN_ENABLED: {
                imgviewDataConnId = R.drawable.ic_data_conn_on;
                break;
            }
        }

        // Setup action for Wi-Fi button click
        Intent wifiIntent = new Intent(mContext, QSBroadCastReceiver.class);
        wifiIntent.setAction(Constants.ACTION_WIFI_TOGGLE_CLICKED);
        wifiIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT, "Wi-Fi Button Clicked");

        PendingIntent wifiPendingIntent = PendingIntent.getBroadcast(mContext, 0, wifiIntent, 0);
        contentView.setOnClickPendingIntent(R.id.image_view_wifi, wifiPendingIntent);
        contentView.setImageViewResource(R.id.image_view_wifi, imgviewWifiId);

        // Setup action for Data button click
        Intent dataConnIntent = new Intent(mContext, QSBroadCastReceiver.class);
        dataConnIntent.setAction(Constants.ACTION_DATA_CONN_TOGGLE_CLICKED);
        dataConnIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT,
                "Data Connection Button Clicked");

        PendingIntent dataConnPendingIntent = PendingIntent.getBroadcast(mContext, 0,
                dataConnIntent, 0);
        contentView.setOnClickPendingIntent(R.id.image_view_data_conn, dataConnPendingIntent);
        contentView.setImageViewResource(R.id.image_view_data_conn, imgviewDataConnId);
    }

    public void cancelPersistentNotification() {
        // TODO Auto-generated method stub
        mNotificationManager.cancel(NOTIFICATION_ID);
    }
}
