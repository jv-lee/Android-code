package com.lee.library.widget.linear

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description
 */
class LinearListView constructor(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    fun setAdapter(adapter: LinearAdapter<*>) {
        adapter.bindRoot(this)
    }
}