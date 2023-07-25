package com.lee.simple

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.lee.library.extensions.toast
import com.lee.simple.tools.WindowPermissionLauncher

/**
 * WindowManager 添加应用外悬浮view 示例Activity
 * @author jv.lee
 * @date 2023/7/24
 */
class WindowViewActivity : AppCompatActivity() {

    private val launcher = WindowPermissionLauncher(this)
    private var mWindowManager: WindowManager? = null
    private var mWindowParams: WindowManager.LayoutParams? = null
    private var mWindowView: View? = null
    private var isShowWindow = false
    private var isInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window_view)

        // 校验是否拥有windowOverlay 应用外显示权限
        launcher.checkOverlayPermission(this, callback = {
            initWindowManager()
        }, notPermission = {
            // 没有应用外window显示权限
            toast("not Permission")
        })

        findViewById<Button>(R.id.button).setOnClickListener {
            showWindowView()
        }
    }

    private fun initWindowManager() {
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
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

        // 设置app前后台切换生命周期监听
        val processEventObserver = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        showWindowView()
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
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    lifecycle.removeObserver(this)
                    ProcessLifecycleOwner.get().lifecycle.removeObserver(processEventObserver)
                    hideWindowView()
                    releaseWindowManager()
                }
            }

        })
        isInit = true
    }

    private fun showWindowView() {
        if (isShowWindow || !isInit) return
        isShowWindow = true

        mWindowView = createWindowView()
        mWindowView?.run {
            addWindowTouchEvent(this) {
                val intent = Intent(this@WindowViewActivity, WindowViewActivity::class.java)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.action = Intent.ACTION_MAIN
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                startActivity(intent)
            }
        }
        mWindowManager?.addView(mWindowView, mWindowParams)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideWindowView() {
        if (!isShowWindow || !isInit) return
        isShowWindow = false
        mWindowView?.run { mWindowManager?.removeView(this) }
        mWindowView?.setOnTouchListener(null)
        mWindowView = null
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
                    mWindowManager?.updateViewLayout(mWindowView, mWindowParams)
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

    private fun createWindowView(): View {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_window_container, null)
        view.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
            hideWindowView()
        }
        return view
    }

    private fun releaseWindowManager() {
        mWindowManager = null
        mWindowParams = null
    }

}