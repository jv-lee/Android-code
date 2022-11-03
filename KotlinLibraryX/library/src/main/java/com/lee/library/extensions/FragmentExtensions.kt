/*
 * fragment 扩展函数帮助类
 * @author jv.lee
 * @date 2021/8/26
 */
package com.lee.library.extensions

import android.app.Activity
import android.app.Dialog
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.lee.library.R
import com.lee.library.utils.ActivityUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.toast(message: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    message ?: return
    if (!isAdded) return
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.show(dialog: Dialog) {
    if (!isAdded) return
    if (ActivityUtil.assertActivityDestroyed(requireActivity())) return
    try {
        dialog.show()
    } catch (_: Exception) {
    }
}

fun Fragment.dismiss(dialog: Dialog) {
    if (!isAdded) return
    if (ActivityUtil.assertActivityDestroyed(requireActivity())) return
    try {
        dialog.dismiss()
    } catch (_: Exception) {
    }
}

fun Fragment.show(dialog: DialogFragment) {
    if (!isAdded) return
    if (ActivityUtil.assertActivityDestroyed(requireActivity())) return
    try {
        dialog.show(childFragmentManager, dialog::class.java.simpleName)
    } catch (_: Exception) {
    }
}

fun Fragment.dismiss(dialog: DialogFragment) {
    if (!isAdded) return
    if (ActivityUtil.assertActivityDestroyed(requireActivity())) return
    try {
        dialog.dismiss()
    } catch (_: Exception) {
    }
}

/**
 * 根据泛型直接获取父Fragment
 */
inline fun <reified T : Fragment> Fragment.findParentFragment() = parentFragment as? T

/**
 * 根据泛型直接获取Activity容器
 */
inline fun <reified T : Activity> Fragment.findParentActivity() = requireActivity() as? T

/**
 * 携程flow fragment生命周期绑定
 */
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

/**
 * 设置双击back关闭Activity
 * @param backExitTime  两次back事件间隔 默认2秒
 * @param alertCall 两次back事件间隔时间不满足条件 call回调
 * @return back控制实例 .remove 移除back拦截事件
 */
inline fun Fragment.delayBackEvent(
    backExitTime: Int = 2000,
    crossinline alertCall: () -> Unit = { toast(getString(R.string.double_click_back)) }
): OnBackPressedCallback {
    var firstTime: Long = 0
    return object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val secondTime = System.currentTimeMillis()
            // 如果两次按键时间间隔大于2秒，则不退出
            if (secondTime - firstTime > backExitTime) {
                alertCall()
                // 更新firstTime
                firstTime = secondTime
            } else { // 两次按键小于2秒时，退出应用
                requireActivity().finish()
            }
        }
    }.apply {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, this)

        // 生命周期解绑
        viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    remove()
                    viewLifecycleOwner.lifecycle.removeObserver(this)
                }
            }
        })
    }
}

/**
 * 设置双击back关闭Activity
 * @param backExitTime  两次back事件间隔 默认2秒
 * @param hasBack 回退拦截策略高阶函数
 * @param alertCall 两次back事件间隔时间不满足条件 call回调
 * @return back控制实例 .remove 移除back拦截事件
 */
inline fun Fragment.delayBackEvent(
    backExitTime: Int = 2000,
    crossinline hasBack: () -> Boolean = { true },
    crossinline alertCall: () -> Unit = { toast(getString(R.string.double_click_back)) }
): OnBackPressedCallback {
    var firstTime: Long = 0
    return object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (hasBack()) {
                val secondTime = System.currentTimeMillis()
                // 如果两次按键时间间隔大于2秒，则不退出
                if (secondTime - firstTime > backExitTime) {
                    alertCall()
                    // 更新firstTime
                    firstTime = secondTime
                } else { // 两次按键小于2秒时，退出应用
                    requireActivity().finish()
                }
            }
        }
    }.apply {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, this)

        // 生命周期解绑
        viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    remove()
                    viewLifecycleOwner.lifecycle.removeObserver(this)
                }
            }
        })
    }
}