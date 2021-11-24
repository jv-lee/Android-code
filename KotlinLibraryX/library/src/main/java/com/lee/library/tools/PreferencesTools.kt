package com.lee.library.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

/**
 * @author jv.lee
 * SharedPreferences 存储工具
 * Created by jv.lee on 2016/8/31.
 */
public class PreferencesTools {

    private static final String SP_NAME = "share_data";
    private volatile static SharedPreferences mSharedPreferences;

    private PreferencesTools() {
    }

    public static SharedPreferences getInstance(Context context) {
        if (mSharedPreferences == null) {
            synchronized (PreferencesTools.class) {
                if (mSharedPreferences == null) {
                    mSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
                }
            }
        }
        return mSharedPreferences;
    }

    public static SharedPreferences getInstance() {
        if (mSharedPreferences == null) {
            throw new RuntimeException("请先调用 带有context的 getInstance() 方法");
        }
        return mSharedPreferences;
    }


    /**
     * 保存数据 根据数据类型自动拆箱
     *
     * @param key    键名
     * @param values Object类型数据 但仅限于(String/int/float/boolean/long)
     */
    public static void save(String key, Object values) {
        Editor editor = mSharedPreferences.edit();
        if (values instanceof String) {
            editor.putString(key, (String) values);

        } else if (values instanceof Integer) {
            editor.putInt(key, (Integer) values);

        } else if (values instanceof Long) {
            editor.putLong(key, (Long) values);

        } else if (values instanceof Boolean) {
            editor.putBoolean(key, (Boolean) values);

        } else if (values instanceof Float) {
            editor.putFloat(key, (Float) values);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }else{
            editor.commit();
        }
    }

    /**
     * 获取Object类型数据 根据接收类型自动拆箱
     *
     * @param key          键名
     * @param defaultValue 根据key获取不到是默认值仅限于(String/int/float/boolean/long)
     */
    public static Object get(String key, Object defaultValue) {
        SharedPreferences sp = mSharedPreferences;
        if (defaultValue instanceof String) {
            return sp.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sp.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sp.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sp.getFloat(key, (Float) defaultValue);
        }

        return null;
    }

    /**
     * 清除保存的数据
     *
     */
    public static void delete() {
        Editor editor = mSharedPreferences.edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.clear().apply();
        }else{
            editor.clear().commit();
        }
    }
    /**
     * 根据key删除value
     *
     */
    public static void deleteBykey(String key){
        Editor editor=mSharedPreferences.edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.remove(key).apply();
        }else{
            editor.remove(key).commit();
        }
    }

}
