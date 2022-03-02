package com.lee.library.tools

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

/**
 * @author jv.lee
 * @date 2020/6/9
 * @description 深色主题适配方法
 */
class DarkModeTools(val context: Context) {

    private val TAG = "DarkModeTools"

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: DarkModeTools? = null

        @JvmStatic
        fun get(context: Context) = instance ?: synchronized(this) {
            instance ?: DarkModeTools(context).also { instance = it }
        }

        fun get(): DarkModeTools {
            if (instance == null) {
                throw Exception("DarkModeTools 未初始化")
            }
            return instance!!
        }
    }

    private val modeKey = "dark_mode"
    private val preferences =
        context.applicationContext.getSharedPreferences(modeKey, Context.MODE_PRIVATE)

    /**
     * 当前是否为系统主题
     */
    fun isSystemTheme(): Boolean {
        val mode = preferences.getInt(modeKey, AppCompatDelegate.getDefaultNightMode())
        return mode != AppCompatDelegate.MODE_NIGHT_YES && mode != AppCompatDelegate.MODE_NIGHT_NO
    }

    /**
     * 当前是否为深色主题
     */
    fun isDarkTheme(): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val mode = preferences.getInt(modeKey, AppCompatDelegate.getDefaultNightMode())
        return when {
            mode == AppCompatDelegate.MODE_NIGHT_YES -> {
                true
            }
            mode == AppCompatDelegate.MODE_NIGHT_NO -> {
                false
            }
            flag == Configuration.UI_MODE_NIGHT_YES -> {
                true
            }
            else -> false
        }
    }

    /**
     * 设置为跟随系统主题变更
     */
    @SuppressLint("CommitPrefEdits")
    fun updateSystemTheme(enable: Boolean) {
        if (enable) {
            preferences.edit().putInt(modeKey, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            updateNightTheme(isDarkTheme())
        }
    }

    /**
     * 设置深色主题
     */
    @SuppressLint("CommitPrefEdits")
    fun updateNightTheme(enable: Boolean) {
        if (enable) {
            preferences.edit().putInt(modeKey, AppCompatDelegate.MODE_NIGHT_YES).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            preferences.edit().putInt(modeKey, AppCompatDelegate.MODE_NIGHT_NO).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}