package com.lee.library.extensions

import android.app.Activity
import android.app.Dialog
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.lee.library.R
import com.lee.library.utils.ActivityUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 *
 * @author jv.lee
 * @date 2021/8/26
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
inline fun FragmentActivity.banBackEvent(crossinline handler: () -> Unit = {}): OnBackPressedCallback {
    return object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handler()
        }
    }.apply {
        onBackPressedDispatcher.addCallback(this@banBackEvent, this)

        // 生命周期解绑
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    remove()
                    lifecycle.removeObserver(this)
                }
            }
        })
    }
}

/**
 * 设置双击back关闭Activity
 * @param backExitTime  两次back事件间隔 默认2秒
 * @param alertCall 两次back事件间隔时间不满足条件 call回调
 * @return back控制实例 .remove 移除back拦截事件
 */
inline fun FragmentActivity.delayBackEvent(
    backExitTime: Int = 2000,
    crossinline alertCall: () -> Unit = { toast(getString(R.string.double_click_back)) }
): OnBackPressedCallback {
    var firstTime: Long = 0
    return object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val secondTime = System.currentTimeMillis()
            //如果两次按键时间间隔大于2秒，则不退出
            if (secondTime - firstTime > backExitTime) {
                alertCall()
                //更新firstTime
                firstTime = secondTime
            } else {//两次按键小于2秒时，退出应用
                finish()
            }
        }
    }.apply {
        onBackPressedDispatcher.addCallback(this@delayBackEvent, this)

        // 生命周期解绑
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    remove()
                    lifecycle.removeObserver(this)
                }
            }
        })
    }
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

fun Activity.bindFragmentLifecycle(
    @NonNull cb: FragmentManager.FragmentLifecycleCallbacks,
    recursive: Boolean = true
) {
    if (this is FragmentActivity) {
        supportFragmentManager.registerFragmentLifecycleCallbacks(cb, recursive)
    }
}

fun Activity.unbindFragmentLifecycle(@NonNull cb: FragmentManager.FragmentLifecycleCallbacks) {
    if (this is FragmentActivity) {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(cb)
    }
}

/**
 * 携程flow fragment生命周期绑定
 */
inline fun FragmentActivity.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}