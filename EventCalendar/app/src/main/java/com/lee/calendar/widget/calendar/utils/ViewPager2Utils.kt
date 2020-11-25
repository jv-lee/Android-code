package com.lee.calendar.widget.calendar.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager2.widget.ViewPager2

/**
 * @author jv.lee
 * @date 2020/11/15
 * @description 反射类 正式包混淆需要过滤 -keep public class androidx.viewpager2.widget.ViewPager2 { *; }
 */
object ViewPager2Utils {
    fun closeItemAnim(viewPager: ViewPager2) {
        try {
            val mRecyclerView = viewPager.javaClass.getDeclaredField("mRecyclerView")
            mRecyclerView.isAccessible = true
            val rvObject = mRecyclerView[viewPager]
            rvObject?.let {
                val itemAnimator = (it as RecyclerView).itemAnimator
                it.overScrollMode = View.OVER_SCROLL_NEVER
                itemAnimator?.let { item->
                    item.addDuration = 0
                    item.changeDuration = 0
                    item.moveDuration = 0
                    item.removeDuration = 0
                    (item as SimpleItemAnimator).supportsChangeAnimations = false
                }
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}