package com.lee.library.widget.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.lee.library.R
import com.lee.library.extensions.dp2px
import com.lee.library.extensions.px2dp
import com.lee.library.utils.StatusUtil


/**
 * @author jv.lee
 * @date 2020/4/1
 * @description 自定义toolbar容器
 */
open class CustomToolbarLayout : FrameLayout {

    private var statusBarHeight = 0
    private var toolbarLayoutHeight = 0

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributes: AttributeSet) : this(context, attributes, 0)
    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    ) {
        toolbarLayoutHeight = initLayoutHeight()
        initBackground()
        initStatusBarHeight()
        initStatusBarPadding()
//        initBottomLine()
    }

    private fun initStatusBarHeight() {
        statusBarHeight = StatusUtil.getStatusBarHeight(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //设置默认宽高比 适配沉浸式
        setMeasuredDimension(measuredWidth, toolbarLayoutHeight)
    }

    /**
     * 初始化布局高度
     */
    open fun initLayoutHeight(): Int {
        val toolbarHeight = resources.getDimension(R.dimen.toolbar_height).toInt()
        val statusHeight = StatusUtil.getStatusBarHeight(context)
        return toolbarHeight + statusHeight
    }

    /**
     * 设置默认背景色
     */
    open fun initBackground() {
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorThemeItem))
    }

    /**
     * 设置状态栏填充padding
     */
    open fun initStatusBarPadding() {
        setPadding(context.dp2px(16).toInt(), statusBarHeight, context.dp2px(16).toInt(), 0)
    }

    private fun initBottomLine() {
        val lineView = View(context)
        lineView.run {
            id = R.id.toolbar_line
            layoutParams = LayoutParams(MATCH_PARENT, context.px2dp(1).toInt()).apply {
                gravity = Gravity.BOTTOM
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

    /**
     * 获取toolbarLayout高度
     */
    fun getToolbarLayoutHeight() = toolbarLayoutHeight

    fun getStatusBarHeight() = statusBarHeight

}