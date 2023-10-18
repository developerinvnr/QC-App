package com.example.qc.broadcast;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(CheckInternetConnectionBroadcast.ConnectivityReceiverListener listener) {
        CheckInternetConnectionBroadcast.connectivityReceiverListener = listener;
    }
}
