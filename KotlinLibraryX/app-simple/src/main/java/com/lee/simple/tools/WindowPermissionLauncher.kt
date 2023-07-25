package com.lee.simple.tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * WindowManager 权限申请校验工具
 * @author jv.lee
 * @date 2023/7/24
 */
class WindowPermissionLauncher {

    var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var resultLauncherCallback: (() -> Unit)? = null

    constructor(fragment: Fragment) {
        createLauncher(fragment)
    }

    constructor(activity: FragmentActivity) {
        createLauncher(activity)
    }

    private fun createLauncher(thisClass: Any) {
        val thisT = when (thisClass) {
            is FragmentActivity -> {
                thisClass
            }
            is Fragment -> {
                thisClass
            }
            else -> {
                throw ClassCastException("thisClass type not is FragmentActivity/Fragment")
            }
        }

        // 创建activityResult 回调监听启动器
        resultLauncher =
            thisT.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                resultLauncherCallback?.invoke()
            }

        // 页面销毁处理
        thisT.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    thisT.lifecycle.removeObserver(this)
                    resultLauncher = null
                    resultLauncherCallback = null
                }
            }
        })
    }

    /**
     * 校验当前是否拥有显示在其他应用之上的Window权限
     */
    fun checkOverlayPermission(
        context: Context,
        callback: () -> Unit,
        notPermission: () -> Unit = {}
    ) {
        // 低版本无需申请权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.invoke()
            return
        }

        // 查看当前是否拥有显示在其他应用之上的悬浮窗权限
        if (Settings.canDrawOverlays(context)) {
            callback.invoke()
        } else {
            // 处理返回页面后权限状态callback
            resultLauncherCallback = {
                if (Settings.canDrawOverlays(context)) {
                    callback.invoke()
                } else {
                    notPermission.invoke()
                }
            }

            // 无导航到设置页面处理
            resultLauncher?.launch(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                )
            )
        }
    }

}