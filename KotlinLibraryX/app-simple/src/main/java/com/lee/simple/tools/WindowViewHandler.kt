package com.lee.simple.tools

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * 后台显示窗口处理器
 * @author jv.lee
 * @date 2023/7/24
 */
class WindowViewHandler(activity: FragmentActivity) {

    private var mActivity: FragmentActivity? = activity
    private var mWindowManager: WindowManager? = null
    private var mWindowParams: WindowManager.LayoutParams? = null
    private var mWindowView: View? = null
    private var isShowWindow = false
    private var isInit = false
    private var mStateListener: OnWindowStateListener? = null

    init {
        // 设置app前后台切换生命周期监听
        val processEventObserver = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        mStateListener?.onWindowShow()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        hideWindowView()
                    }

                    Lifecycle.Event.ON_DESTROY -> {
                        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
                        hideWindowView()
                    }
                    else -> {}
                }
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(processEventObserver)
        // activity销毁后取消观察
        mActivity?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_CREATE) {
                    initWindowManager()
                } else if (event == Lifecycle.Event.ON_DESTROY) {
                    mActivity?.lifecycle?.removeObserver(this)
                    ProcessLifecycleOwner.get().lifecycle.removeObserver(processEventObserver)
                    hideWindowView()
                    release()
                }
            }
        })
    }

    private fun initWindowManager() {
        mWindowManager = mActivity?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        mWindowParams = WindowManager.LayoutParams()
        mWindowParams?.width = 400
        mWindowParams?.height = 400
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mWindowParams?.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            mWindowParams?.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        mWindowParams?.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        mWindowParams?.gravity = Gravity.CENTER
        mWindowParams?.format = PixelFormat.TRANSLUCENT
//        mWindowParams?.x = 200
        isInit = true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addWindowTouchEvent(view: View, clickLayoutAction: () -> Unit) {
        var mLastX = 0F
        var mLastY = 0F
        var isMove = false
        view.setOnTouchListener { v, event ->
            val mInScreenX = event.rawX
            val mInScreenY = event.rawY
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isMove = false
                    mLastX = event.rawX
                    mLastY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    if (event.rawX != mLastX && event.rawY != mLastY) {
                        isMove = true
                    }
                    mWindowParams?.run {
                        x += (mInScreenX - mLastX).toInt()
                        y += (mInScreenY - mLastY).toInt()
                    }
                    mLastX = mInScreenX
                    mLastY = mInScreenY
                    mWindowView?.run {
                        mWindowManager?.updateViewLayout(this, mWindowParams)
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (!isMove) {
                        clickLayoutAction.invoke()
                    }
                }
            }
            return@setOnTouchListener isMove
        }
    }

    private fun release() {
        mStateListener = null
        mActivity = null
        mWindowManager = null
        mWindowParams = null
    }

    fun showWindowView(view: View) {
        if (isShowWindow || !isInit) return
        isShowWindow = true

        mWindowView = view
        mWindowView?.run {
            addWindowTouchEvent(this) {
                mActivity?.let { activity ->
                    val intent = Intent(activity, activity::class.java)
                    intent.addCategory(Intent.CATEGORY_LAUNCHER)
                    intent.action = Intent.ACTION_MAIN
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    mActivity?.startActivity(intent)
                }
            }
            mWindowManager?.addView(this, mWindowParams)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun hideWindowView() {
        if (!isShowWindow || !isInit) return
        isShowWindow = false
        mWindowView?.run {
            mWindowManager?.removeView(this)
            setOnTouchListener(null)
            mWindowView = null
        }
    }

    fun onWindowShow() {
        mStateListener?.onWindowShow()
    }

    fun setOnWindowStateListener(listener: OnWindowStateListener) {
        mStateListener = listener
    }

    interface OnWindowStateListener {
        fun onWindowShow()
    }

}