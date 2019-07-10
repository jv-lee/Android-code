package com.lee.library.base;

import android.app.Application;
import android.util.Log;

import com.lee.library.utils.Constants;

/**
 * @author jv.lee
 * @date 2019/7/10.
 * @description
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(Constants.TAG, "common/BaseApplication");
    }
}
