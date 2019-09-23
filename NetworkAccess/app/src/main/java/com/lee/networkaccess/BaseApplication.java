package com.lee.networkaccess;

import android.app.Application;

import jv.lee.library.NetworkManager;

/**
 * @author jv.lee
 * @date 2019/9/23.
 * @description
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.getDefault().init(this);
    }
}
