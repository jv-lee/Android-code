package com.lee.library.widget.linear

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView

/**
 * @author jv.lee
 * @date 2020/9/7
 *
 */
class LinearListView constructor(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    fun setAdapter(adapter: LinearAdapter<*>) {
        adapter.bindRoot(this)
    }

    fun scrollByPosition(position: Int) {
        try {
            var height = 0
            for (index in 0..position) {
                height += getChildAt(index).height
            }
            scrollY = height
        } catch (e: Exception) {
        }
    }

    fun scrollByPosition(position: Int, nestedScrollView: NestedScrollView?) {
        nestedScrollView ?: return
        try {
            var height = 0
            for (index in 0..position) {
                height += getChildAt(index).height
            }
            nestedScrollView.scrollY = height
        } catch (e: Exception) {
        }
    }

}