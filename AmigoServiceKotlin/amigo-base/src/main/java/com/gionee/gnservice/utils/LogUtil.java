package com.gionee.gnservice.utils;

import android.content.Context;

import com.gionee.gnservice.config.AppConfig;

/**
 * {@hide}
 */
public final class LogUtil {
    private static final String TAG = "Amigo_Service";
    private static final String TAG_SDK = "Amigo_Service_Sdk";
    private static String sTag = TAG;

    private LogUtil() {
        throw new RuntimeException("Stub!");
    }

    public static void d(String msg) {
        if (AppConfig.DEBUG) {
            android.util.Log.d(sTag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (AppConfig.DEBUG) {
            android.util.Log.d(sTag + "." + tag, msg);
        }
    }

    public static void i(String msg) {
        if (AppConfig.DEBUG) {
            android.util.Log.i(sTag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (AppConfig.DEBUG) {
            android.util.Log.i(sTag + "." + tag, msg);
        }
    }

    public static void e(String msg) {
        if (AppConfig.DEBUG) {
            android.util.Log.e(sTag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (AppConfig.DEBUG) {
            android.util.Log.e(sTag + "." + tag, msg);
        }
    }


    public static void initTag(Context context) {
        sTag = SdkUtil.isCallBySdk(context) ? TAG_SDK : TAG;
    }
}
