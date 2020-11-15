package com.lee.calendar.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager2.widget.ViewPager2

/**
 * @author jv.lee
 * @date 2020/11/15
 * @description
 */
object ViewPager2Utils {
    fun closeItemAnim(viewPager: ViewPager2) {
        try {
            val mRecyclerView = viewPager.javaClass.getDeclaredField("mRecyclerView")
            mRecyclerView.isAccessible = true
            val rvObject = mRecyclerView[viewPager]
            rvObject?.let {
                val itemAnimator = (it as RecyclerView).itemAnimator
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