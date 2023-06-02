package com.lee.ui.utils

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager

/**
 * 窗口自定义view 帮助类
 * 可以在Activity中窗口顶部通过 WindowManager 绘制view到屏幕上
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

    /**
     * 通过WindowManager 显示自定义view
     * @param contentView 需要显示的view
     */
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

    /**
     * 通过WindowManager 隐藏自定义view
     */
    fun hide() {
        windowManager?.run {
            contentView?.let { view ->
                removeView(view)
            }
        }
        windowManager = null
        contentView = null
    }

    /**
     * 通过WindowManager 显示自定义view并延时自动隐藏
     * @param contentView 需要显示的view
     * @param time 延时时间
     */
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

        /**
         * 设置WindowManager.LayoutParams的宽度
         * @param width 宽度
         */
        fun setWidth(width: Int): Builder {
            this.width = width
            return this
        }

        /**
         * 设置WindowManager.LayoutParams的高度
         * @param height 高度
         */
        fun setHeight(height: Int): Builder {
            this.height = height
            return this
        }

        /**
         * 设置WindowManager.LayoutParams的gravity位置
         * Gravity.CENTER: 居中
         * Gravity.START or Gravity.TOP: 左上角开始布局，该模式设置x，y有效
         * @param gravity 位置
         */
        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        /**
         * 设置WindowManager.LayoutParams的x坐标
         * @param x x坐标
         */
        fun setX(x: Int): Builder {
            this.x = x
            return this
        }

        /**
         * 设置WindowManager.LayoutParams的y坐标
         * @param y y坐标
         */
        fun setY(y: Int): Builder {
            this.y = y
            return this
        }

        /**
         * 构建WindowViewHelper实例
         */
        fun build(): WindowViewHelper {
            return WindowViewHelper(width, height, gravity, x, y)
        }
    }
}