package com.lee.app.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.Surface

/**
 * 获取当前屏幕方向
 * @return
 * [ActivityInfo.SCREEN_ORIENTATION_PORTRAIT] 竖屏
 * [ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT] 反转竖屏
 * [ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE] 横屏
 * [ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE] 反转横屏
 */
fun Activity.getScreenOrientation(): Int {
    val rotation = windowManager.defaultDisplay.rotation
    val orientation = resources.configuration.orientation
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        return if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270) {
            // 竖屏
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            // 反转竖屏
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
        }
    }
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        return if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
            // 横屏
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            // 反转横屏
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        }
    }
    return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}