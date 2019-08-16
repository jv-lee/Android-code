package com.gionee.gnservice.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 用户中心sdk工具类，主要做版本兼容用
 */
public final class SdkUtil {
    private static final String TAG = SdkUtil.class.getSimpleName();
    public static final String AMIGO_SERVICE_PACKAGE_NAME = "com.gionee.gnservice";
    private static final long SUPPORT_SDK_VERSION_CODE = 2004000200;
    private static boolean mFromSdkDemo = false;

    private SdkUtil() {
    }

    public static void setFromSdkDemo(boolean from) {
        mFromSdkDemo = from;
    }

    /**
     * sdk是否是rom版本
     */
    public static boolean isSdkRomVersion() {
        try {
            Class clz = Class.forName("com.gionee.gnservice.sdk.member.MemberCardActivity");
            return clz == null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return true;
        }

    }

    public static boolean isSdkCommonVersion(){
        try {
            Class clz = Class.forName("com.gionee.gnservice.sdk.member.MemberCardActivity");
            return clz != null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 当前页面是否被sdk调用
     */
    public static boolean isCallBySdk(Context context) {
        if (mFromSdkDemo) {
            return true;
        }
        return !context.getPackageName().equals(AMIGO_SERVICE_PACKAGE_NAME);
    }


    /**
     * 当前的用户中心是否支持用户中心sdk
     */
    public static boolean hasSupportAmigoServiceVersion(Context context) {
        boolean value = checkVersionCode(context) && checkVersionName(context);
        LogUtil.d(TAG, "check amigo Service Version is Support SDK:" + value);
        return value;
    }

    private static int amigoServiceVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(AMIGO_SERVICE_PACKAGE_NAME, 0);
            LogUtil.d(TAG, "amigo service version code is:" + packageInfo.versionCode);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage());
            return -1;
        }
    }

    private static String amigoServiceVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(AMIGO_SERVICE_PACKAGE_NAME, 0);
            LogUtil.d(TAG, "amigo service version name is:" + packageInfo.versionName);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }


    private static boolean checkVersionCode(Context context) {
        return amigoServiceVersionCode(context) >= SUPPORT_SDK_VERSION_CODE;
    }

    private static boolean checkVersionName(Context context) {
        String name = amigoServiceVersionName(context);
        if (!TextUtils.isEmpty(name)) {
            String[] versions = name.split("\\.");
            if (versions.length == 4) {
                return Integer.valueOf(versions[0]) >= 4 && Integer.valueOf(versions[2]) >= 2;
            }
        }
        return false;
    }

}
