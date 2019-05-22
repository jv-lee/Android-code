package com.lee.code.andfix;

import android.app.Application;

/**
 * @author jv.lee
 * @date 2019-05-22
 */
public class App extends Application {

    /**
     * 对象声明在JVM堆区
     */
    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
