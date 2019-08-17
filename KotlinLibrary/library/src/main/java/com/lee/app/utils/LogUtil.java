package com.lee.app.utils;

import android.util.Log;


/**
 * @author jv.lee
 * on 2016/8/31.
 */
public class LogUtil {

    private final static boolean IS_DEBUG = true;
    private final static String TAG = "reader>>>";

    public static void getStackTraceString(Throwable throwable) {
        if (throwable != null) {
            Log.i(getTag(), Log.getStackTraceString(throwable));
        }
    }

    public static void i(String msg) {
        if (IS_DEBUG) {
            if (msg != null) {
                Log.i(getTag(), msg);
            }
        }
    }

    public static void w(String msg) {
        if (IS_DEBUG) {
            if (msg != null) {
                Log.w(getTag(), msg);
            }
        }
    }

    public static void e(String msg) {
        if (IS_DEBUG) {
            if (msg != null) {
                Log.e(getTag(), msg);
            }
        }
    }

    public static void d(String msg) {
        if (IS_DEBUG) {
            if (msg != null) {
                Log.d(getTag(), msg);
            }
        }
    }

    public static void v(String msg) {
        if (IS_DEBUG) {
            if (msg != null) {
                Log.v(getTag(), msg);
            }
        }
    }

    private static String getTag() {
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        String callingClass = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(Log.class)) {
                callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                break;
            }
        }
        return TAG + callingClass;
    }

}
