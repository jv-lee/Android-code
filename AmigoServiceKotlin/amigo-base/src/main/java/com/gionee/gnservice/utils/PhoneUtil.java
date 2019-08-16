package com.gionee.gnservice.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by caocong on 5/15/17.
 */
public final class PhoneUtil {
    private static final String TAG = "PhoneUtil";

    private PhoneUtil() {
        throw new RuntimeException("stub");
    }

    public static String getModel(Context context) {
        return Build.MODEL;
    }

    public static String getIMEI(Context context) {
        String imei = "";
        imei = getSystemProperty("persist.sys.gn.imei0");
        if (TextUtils.isEmpty(imei)) {
            imei = getSystemProperty("persist.radio.imei");
        }
        if (TextUtils.isEmpty(imei)) {
            TelephonyManager phoneMgr = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            imei = phoneMgr.getDeviceId();
        }
        LogUtil.d(TAG, "imei = " + imei);
        return imei;
    }

    /**
     * 是否支持系统弹框
     */
    public static boolean isSupportSystemPermissionAlert() {
        String value = getSystemProperty("ro.gn.sys_perm_alert.support");
        boolean isSupport = "yes".equalsIgnoreCase(value);
        LogUtil.e("get isSupportSystemPermission value=" + isSupport);
        return isSupport;
    }

    /**
     * 是否移动定制机
     */
    public static boolean isMobileCustomizationVersion() {
        String value = getSystemProperty("ro.gn.custom.operators");
        boolean result = "cmcc_strategy".equalsIgnoreCase(value);
        LogUtil.d("get isMobileCustomizationVersion value=" + result);
        return result;
    }


    @SuppressWarnings("unchecked")
    private static String getSystemProperty(String key) {
        try {
            Class localClass = Class.forName("android.os.SystemProperties");
            Method localMethod = localClass.getDeclaredMethod("get", String.class);
            localMethod.setAccessible(true);
            String value = (String) localMethod.invoke(localClass, key);
            LogUtil.d(TAG, "get system property is:" + value);
            return value;
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return "";
        }
        if (!checkPhoneNumber(phoneNumber)) {
            LogUtil.d(TAG, "is not phone number");
            return phoneNumber;
        }
        LogUtil.d(TAG, "is phone number");
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, phoneNumber.length());
    }

    // update: 2017-09-26
    private static boolean checkPhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^((13[0-9])" +
                "|(14[5,7,9])" +
                "|(15[^4,\\D])" +
                "|(17[3,5-8])" +
                "|(18[0-9]))" +
                "\\d{8}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }


    public static String getPackageName(Context context) {
        return context.getPackageName();
    }


    //ExternalStorage是否存在
    public static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static int recoverRemovableApp(Context appContext, String pkgName) {
        int result = -1;
        try {
            PackageManager pm = appContext.getPackageManager();
            Class<?> cls = Class.forName("android.content.pm.PackageManager");
            Method method = cls.getDeclaredMethod("AmigoRecoverRemovableApp", String.class, int.class);
            method.setAccessible(true);
            result = (int)method.invoke(pm, pkgName, myUserId());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static int myUserId() {
        int userId = -1;
        try {
            Class<?> cls = Class.forName("android.os.UserHandle");
            Method method = cls.getDeclaredMethod("myUserId");
            method.setAccessible(true);
            userId = (int) method.invoke(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static boolean isRunningByMonkey() {
        boolean testByMonkey = false;
        try {
            testByMonkey = ActivityManager.isUserAMonkey();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return testByMonkey;
    }

}
