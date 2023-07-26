package com.lee.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.extensions.toast
import com.lee.library.utils.LogUtil
import com.lee.simple.tools.WindowPermissionLauncher
import com.lee.simple.tools.WindowViewHandler

/**
 * WindowManager 添加应用外悬浮view 示例Activity
 * @author jv.lee
 * @date 2023/7/24
 */
class WindowViewActivity : AppCompatActivity(), WindowViewHandler.OnWindowStateListener {

    private val launcher = WindowPermissionLauncher(this)
    private val windowViewHandler = WindowViewHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window_view)

        findViewById<Button>(R.id.button).setOnClickListener {
            windowViewHandler.onWindowShow()
        }

        windowViewHandler.setOnWindowStateListener(this)
    }

    override fun onWindowShow() {
        // 校验是否拥有windowOverlay 应用外显示权限
        launcher.checkOverlayPermission(this, callback = {
            windowViewHandler.showWindowView(createWindowView())
        }, notPermission = {
            // 没有应用外window显示权限
            toast("not Permission")
        })
    }

    private fun createWindowView(): View {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_window_container, null)
        view.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
            windowViewHandler.hideWindowView()
        }
        return view
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        LogUtil.i("onUserLeaveHint")
    }

}