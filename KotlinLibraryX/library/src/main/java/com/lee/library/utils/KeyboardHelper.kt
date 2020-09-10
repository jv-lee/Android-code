package com.lee.library.utils

import android.annotation.TargetApi
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author jv.lee
 * @date 2020/9/8
 * @description 当主题中用了<item name="android:windowTranslucentStatus">true</item>后，软键盘弹出就不会将输入框往上推了，该类可以解决这个问题。
 * http://stackoverflow.com/a/9108219/325479 * https://github.com/mikepenz/MaterialDrawer/blob/aa9136fb4f5b3a80460fe5f47213985026d20c88/library/src/main/java/com/mikepenz/materialdrawer/util/KeyboardUtil.java
 * //扩展
 * 沉浸式状态栏控制键盘弹起 view/recyclerView 自适应布局帮助类 , 非沉浸式直接设置 android:windowSoftInputMode="adjustResize" 无需其他操作即可
 * 单独设置普通布局 无需设置RecyclerView的话 构造后直接调用enable
 * 如需设置RecyclerView 先调用bindRecyclerView方法 再开启 enable方法
 */
class KeyboardHelper(
    private val decorView: View,
    private val contentView: View
) {

    private var tempDiff = 0
    private var isEnd = false
    private var isStart = false
    private var reverse = false
    private var recyclerView: RecyclerView? = null

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun enable() {
        decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        recyclerView?.addOnScrollListener(onScrollListener)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun disable() {
        decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        recyclerView?.removeOnScrollListener(onScrollListener)
    }

    /**
     * 设置RecyclerView layoutManager反转布局及键盘弹起适配
     * @param recyclerView
     */
    fun bindRecyclerView(recyclerView: RecyclerView, reverse: Boolean = false) {
        this.recyclerView = recyclerView
        this.reverse = reverse
        this.isEnd = reverse
        if (!reverse) {
            return
        }
        val layoutManager = recyclerView.layoutManager
        layoutManager?.let {
            if (it is LinearLayoutManager) {
                it.reverseLayout = true
                it.stackFromEnd = true
            }
        }
        recyclerView.scrollToPosition(0)
    }

    private var onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            layoutManager?.let {
                val lastVisibleItemPosition = it.findLastCompletelyVisibleItemPosition()
                isEnd = lastVisibleItemPosition == it.itemCount - 1
                val firstVisibleItemPosition = it.findFirstCompletelyVisibleItemPosition()
                isStart = firstVisibleItemPosition == 0
            }
        }
    }

    /**
     * a small helper to allow showing the editText focus
     */
    private var onGlobalLayoutListener = OnGlobalLayoutListener {
        val r = Rect()
        //r will be populated with the coordinates of your view that area still visible.
        decorView.getWindowVisibleDisplayFrame(r)

        //get screen height and calculate the difference with the useable area from the r
        val height: Int = decorView.context.resources.displayMetrics.heightPixels
        val diff = height - r.bottom

        //if it could be a keyboard add the padding to the view
        if (diff != 0) {
            tempDiff = diff
            // if the use-able screen height differs from the total screen height we assume that it shows a keyboard now
            //check if the padding is 0 (if yes set the padding for the keyboard)
            if (contentView.paddingBottom != diff) {
                //set the padding of the contentView for the keyboard
                contentView.setPadding(0, 0, 0, diff)
                //动态设置recyclerView滑动适配距离
                if (reverse) recyclerViewMoveReverse(diff)
                else recyclerViewMove(diff)
            }
        } else {
            //check if the padding is != 0 (if yes reset the padding)
            if (contentView.paddingBottom != 0) {
                //reset the padding of the contentView
                contentView.setPadding(0, 0, 0, 0)
                //动态设置recyclerView滑动适配距离
                if (reverse) recyclerViewMoveReverse(-tempDiff, false)
                else recyclerViewMove(-tempDiff, false)
                //清除临时diff值
                tempDiff = 0
            }
        }
    }

    /**
     * 顺序数据列表设置diff值
     * @param diff 列表滑动距离
     * @param open 是否未打开键盘时
     */
    private fun recyclerViewMove(diff: Int, open: Boolean = true) {
        recyclerView?.let {
            if (open) {
                it.postDelayed({ it.smoothScrollBy(0, diff) }, 100)
            }
            if (!open && !isEnd) {
                it.smoothScrollBy(0, -tempDiff)
            }
        }
    }

    /**
     * 逆序数据列表设置diff值
     * @param diff 列表滑动距离
     * @param open 是否未打开键盘时
     */
    private fun recyclerViewMoveReverse(diff: Int, open: Boolean = true) {
        recyclerView?.let {
            if (open) {
                if (it.adapter?.itemCount!! > 5) {
                    it.scrollToPosition(5)
                }
                it.postDelayed({
                    it.smoothScrollToPosition(0)
                }, 100)
            }
        }
    }

}