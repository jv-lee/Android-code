package com.lee.library.widget.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 * @author jv.lee
 * @data 2021/11/18
 * @description
 */
class LinearTopSmoothScroller(context: Context) : LinearSmoothScroller(context) {
    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }
}