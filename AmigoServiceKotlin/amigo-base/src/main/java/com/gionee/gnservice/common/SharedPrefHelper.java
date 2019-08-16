package com.gionee.gnservice.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.gionee.gnservice.utils.PreconditionsUtil;
import com.gionee.gnservice.utils.SdkUtil;

import java.util.Map;

public class SharedPrefHelper {
    private static final String NAME_SDK_PREF = "amigo_service_sdk_pref";
    private SharedPreferences mSharePrefs;
    private volatile static SharedPrefHelper sInstance;

    private SharedPrefHelper(Context context) {
        PreconditionsUtil.checkNotNull(context);
        if (SdkUtil.isCallBySdk(context)) {
            mSharePrefs = context.getSharedPreferences(NAME_SDK_PREF, Context.MODE_PRIVATE);
        } else {
            mSharePrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }
    }

    public static SharedPrefHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SharedPrefHelper.class) {
                if (sInstance == null) {
                    sInstance = new SharedPrefHelper(context);
                }
            }
        }
        return sInstance;
    }


    /**
     * 一些列读操作
     */
    public int getInt(String key, int defValue) {
        return mSharePrefs.getInt(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSharePrefs.getBoolean(key, defValue);
    }

    @SuppressWarnings("unused")
    public float getFloat(String key, float defValue) {
        return mSharePrefs.getFloat(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return mSharePrefs.getLong(key, defValue);
    }

    public String getString(String key, String defValue) {
        return mSharePrefs.getString(key, defValue);
    }

    /**
     * 一系列写的操作
     */
    public void putInt(String key, int value) {
        Editor editor = mSharePrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        Editor editor = mSharePrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @SuppressWarnings("unused")
    public void putFloat(String key, float value) {
        Editor editor = mSharePrefs.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        Editor editor = mSharePrefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putString(String key, String value) {
        Editor editor = mSharePrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clear() {
        Editor editor = mSharePrefs.edit();
        editor.clear();
        editor.apply();
    }

    public void remove(Context context, String key) {
        Editor editor = mSharePrefs.edit();
        editor.remove(key);
        editor.apply();
    }

    @SuppressWarnings("unused")
    public boolean contains(Context context, String key) {
        return mSharePrefs.contains(key);
    }

    @SuppressWarnings("unused")
    public Map<String, ?> getAll(Context context) {
        return mSharePrefs.getAll();
    }

}
