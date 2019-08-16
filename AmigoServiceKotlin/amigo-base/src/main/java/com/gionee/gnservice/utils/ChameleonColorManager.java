package com.gionee.gnservice.utils;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by borney on 7/4/17.
 */

public class ChameleonColorManager {
    private static final String TAG = "ChameleonColorManager";
    private static Class<?> sChameleonManager;
    private static HashMap<String, Method> sMethod = new HashMap<>();

    static {
        try {
            sChameleonManager = Class.forName("amigoui.changecolors.ChameleonColorManager");
        } catch (ClassNotFoundException e) {
            LogUtil.e(TAG, "not import amigoui lib");
        } catch (Exception e) {
            LogUtil.e(TAG, "reason: " + e.getMessage());
        }

        try {
            if (sChameleonManager == null) {
                sChameleonManager = Class.forName("amigo.changecolors.ChameleonColorManager");
            }
        } catch (ClassNotFoundException e) {
            LogUtil.e(TAG, "not import amigo lib");
        } catch (Exception e) {
            LogUtil.e(TAG, "reason: " + e.getMessage());
        }
    }

    public void register(Context context) {
        if (sChameleonManager == null) {
            return;
        }

        try {
            Method register = sMethod.get("register");
            if (register == null) {
                register = sChameleonManager.getMethod("register", Context.class);
                register.setAccessible(true);
                sMethod.put("register", register);
            }
            register.invoke(sChameleonManager, context);
        } catch (NoSuchMethodException e) {
            LogUtil.e(TAG, "register:no such method exception");
        } catch (InvocationTargetException e) {
            LogUtil.e(TAG, "register:invoke exception");
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "register:illegal access exception");
        } catch (Exception e) {
            LogUtil.e(TAG, "register: exception");
        }
    }

    public void unregister() {
        if (sChameleonManager == null) {
            return;
        }

        try {
            Method unregister = sMethod.get("unregister");
            if (unregister == null) {
                unregister = sChameleonManager.getMethod("unregister");
                unregister.setAccessible(true);
                sMethod.put("unregister", unregister);
            }
            unregister.invoke(sChameleonManager);
        } catch (NoSuchMethodException e) {
            LogUtil.e(TAG, "unregister:no such method exception");
        } catch (InvocationTargetException e) {
            LogUtil.e(TAG, "unregister:invoke exception");
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "unregister:illegal access exception");
        } catch (Exception e) {
            LogUtil.e(TAG, "unregister: exception");
        }
    }

    public static boolean isNeedChangeColor() {
        if (sChameleonManager == null) {
            return false;
        }

        try {
            Method isNeedChangeColor = sMethod.get("isNeedChangeColor");
            if (isNeedChangeColor == null) {
                isNeedChangeColor = sChameleonManager.getMethod("isNeedChangeColor", (Class<?>) null);
                isNeedChangeColor.setAccessible(true);
                sMethod.put("isNeedChangeColor", isNeedChangeColor);
            }
            boolean b = (Boolean) isNeedChangeColor.invoke(null, (Object) null);
            LogUtil.i(TAG, "isNeedChangeColor = " + b);
            return b;
        } catch (NoSuchMethodException e) {
            LogUtil.e(TAG, "isNeedChangeColor:no such method exception");
        } catch (InvocationTargetException e) {
            LogUtil.e(TAG, "isNeedChangeColor:invoke exception");
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "isNeedChangeColor:illegal access exception");
        } catch (Exception e) {
            LogUtil.e(TAG, "isNeedChangeColor: exception");
        }
        return false;
    }

    public static boolean isPowerSavingMode() {
        if (sChameleonManager == null) {
            return false;
        }

        try {
            Method isNeedChangeColor = sMethod.get("isPowerSavingMode");
            if (isNeedChangeColor == null) {
                isNeedChangeColor = sChameleonManager.getMethod("isPowerSavingMode", (Class<?>) null);
                isNeedChangeColor.setAccessible(true);
                sMethod.put("isPowerSavingMode", isNeedChangeColor);
            }
            boolean b = (Boolean) isNeedChangeColor.invoke(null, (Object) null);
            LogUtil.i(TAG, "isPowerSavingMode = " + b);
            return b;
        } catch (NoSuchMethodException e) {
            LogUtil.e(TAG, "isPowerSavingMode:no such method exception");
        } catch (InvocationTargetException e) {
            LogUtil.e(TAG, "isPowerSavingMode:invoke exception");
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "isPowerSavingMode:illegal access exception");
        } catch (Exception e) {
            LogUtil.e(TAG, "isPowerSavingMode: exception");
        }
        return false;
    }

    public static int getBackgroudColor_B1() {
        if (sChameleonManager == null) {
            return -1;
        }

        try {
            Method getBackgroudColor_B1 = sMethod.get("getBackgroudColor_B1");
            if (getBackgroudColor_B1 == null) {
                getBackgroudColor_B1 = sChameleonManager.getMethod("getBackgroudColor_B1", (Class<?>) null);
                getBackgroudColor_B1.setAccessible(true);
                sMethod.put("getBackgroudColor_B1", getBackgroudColor_B1);
            }
            int i = (Integer) getBackgroudColor_B1.invoke(null, (Object) null);
            LogUtil.i(TAG, "getBackgroudColor_B1 = " + i);
            return i;
        } catch (NoSuchMethodException e) {
            LogUtil.e(TAG, "getBackgroudColor_B1:no such method exception");
        } catch (InvocationTargetException e) {
            LogUtil.e(TAG, "getBackgroudColor_B1:invoke exception");
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "getBackgroudColor_B1:illegal access exception");
        } catch (Exception e) {
            LogUtil.e(TAG, "getBackgroudColor_B1: exception");
        }
        return -1;
    }

}
