package com.example.myapplication;

import android.app.Application;

import com.jeremyliao.liveeventbus.LiveEventBus;


public class MyApplication extends Application {

    private String baseurl="http://cyp.natapp1.cc/";
    public String getBaseurl() {
        return baseurl;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LiveEventBus.get()
                .config()
                .supportBroadcast(this)
                .lifecycleObserverAlwaysActive(true)
                .autoClear(false);
    }
}