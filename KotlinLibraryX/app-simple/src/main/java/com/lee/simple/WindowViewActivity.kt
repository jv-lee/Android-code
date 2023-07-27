package com.lee.simple

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.extensions.toast
import com.lee.library.utils.LogUtil
import com.lee.simple.tools.WindowOverlayUtils
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
        LogUtil.i("onWindowShow")
        val isActivityNewVersion = true
        val callback = { windowViewHandler.showWindowView(createWindowView()) }
        val notPermission = { toast("not Permission") }

        // 是否为最新activity库 支持ActivityResultLauncher API
        if (isActivityNewVersion) {
            // 校验是否拥有windowOverlay 应用外显示权限
            launcher.checkOverlayPermission(this, callback, notPermission)
        } else {
            // 旧版本封装startActivityForResult 回调权限设置结果
            WindowOverlayUtils.checkOverlayPermission(this, callback, notPermission)
        }
    }

    private fun createWindowView(): View {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_window_container, null)
        view.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
            windowViewHandler.hideWindowView()
        }
        return view
    }

    override fun onUserLeaveHint() {
        LogUtil.i("onUserLeaveHint")
        super.onUserLeaveHint()
    }

    override fun onUserInteraction() {
        LogUtil.i("onUserInteraction")
        super.onUserInteraction()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        LogUtil.i("onKeyDown: keyCode:$keyCode,event:$event")
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        LogUtil.i("onKeyUp: keyCode:$keyCode,event:$event")
        return super.onKeyUp(keyCode, event)
    }

}