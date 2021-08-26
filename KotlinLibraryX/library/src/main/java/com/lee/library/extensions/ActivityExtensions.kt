package com.lee.library.extensions

import android.app.Dialog
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lee.library.R
import com.lee.library.utils.ActivityUtil

/**
 * @author jv.lee
 * @data 2021/8/26
 * @description
 */

fun FragmentActivity.toast(message: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    message ?: return
    Toast.makeText(applicationContext, message, duration).show()
}

fun FragmentActivity.show(dialog: Dialog) {
    if (ActivityUtil.assertActivityDestroyed(this)) return
    try {
        dialog.show()
    } catch (e: Exception) {
    }
}

fun FragmentActivity.dismiss(dialog: Dialog) {
    if (ActivityUtil.assertActivityDestroyed(this)) return
    try {
        dialog.dismiss()
    } catch (e: Exception) {
    }
}

fun FragmentActivity.show(dialog: DialogFragment) {
    if (ActivityUtil.assertActivityDestroyed(this)) return
    try {
        dialog.show(supportFragmentManager, dialog::class.java.simpleName)
    } catch (e: Exception) {
    }
}

fun FragmentActivity.dismiss(dialog: DialogFragment) {
    if (ActivityUtil.assertActivityDestroyed(this)) return
    try {
        dialog.dismiss()
    } catch (e: Exception) {
    }
}

/**
 * 禁用back事件关闭Activity
 * @param handler back执行后回调方法体
 * @return back控制实例 .remove 移除back拦截事件
 */
fun FragmentActivity.banBackEvent(handler: () -> Unit = {}): OnBackPressedCallback {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handler.invoke()
        }
    }
    onBackPressedDispatcher.addCallback(this, callback)
    return callback
}

/**
 * 设置双击back关闭Activity
 * @param backExitTime  两次back事件间隔 默认2秒
 * @param alertCall 两次back事件间隔时间不满足条件 call回调
 * @return back控制实例 .remove 移除back拦截事件
 */
fun FragmentActivity.delayBackEvent(
    backExitTime: Int = 2000,
    alertCall: () -> Unit = { toast(getString(R.string.double_click_back)) }
): OnBackPressedCallback {
    var firstTime: Long = 0
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val secondTime = System.currentTimeMillis()
            //如果两次按键时间间隔大于2秒，则不退出
            if (secondTime - firstTime > backExitTime) {
                alertCall.invoke()
                //更新firstTime
                firstTime = secondTime
            } else {//两次按键小于2秒时，退出应用
                finish()
            }
        }
    }
    onBackPressedDispatcher.addCallback(this, callback)
    return callback
}

/**
 * fragment 控制扩展函数
 */
fun FragmentActivity.fragmentTransaction(containerId: Int, fragment: Fragment?) {
    fragment ?: return
    val transaction = supportFragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_default)
    for (tag in supportFragmentManager.fragments) {
        transaction.hide(tag)
    }
    if (supportFragmentManager.fragments.contains(fragment)) {
        transaction.show(fragment)
    } else {
        //防止fragment重复添加
        if (!fragment.isAdded && supportFragmentManager.findFragmentByTag(fragment::class.java.simpleName) == null) {
            transaction.add(containerId, fragment, fragment::class.java.simpleName)
        }
    }
    if (!isDestroyed) {
        transaction.commit()
        //事务通知所有事件执行 防止 isAdded / getTag失效 引发异常
        supportFragmentManager.executePendingTransactions()
    }
}