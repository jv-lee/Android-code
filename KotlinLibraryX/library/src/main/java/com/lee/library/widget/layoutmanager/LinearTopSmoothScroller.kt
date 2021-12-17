package com.lee.library.widget.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 * @author jv.lee
 * @date 2021/11/18
 * @description recyclerView滑动到指定position ，并将该item滚动到顶部
 */
class LinearTopSmoothScroller(context: Context, targetPosition: Int = 0) :
    LinearSmoothScroller(context) {

    init {
        setTargetPosition(targetPosition)
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }
}