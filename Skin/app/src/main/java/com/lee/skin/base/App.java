package com.lee.skin.base;

import android.app.Application;

import com.lee.library.SkinManager;

/**
 * @author jv.lee
 * @date 2019-07-21
 * @description
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
