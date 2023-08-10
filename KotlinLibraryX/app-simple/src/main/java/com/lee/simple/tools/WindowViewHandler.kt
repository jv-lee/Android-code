package com.lee.simple.tools

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.lee.library.extensions.dp2px

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
    private var mLastX = 0F
    private var mLastY = 0F
    private var isZoomIng = false // 缩放进行时
    private var isZoomIn = true // 放大or缩小

    private var mScaleSize = 1f
    private val mWindowWidth = 720
    private val mWindowHeight = 400
    private var mScreenWidth = 0
    private var mScreenHeight = 0

    private var mStateListener: OnWindowStateListener? = null

    // 捕获用户多点触控
    private var mScaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(
        activity,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleStartEvent(detector)
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                super.onScaleEnd(detector)
                scaleEndEvent(detector)
            }
        })

    // 基础手势
    private var mGestureDetector: GestureDetector =
        GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                singleTap()
                return super.onSingleTapConfirmed(e)
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                doubleTap()
                return super.onDoubleTap(e)
            }
        })

    init {
        // 获取屏幕宽高
        mScreenWidth = activity.resources.displayMetrics.widthPixels
        mScreenHeight = activity.resources.displayMetrics.heightPixels

        // 设置app前后台切换生命周期监听
        val processEventObserver = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        onWindowShow()
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
        mWindowParams?.width = mWindowWidth
        mWindowParams?.height = mWindowHeight
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
        isInit = true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addGestureListener(view: View) {
        view.setOnTouchListener { v, event ->
            // 缩放触发中只处理缩放事件
            if (isZoomIng) return@setOnTouchListener mScaleGestureDetector.onTouchEvent(event)
            return@setOnTouchListener mScaleGestureDetector.onTouchEvent(event) or
                    mGestureDetector.onTouchEvent(event) or onMoveTouchEvent(event)
        }
    }

    private fun onMoveTouchEvent(event: MotionEvent): Boolean {
        val mInScreenX = event.rawX
        val mInScreenY = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.rawX
                mLastY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
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
        }
        return true
    }

    private fun release() {
        mStateListener = null
        mActivity = null
        mWindowManager = null
        mWindowParams = null
    }

    private fun singleTap() {
        mActivity?.let { activity ->
            WindowOverlayUtils.startToApp(activity)
        }
    }

    private fun doubleTap() {
        mActivity?.run {
            if (isZoomIn) {
                mScaleSize += 0.5f
            } else {
                mScaleSize -= 0.5f
            }
            isZoomIn = !isZoomIn
            mWindowParams?.run {
                // 双击放大缩小设置x y位置防止放大时抖动
                val oldWidth = width
                val oldHeight = height
                val newWidth = (mWindowWidth * mScaleSize).toInt()
                val newHeight = (mWindowHeight * mScaleSize).toInt()
                x += (newWidth - oldWidth) / 2
                y += (newHeight - oldHeight) / 2
                width = newWidth
                height = newHeight
            }
            mWindowView?.run { mWindowManager?.updateViewLayout(this, mWindowParams) }
        }
    }

    private fun scaleStartEvent(detector: ScaleGestureDetector) {
        isZoomIng = true
        mWindowParams?.run {
            var scaleWidth = (width * detector.scaleFactor).toInt()
            var scaleHeight = (height * detector.scaleFactor).toInt()
            // 缩放宽度最小值限制
            if (scaleWidth < mWindowWidth) {
                scaleWidth = mWindowWidth
            }
            // 缩放高度最小值限制
            if (scaleHeight < mWindowHeight) {
                scaleHeight = mWindowHeight
            }
            // 缩放宽高最大值限制
            if (scaleWidth > mScreenWidth) {
                scaleWidth = mScreenWidth
                scaleHeight = height
            }
            width = scaleWidth
            height = scaleHeight
        }
        mWindowView?.run {
            mWindowManager?.updateViewLayout(this, mWindowParams)
        }
    }

    private fun scaleEndEvent(detector: ScaleGestureDetector) {
        // 设置延时，防止缩放结束后移动事件同步改变位置导致窗口闪缩移动
        mWindowView?.postDelayed({
            isZoomIng = false
        }, 300)
    }

    fun showWindowView(view: View) {
        if (isShowWindow || !isInit) return
        isShowWindow = true

        // 每次显示重置小窗大小位置属性
        mWindowParams?.run {
            mWindowParams?.width = mWindowWidth
            mWindowParams?.height = mWindowHeight
            mWindowParams?.x = 0
            mWindowParams?.y = 0
        }

        mWindowView = view
        mWindowView?.run {
            addGestureListener(this)

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