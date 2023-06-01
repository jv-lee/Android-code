package com.lee.ui.utils

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager

/**
 * 窗口自定义view 帮助类
 * @author jv.lee
 * @date 2023/6/1
 */
class WindowViewHelper private constructor(
    private var width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    private var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    private var gravity: Int = Gravity.CENTER,
    private var x: Int = 0,
    private var y: Int = 0
) {
    private var contentView: View? = null
    private var windowManager: WindowManager? = null

    fun show(contentView: View) {
        hide()
        this.windowManager =
            contentView.context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        this.contentView = contentView
        windowManager?.run {
            try {
                addView(contentView, buildWindowParams())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun hide() {
        windowManager?.run {
            contentView?.let { view ->
                removeView(view)
            }
        }
        windowManager = null
        contentView = null
    }

    fun showDelayHide(contentView: View, time: Long = 3000) {
        show(contentView)
        contentView.postDelayed({ hide() }, time)
    }

    private fun buildWindowParams(): WindowManager.LayoutParams {
        val params = WindowManager.LayoutParams()
        params.width = width
        params.height = height
        params.format = PixelFormat.TRANSLUCENT
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        params.gravity = gravity
        params.x = x
        params.y = y
        return params
    }

    class Builder {
        private var width: Int = WindowManager.LayoutParams.WRAP_CONTENT
        private var height = WindowManager.LayoutParams.WRAP_CONTENT
        private var gravity = Gravity.CENTER
        private var x = 0
        private var y = 0

        fun setWidth(width: Int): Builder {
            this.width = width
            return this
        }

        fun setHeight(height: Int): Builder {
            this.height = height
            return this
        }

        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        fun setX(x: Int): Builder {
            this.x = x
            return this
        }

        fun setY(y: Int): Builder {
            this.y = y
            return this
        }

        fun build(): WindowViewHelper {
            return WindowViewHelper(width, height, gravity, x, y)
        }
    }
}