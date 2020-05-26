package com.lee.library.widget.toolbar

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.lee.library.R
import com.lee.library.utils.SizeUtil
import com.lee.library.utils.StatusUtil


/**
 * @author jv.lee
 * @date 2020/4/1
 * @description 自定义toolbar容器
 */
open class CustomToolbarLayout : ConstraintLayout {

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
        initStatusBarPadding()
//        initBottomLine()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //设置默认宽高比 适配沉浸式
        setMeasuredDimension(MATCH_PARENT, toolbarLayoutHeight)
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
        val statusHeight = StatusUtil.getStatusBarHeight(context)
        setPadding(SizeUtil.dp2px(context, 16f), statusHeight, SizeUtil.dp2px(context, 16f), 0)
    }

    private fun initBottomLine() {
        val lineView = View(context)
        lineView.run {
            id = R.id.toolbar_line
//            layoutParams = LayoutParams(MATCH_PARENT, SizeUtil.px2dp(context, 1f))
            lineView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorThemeBackground
                )
            )
            addView(this)
            val set = ConstraintSet()
            set.connect(
                id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            set.constrainWidth(id, ConstraintSet.MATCH_CONSTRAINT)
            set.constrainHeight(id, SizeUtil.px2dp(context, 1f))
            set.applyTo(this@CustomToolbarLayout)
        }
    }

    /**
     * 获取toolbarLayout高度
     */
    fun getToolbarLayoutHeight(): Int {
        return toolbarLayoutHeight
    }

}