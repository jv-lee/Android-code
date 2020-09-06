package com.lee.library.extensions

import android.content.res.ColorStateList
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.math.abs

/**
 * @author jv.lee
 * @date 2020/4/1
 * @description  View.扩展函数
 */

/**
 * 监听RecyclerView滑动状态 Glide加载模式
 */
fun RecyclerView.glideEnable() {
    if (true) {
        return
    }
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        var isDown = false
        var lastPosition = 0
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!isDown) return
            when (newState) {
                //正在拖动
                RecyclerView.SCROLL_STATE_DRAGGING ->
                    context?.let { Glide.with(it).resumeRequests() }
                //滑动停止
                RecyclerView.SCROLL_STATE_IDLE ->
                    context?.let { Glide.with(it).resumeRequests() }
                //惯性滑动中
                RecyclerView.SCROLL_STATE_SETTLING ->
                    context?.let { Glide.with(it).pauseRequests() }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val currentPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            if (currentPosition == lastPosition) {
                isDown = true
                return
            }
            isDown = false
            if (dy >= 0) {
                if (currentPosition > lastPosition) {
                    isDown = true
                    lastPosition = currentPosition
                }
            }
        }
    })
}

/**
 * 向下兼容设置View背景着色器选择器
 */
fun View.setBackgroundSelectorTintCompat(selectorId: Int) {
    ViewCompat.setBackgroundTintList(
        this,
        ColorStateList.createFromXml(
            this.resources,
            this.resources.getXml(selectorId)
        )
    )
}

/**
 * 设置view透明度兼容方法
 */
fun View.setBackgroundAlphaCompat(alpha: Int) {
    val mutate = background.mutate()
    if (mutate != null) {
        mutate.alpha = alpha
    } else {
        background.alpha = alpha
    }
}

/**
 * ImageView扩展函数 向下兼容Tint着色器
 */
fun ImageView.setImageTintCompat(drawableId: Int, color: Int = 0) {
    if (color == 0) {
        setImageResource(drawableId)
        return
    }
    var drawable =
        ContextCompat.getDrawable(context, drawableId)
    val colors = intArrayOf(color, color)
    val states = arrayOfNulls<IntArray>(2)
    states[0] = intArrayOf(android.R.attr.state_pressed)
    states[1] = intArrayOf()
    val colorList = ColorStateList(states, colors)
    val stateListDrawable = StateListDrawable()
    stateListDrawable.addState(states[0], drawable) //注意顺序

    stateListDrawable.addState(states[1], drawable)
    val state = stateListDrawable.constantState
    drawable =
        DrawableCompat.wrap(state?.newDrawable() ?: stateListDrawable)
            .mutate()
    DrawableCompat.setTintList(drawable, colorList)
    setImageDrawable(drawable)
}

/**
 * RadioButton 扩展函数 向下兼容Button Tint着色器
 */
fun RadioButton.setButtonTint(drawableId: Int, selectorId: Int) {
    val drawable =
        DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableId)!!)
    val colorStateList = ContextCompat.getColorStateList(context, selectorId)
    DrawableCompat.setTintList(drawable, colorStateList)
    buttonDrawable = drawable
}

/**
 * NestedScrollView 滑动改变view 透明度及view状态
 */
fun NestedScrollView.setScrollTransparent(limit: Int, transparentBar: (Boolean, Int) -> Unit) {
    setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
        if (scrollY < limit) {
            transparentBar(true, scrollY)
        } else {
            transparentBar(false, 255)
        }
        return@OnScrollChangeListener
    })
}

/**
 * RecyclerView 滑动改变view 透明度及view状态
 */
fun RecyclerView.setScrollTransparent( transparentBar: (Boolean, Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (recyclerView.layoutManager !is LinearLayoutManager) return
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val position = linearLayoutManager.findFirstVisibleItemPosition()
            if (position == 0) {
                linearLayoutManager.findViewByPosition(position)?.let {
                    val scale = (255 / it.height)
                    transparentBar(true,(abs(it.top) * scale).toInt())
                }
            } else {
                transparentBar(false, 255)
            }
        }
    })
}