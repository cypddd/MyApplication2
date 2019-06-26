package com.example.myapplication;

import android.app.Application;

import com.jeremyliao.liveeventbus.LiveEventBus;


public class MyApplication extends Application {


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