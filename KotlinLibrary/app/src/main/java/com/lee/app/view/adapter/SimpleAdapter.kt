package com.lee.app.view.adapter

import android.content.Context
import com.lee.library.adapter.LeeViewAdapter

/**
 * @author jv.lee
 * @date 2019/9/17.
 * @description
 */
class SimpleAdapter(context: Context, data: List<Int>) : LeeViewAdapter<Int>(context, data) {

    init {
        addItemStyles(SimpleItem())
    }

}