package com.rajaraman.quick_settings;

public class NetworkStatus {
    // Network status constants
    public static final int NETWORK_WIFI_CONN_OFF = 0;
    public static final int NETWORK_WIFI_CONN_ON = 1;
    public static final int NETWORK_DATA_CONN_OFF = 2;
    public static final int NETWORK_DATA_CONN_ON = 3;
    public static final int NETWORK_2G_3G_DATA_CONN_BOTH_OFF = 4;
    public static final int NETWORK_2G_3G_DATA_CONN_2G_ON = 5;
    public static final int NETWORK_2G_3G_DATA_CONN_3G_ON = 6;
    public static final int NETWORK_AIRPLANE_MODE = 7;

    private int wifiState;
    private int dataConnState;
    private int dataConnSubTypeState;
    private int airplaneModeState;

    public int getWifiState() {
        return wifiState;
    }

    public void setWifiState(int wifiState) {
        this.wifiState = wifiState;
    }

    public int getDataConnState() {
        return dataConnState;
    }

    public void setDataConnState(int dataConnState) {
        this.dataConnState = dataConnState;
    }

    public int getDataConnSubTypeState() {
        return dataConnSubTypeState;
    }

    public void setDataConnSubTypeState(int dataConnSubTypeState) {
        this.dataConnSubTypeState = dataConnSubTypeState;
    }

    public int getAirplaneModeState() {
        return airplaneModeState;
    }

    public void setAirplaneModeState(int airplaneModeState) {
        this.airplaneModeState = airplaneModeState;
    }
}