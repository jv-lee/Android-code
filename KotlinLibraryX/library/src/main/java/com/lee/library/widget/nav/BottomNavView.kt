package com.lee.library.widget.nav

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.lee.library.extensions.dp2px
import com.lee.library.utils.ReflexUtil.reflexField

/**
 * @author jv.lee
 * @date 2019/5/7
 * @description 使用png 等多色彩图片时 需要动态设置 itemIconTintList = null
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
class BottomNavView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr),
    BottomNavigationView.OnNavigationItemSelectedListener {
    private var mViewPager: ViewPager? = null
    private var mItemPositionListener: ItemPositionListener? = null
    private var numberDots = ArrayList<NumberDotView>()
    private var dots = ArrayList<DotView>()
    private var isNumberInit = false
    private var isDotsInit = false

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        for (i in 0 until menu.size()) {
            if (menu.getItem(i) === menuItem) {
                mViewPager?.setCurrentItem(i, false)
                mItemPositionListener?.onPosition(menuItem, i)
            }
        }
        return true
    }

    fun toPosition(position: Int) {
        selectedItemId = menu.getItem(position).itemId
    }

    private fun createNumberDotViews() {
        //初始化红点view
        var menuView: BottomNavigationMenuView? = null
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is BottomNavigationMenuView) {
                menuView = child
                break
            }
        }
        menuView?.run {
            numberDots.clear()
            for (i in 0 until childCount) {
                val itemView =
                    menuView.getChildAt(i) as BottomNavigationItemView
                val params = LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val imageView =
                    reflexField<ImageView>(itemView, "icon")
                imageView?.let {
                    params.topMargin = it.top
                    params.leftMargin = it.right
                }
                val dotView = NumberDotView(context)
                if (!itemView.children.contains(dotView)) itemView.addView(dotView, params)
                numberDots.add(dotView)
            }
            isNumberInit = true
        }
    }

    private fun createDotViews() {
        //初始化红点view
        var menuView: BottomNavigationMenuView? = null
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is BottomNavigationMenuView) {
                menuView = child
                break
            }
        }
        menuView?.run {
            dots.clear()
            for (i in 0 until childCount) {
                val itemView = menuView.getChildAt(i) as BottomNavigationItemView
                val params = LayoutParams(context.dp2px(6).toInt(), context.dp2px(6).toInt())
                params.gravity = Gravity.CENTER_HORIZONTAL
                params.topMargin = context.dp2px(6).toInt()
                params.leftMargin = context.dp2px(6).toInt()
                val dotView = DotView(context)
                dotView.visibility = View.GONE
                if (!itemView.children.contains(dotView)) itemView.addView(dotView, params)
                dots.add(dotView)
            }
            isDotsInit = true
        }
    }

    private fun toNumberDot(index: Int, number: Int) {
        if (numberDots.size > index) numberDots[index].setNumberCount(number)
    }

    private fun toDotVisibility(index: Int, visibility: Int) {
        if (dots.size > index) dots[index].visibility = visibility
    }

    fun setNumberDot(index: Int, number: Int) {
        if (isNumberInit) {
            toNumberDot(index, number)
        } else {
            postDelayed({ toNumberDot(index, number) }, 100)
        }
    }

    fun setDotVisibility(index: Int, visibility: Int) {
        if (isDotsInit) {
            toDotVisibility(index, visibility)
        } else {
            postDelayed({ toDotVisibility(index, visibility) }, 100)
        }
    }

    interface ItemPositionListener {
        fun onPosition(menuItem: MenuItem?, position: Int)
    }

    fun setItemPositionListener(mItemPositionListener: ItemPositionListener?) {
        this.mItemPositionListener = mItemPositionListener
    }

    fun bindViewPager(mViewPager: ViewPager?) {
        this.mViewPager = mViewPager
    }

    init {
        isHorizontalFadingEdgeEnabled = false
        labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        setOnNavigationItemSelectedListener(this)
        post {
            createNumberDotViews()
            createDotViews()
        }
    }
}