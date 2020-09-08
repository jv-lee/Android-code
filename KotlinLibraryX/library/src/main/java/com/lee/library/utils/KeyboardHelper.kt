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
 * @description 键盘弹起 view/recyclerView 自适应布局帮助类
 */
class KeyboardHelper(
    private val decorView: View,
    private val contentView: View,
    private val recyclerView: RecyclerView? = null
) {

    private var tempDiff = 0
    private var isEnd = false
    private var reverse = false

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun enable(reverse: Boolean = false) {
        this.reverse = reverse
        this.isEnd = reverse
        decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        recyclerView?.addOnScrollListener(onScrollListener)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun disable() {
        decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        recyclerView?.removeOnScrollListener(onScrollListener)
    }

    var onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            layoutManager?.let {
                val lastVisibleItemPosition = it.findLastCompletelyVisibleItemPosition()
                isEnd = lastVisibleItemPosition == it.itemCount - 1
            }
        }
    }

    /**
     * a small helper to allow showing the editText focus
     */
    var onGlobalLayoutListener = OnGlobalLayoutListener {
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
                recyclerView?.postDelayed({
                    recyclerView.smoothScrollBy(0, diff)
                }, 100)
            }
        } else {
            //check if the padding is != 0 (if yes reset the padding)
            if (contentView.paddingBottom != 0) {
                //reset the padding of the contentView
                contentView.setPadding(0, 0, 0, 0)
                if (!isEnd) {
                    recyclerView?.smoothScrollBy(0, -tempDiff)
                }
                tempDiff = 0
            }
        }

        if (!reverse) {

        } else {
            //get screen height and calculate the difference with the useable area from the r
            val height: Int = decorView.context.resources.displayMetrics.heightPixels
            val diff = height - r.bottom
            if (diff != 0) {
                if (contentView.paddingTop != diff) {
                    contentView.setPadding(0, diff, 0, 0)
                }
            } else {
                contentView.setPadding(0, 0, 0, 0)
            }
        }
    }
}