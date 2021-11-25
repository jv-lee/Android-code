package com.lee.library.tools

import android.content.Context
import android.content.SharedPreferences
import com.lee.library.base.BaseApplication

/**
 * @author jv.lee
 * SharedPreferences 存储工具
 * Created by jv.lee on 2016/8/31.
 */
class PreferencesTools {

    companion object {

        private const val SP_NAME = "share_data"

        private val preferences: SharedPreferences by lazy {
            BaseApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        }

        /**
         * 保存数据 根据数据类型自动拆箱
         *
         * @param key    键名
         * @param values Object类型数据 但仅限于(String/int/float/boolean/long)
         */
        @Synchronized
        fun <T> put(key: String, values: T) {
            val editor = preferences.edit()
            when (values) {
                is String -> {
                    editor.putString(key, values)
                }
                is Int -> {
                    editor.putInt(key, values)
                }
                is Long -> {
                    editor.putLong(key, values)
                }
                is Boolean -> {
                    editor.putBoolean(key, values)
                }
                is Float -> {
                    editor.putFloat(key, values)
                }
            }
            editor.apply()
        }

        /**
         * 获取Object类型数据 根据接收类型自动拆箱
         *
         * @param key          键名
         * @param defaultValue 根据key获取不到是默认值仅限于(String/int/float/boolean/long)
         */
        @Synchronized
        fun <T> get(key: String, defaultValue: T): T {
            when (defaultValue) {
                is String -> {
                    return preferences.getString(key, defaultValue) as T
                }
                is Int -> {
                    return preferences.getInt(key, defaultValue) as T
                }
                is Long -> {
                    return preferences.getLong(key, defaultValue) as T
                }
                is Boolean -> {
                    return preferences.getBoolean(key, defaultValue) as T
                }
                is Float -> {
                    return preferences.getFloat(key, defaultValue) as T
                }
                else -> return defaultValue
            }
        }

        /**
         * 清除保存的数据
         */
        @Synchronized
        fun clear() {
            val editor = preferences.edit()
            editor.clear().apply()
        }

        /**
         * 根据key删除value
         * @param key 被删除的key
         */
        @Synchronized
        fun remove(key: String) {
            val editor = preferences.edit()
            editor.remove(key).apply()
        }

    }


}