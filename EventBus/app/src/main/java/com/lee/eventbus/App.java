package com.lee.eventbus;

import android.app.Application;

import lee.eventbus.apt.EventBusIndex;
import lee.eventbus.core.EventBus;

/**
 * @author jv.lee
 * @date 2019-08-18
 * @description
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().addIndex(new EventBusIndex());
    }
}
