/*
 * View.扩展函数
 * @author jv.lee
 * @date 2020/4/1
 */
package com.lee.library.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.text.InputFilter
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.size
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.lee.library.R
import com.lee.library.tools.ReflexTools
import kotlin.math.abs

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
    background ?: return
    val mutate: Drawable? = background.mutate()
    mutate?.let {
        it.alpha = alpha
    } ?: kotlin.run {
        background.alpha = alpha
    }
}

/**
 * 获取view背景透明度兼容方法
 */
fun View.getBackgroundAlphaCompat(): Int {
    background ?: return 0
    val mutate = background.mutate()
    return mutate?.alpha ?: background.alpha
}

/**
 * 设置view背景颜色兼容方法
 */
fun View.setBackgroundColorCompat(color: Int) {
    setBackgroundColor(ContextCompat.getColor(context, color))
}

/**
 * 设置view背景资源兼容方法
 */
fun View.setBackgroundDrawableCompat(drawableId: Int) {
    background = ContextCompat.getDrawable(context, drawableId)
}

/**
 * 设置文本颜色兼容方法
 */
fun TextView.setTextColorCompat(color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
}

/**
 * TextView设置 四个位置drawable
 */
fun TextView.setDrawableCompat(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0,
    tint: Int = 0
) {
    val leftDrawable = if (left == 0) null else getTintDrawableCompat(context, left, tint)
    leftDrawable?.setBounds(0, 0, leftDrawable.minimumWidth, leftDrawable.minimumHeight)

    val topDrawable = if (top == 0) null else getTintDrawableCompat(context, top, tint)
    topDrawable?.setBounds(0, 0, topDrawable.minimumWidth, topDrawable.minimumHeight)

    val rightDrawable = if (right == 0) null else getTintDrawableCompat(context, right, tint)
    rightDrawable?.setBounds(0, 0, rightDrawable.minimumWidth, rightDrawable.minimumHeight)

    val bottomDrawable = if (bottom == 0) null else getTintDrawableCompat(context, bottom, tint)
    bottomDrawable?.setBounds(0, 0, bottomDrawable.minimumWidth, bottomDrawable.minimumHeight)

    setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, bottomDrawable)
}

/**
 * ImageView扩展函数 向下兼容Tint着色器
 */
fun ImageView.setImageTintCompat(drawableId: Int, color: Int = 0) {
    val drawable = getTintDrawableCompat(context, drawableId, color)
    setImageDrawable(drawable)
}

private fun getTintDrawableCompat(context: Context, drawableId: Int, color: Int = 0): Drawable? {
    if (drawableId == 0) {
        return null
    }
    var drawable = ContextCompat.getDrawable(context, drawableId)
    if (color == 0) {
        return drawable
    }
    val colors = intArrayOf(color, color)
    val states = arrayOfNulls<IntArray>(2)
    states[0] = intArrayOf(android.R.attr.state_pressed)
    states[1] = intArrayOf()
    val colorList = ColorStateList(states, colors)
    val stateListDrawable = StateListDrawable()
    stateListDrawable.addState(states[0], drawable) // 注意顺序

    stateListDrawable.addState(states[1], drawable)
    val state = stateListDrawable.constantState
    drawable = DrawableCompat.wrap(state?.newDrawable() ?: stateListDrawable).mutate()
    DrawableCompat.setTintList(drawable, colorList)
    return drawable
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
 * 设置view 前景色点击效果
 */
fun View.setSelectableItemForeground() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val attrs = intArrayOf(R.attr.selectableItemBackground)
        val typedArray = context.obtainStyledAttributes(attrs)
        val backgroundResource: Int = typedArray.getResourceId(0, 0)
        setBackgroundResource(backgroundResource)
        typedArray.recycle()
    }
}

/**
 * NestedScrollView 滑动改变view 透明度及view状态
 */
inline fun NestedScrollView.setScrollTransparent(
    limit: Int,
    crossinline transparentBar: (Boolean, Int) -> Unit
) {
    setOnScrollChangeListener(
        NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY < limit) {
                transparentBar(true, scrollY)
            } else {
                transparentBar(false, 255)
            }
            return@OnScrollChangeListener
        }
    )
}

/**
 * RecyclerView 滑动改变view 透明度及view状态
 */
inline fun RecyclerView.setScrollTransparent(crossinline transparentBar: (Boolean, Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (recyclerView.layoutManager !is LinearLayoutManager) return
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val position = linearLayoutManager.findFirstVisibleItemPosition()
            if (position == 0) {
                linearLayoutManager.findViewByPosition(position)?.let {
                    val scale = (255 / it.height)
                    transparentBar(true, (abs(it.top) * scale).toInt())
                }
            } else {
                transparentBar(false, 255)
            }
        }
    })
}

/**
 * RecyclerView 滑动改变view 透明度及view状态
 */
inline fun RecyclerView.setScrollTransparent2(crossinline transparentBar: (Boolean, Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (recyclerView.layoutManager !is LinearLayoutManager) return
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val position = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
            if (position == 0) {
                linearLayoutManager.findViewByPosition(position)?.let {
                    val scale = (255 / it.height)
                    transparentBar(true, (abs(it.top) * scale).toInt())
                }
            } else {
                transparentBar(false, 255)
            }
        }
    })
}

/**
 * 回调RecyclerView滑动高度
 * @return 滑动距离 - px值
 */
inline fun RecyclerView.callScrollHeight(crossinline callScroll: (Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        private var scrollHeight = 0
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            scrollHeight += dy
            callScroll(scrollHeight)
        }
    })
}

/**
 * 设置输入框最大输入长度
 */
fun EditText.setMaxSize(maxSize: Int) {
    filters = arrayOf(InputFilter.LengthFilter(maxSize))
}

/**
 * 设置输入框最大size / 最大lines
 * @param maxLines 最大行数
 * @param maxSize 最大长度
 */
fun EditText.setMaxSizeOrLines(maxSize: Int = 0, maxLines: Int = 0) {
    if (maxSize != 0) filters = arrayOf(InputFilter.LengthFilter(maxSize))
    if (maxLines != 0) this.maxLines = maxLines
}

/**
 * 扩展容器类设置Marin方法
 */
fun ViewGroup.setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        (layoutParams as ViewGroup.MarginLayoutParams).run {
            setMargins(
                if (left == 0) leftMargin else left,
                if (top == 0) topMargin else top,
                if (right == 0) rightMargin else right,
                if (bottom == 0) bottomMargin else bottom
            )
        }
    }
}

/**
 * RecyclerView 反转布局方向
 */
fun RecyclerView.reverseLayout() {
    layoutManager?.takeIf { it is LinearLayoutManager }.let {
        (it as LinearLayoutManager).reverseLayout = true
    }
}

/**
 * RecyclerView 多数据列表快速滑动到顶部
 */
fun RecyclerView.smoothScrollToTop() {
    val itemCount = adapter?.itemCount ?: return
    if (itemCount == 0) return

    if (layoutManager is LinearLayoutManager &&
        (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() == 0
    ) {
        scrollToPosition(0)
        return
    }

    scrollToPosition(5)
    postDelayed({ smoothScrollToPosition(0) }, 50)
}

/**
 * webView设置back事件拦截
 */
fun WebView.setWebBackEvent() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
    setOnKeyListener(object : View.OnKeyListener {
        override fun onKey(view: View?, i: Int, keyEvent: KeyEvent?): Boolean {
            if (canGoBack()) {
                goBack()
                return true
            }
            return false
        }
    })
}

/**
 * 选择radioButton 不通知监听器
 * @param id radioButtonViewId
 */
fun RadioGroup.checkUnNotification(@IdRes id: Int) {
    findViewById<RadioButton>(id).isChecked = true
}

/**
 * 根据滑动page，慢慢递增page缓存
 * 解决一次性缓存所有page页面初始化卡顿问题
 */
fun ViewPager2.increaseOffscreenPageLimit() {
    registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 0) return
                adapter?.run {
                    val limit = itemCount - 1
                    val offset = position * 2
                    if (offscreenPageLimit >= limit) return
                    if (offscreenPageLimit < offset) {
                        offscreenPageLimit = if (offset >= limit) limit else offset
                    }
                }
            }
        })
}

/**
 * 根据滑动page，慢慢递增page缓存
 * 解决一次性缓存所有page页面初始化卡顿问题
 */
fun TabLayout.increaseOffscreenPageLimit(viewPager: ViewPager2) {
    viewPager.increaseOffscreenPageLimit()
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.run {
                if (position == 0) return
                val limit = tabCount - 1
                val offset = position * 2
                if (viewPager.offscreenPageLimit >= limit) return
                if (viewPager.offscreenPageLimit < offset) {
                    viewPager.offscreenPageLimit = if (offset >= limit) limit else offset
                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    })
}

/**
 * ViewPager2 联动RadioGroup
 * @param radioGroup 联动的View
 */
fun ViewPager2.bindRadioGroup(radioGroup: RadioGroup) {
    registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (radioGroup.size <= position) return

                val button = (radioGroup.getChildAt(position) as RadioButton)

                if (!button.isChecked) button.isChecked = true
            }
        })
    radioGroup.setOnCheckedChangeListener { group, checkedId ->
        val button = group.findViewById<RadioButton>(checkedId)

        group.children.forEachIndexed { index, view ->
            if (button == view && index != currentItem) {
                setCurrentItem(index, false)
            }
        }
    }
}

/**
 * 通过反射设置viewPager2阴影
 */
fun ViewPager2.overScrollNever() {
    val recyclerView = ReflexTools.reflexField<RecyclerView>(this, "mRecyclerView")
    recyclerView?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
}
