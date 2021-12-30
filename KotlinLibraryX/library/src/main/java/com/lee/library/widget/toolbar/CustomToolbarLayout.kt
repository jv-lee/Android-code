package com.lee.library.widget.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.lee.library.R
import com.lee.library.extensions.dp2px
import com.lee.library.extensions.px2dp
import com.lee.library.tools.StatusTools
import com.lee.library.tools.StatusTools.statusBarHeight


/**
 * @author jv.lee
 * @date 2020/4/1
 * @description 自定义toolbar容器
 */
open class CustomToolbarLayout : ConstraintLayout {

    private var statusBarHeight = context.statusBarHeight()
    private var toolbarLayoutHeight = initLayoutHeight()

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributes: AttributeSet) : this(context, attributes, 0)
    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    ) {
        minHeight = toolbarLayoutHeight

        initStatusBarPadding()
        initBackground()
    }

    /**
     * 初始化布局高度
     */
    open fun initLayoutHeight(): Int {
        val toolbarHeight = resources.getDimension(R.dimen.toolbar_height).toInt()
        return toolbarHeight + statusBarHeight
    }

    /**
     * 设置默认背景色
     */
    open fun initBackground() {
        background ?: kotlin.run {
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorThemeItem))
        }
    }

    /**
     * 设置状态栏填充padding
     */
    open fun initStatusBarPadding() {
        setPadding(context.dp2px(16).toInt(), statusBarHeight, context.dp2px(16).toInt(), 0)
    }

    /**
     * 获取toolbarLayout高度
     */
    fun getToolbarLayoutHeight() = toolbarLayoutHeight

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight() = statusBarHeight

    private fun initBottomLine() {
        val lineView = View(context)
        lineView.run {
            id = R.id.toolbar_line
            layoutParams = LayoutParams(MATCH_PARENT, context.px2dp(1).toInt()).apply {
                bottomToBottom = 0
            }
            lineView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorThemeBackground
                )
            )
        }
        addView(lineView)
    }

}