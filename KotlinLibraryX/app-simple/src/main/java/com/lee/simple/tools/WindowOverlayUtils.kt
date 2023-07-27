package com.lee.simple.tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction

/**
 * 校验开启应用外悬浮窗权限
 * @author jv.lee
 * @date 2023/7/25
 */
object WindowOverlayUtils {

    private const val REQUEST_CODE_WRITE_SETTINGS = 0X111

    /** 当前应用是否 */
    private var isUserLeaveHint = false
    private var mIntentRequest: IntentRequest? = null
    private var mFragment: IntentFragment? = null

    /**
     * 校验当前是否拥有显示在其他应用之上的Window权限
     */
    fun checkOverlayPermission(
        activity: FragmentActivity,
        callback: () -> Unit,
        notPermission: () -> Unit
    ) {
        // 低版本无需申请权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.invoke()
            return
        }

        // 查看当前是否拥有显示在其他应用之上的悬浮窗权限
        if (Settings.canDrawOverlays(activity)) {
            callback.invoke()
        } else {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${activity.packageName}")
            )
            startIntentForResult(activity, intent, object : IntentRequest {
                override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                    checkActivityResult(activity, requestCode, callback, notPermission)
                }
            })
        }
    }

    /**
     * 回到系统桌面
     */
    fun startToLauncher(activity: FragmentActivity) {
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_HOME)
        activity.startActivity(intent)
    }

    /**
     * 回到App
     */
    fun startToApp(activity: FragmentActivity) {
        val intent = Intent(activity, activity::class.java)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.action = Intent.ACTION_MAIN
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        activity.startActivity(intent)
    }

    /**
     * 跳转开通应用外悬浮窗权限设置页面后返回校验是否成功开启
     */
    private fun checkActivityResult(
        context: Context,
        requestCode: Int,
        callback: () -> Unit,
        notPermission: () -> Unit
    ) {
        // 低版本无需申请权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.invoke()
            return
        }
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Settings.canDrawOverlays(context)) {
                callback.invoke()
            } else {
                notPermission.invoke()
            }
        }
    }

    private fun startIntentForResult(
        activity: FragmentActivity,
        intent: Intent,
        intentRequest: IntentRequest
    ) {
        mIntentRequest = intentRequest

        val manager = activity.supportFragmentManager
        val transaction = manager.beginTransaction()

        if (mFragment == null) {
            mFragment = IntentFragment()
        }

        mFragment?.let { fragment ->
            if (fragment.isAdded) {
                transaction.remove(fragment)
            }
            transaction.add(fragment, fragment::class.java.name)
            transaction.addToBackStack(fragment::class.java.name)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commitAllowingStateLoss()
            manager.executePendingTransactions()

            fragment.startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS)
        }
    }

    private interface IntentRequest {
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }

    class IntentFragment : Fragment() {
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            mIntentRequest?.onActivityResult(requestCode, resultCode, data)
        }
    }

}