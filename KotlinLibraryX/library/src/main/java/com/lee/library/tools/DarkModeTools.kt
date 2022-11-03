@file:Suppress("StaticFieldLeak")

package com.lee.library.tools

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

/**
 * 深色主题适配帮助类
 * 更改系统深色主题模式Flag [AppCompatDelegate.setDefaultNightMode],
 * 获取深色主题当前模式Flag [AppCompatDelegate.getDefaultNightMode],
 * [DarkModeTools.init] 帮助类初始化方法,获取当前存储深色模式类型切换至目标类型,
 * [DarkModeTools.isSystemTheme] 获取当前是否为跟随系统主题类型,
 * [DarkModeTools.isDarkTheme] 获取当前是否为深色模式主题类型,
 * [DarkModeTools.updateSystemTheme] 更改为系统主题类型,
 * [DarkModeTools.updateDarkTheme] 更改为深色模式主题类型
 * [DarkModeTools.setWebDarkCompat] 适配androidWebView深色模式失效兼容问题
 * @author jv.lee
 * @date 2020/6/9
 */
class DarkModeTools(val context: Context) {

    companion object {
        private const val TAG = "DarkModeTools"

        @Volatile
        private var instance: DarkModeTools? = null

        @JvmStatic
        fun init(context: Context) = instance ?: synchronized(this) {
            instance ?: DarkModeTools(context).also {
                instance = it
                it.init()
            }
        }

        fun get(): DarkModeTools = instance ?: throw Exception("DarkModeTools 未初始化")
    }

    var isDark = false
    private val modeKey = "dark_mode"
    private val preferences =
        context.applicationContext.getSharedPreferences(modeKey, Context.MODE_PRIVATE)

    fun init() {
        Log.i(TAG, "init.")
        if (!isSystemTheme()) {
            updateDarkTheme(isDarkTheme())
        }
    }

    /**
     * 当前是否为系统主题
     */
    fun isSystemTheme(): Boolean {
        val mode = preferences.getInt(modeKey, AppCompatDelegate.getDefaultNightMode())
        val isSystemTheme =
            mode != AppCompatDelegate.MODE_NIGHT_YES && mode != AppCompatDelegate.MODE_NIGHT_NO
        Log.i(TAG, "isSystemTheme:$isSystemTheme")
        return isSystemTheme
    }

    /**
     * 当前是否为深色主题
     */
    fun isDarkTheme(): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val mode = preferences.getInt(modeKey, AppCompatDelegate.getDefaultNightMode())
        val isDarkTheme = when {
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
        Log.i(TAG, "isDarkTheme:$isDarkTheme")
        this.isDark = isDarkTheme
        return isDarkTheme
    }

    /**
     * 设置为跟随系统主题变更
     */
    fun updateSystemTheme(enable: Boolean) {
        Log.i(TAG, "updateSystemTheme:$enable")
        if (enable) {
            preferences.edit().putInt(modeKey, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            updateDarkTheme(isDarkTheme())
        }
    }

    /**
     * 设置深色主题
     */
    fun updateDarkTheme(enable: Boolean) {
        Log.i(TAG, "updateDarkTheme:$enable")
        if (enable) {
            preferences.edit().putInt(modeKey, AppCompatDelegate.MODE_NIGHT_YES).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            preferences.edit().putInt(modeKey, AppCompatDelegate.MODE_NIGHT_NO).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    /**
     * 使用android深色模式后如果没有重建Activity在进入有webView的页面时会模式错乱
     * 通过该方法校正
     */
    fun setWebDarkCompat() {
        if (!isDark) return
        if ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)
            != Configuration.UI_MODE_NIGHT_YES
        ) {
            updateDarkTheme(false)
            updateDarkTheme(true)
        }
    }
}