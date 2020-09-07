package com.lee.app.adapter

import android.content.Context
import com.lee.library.adapter.LeeViewAdapter

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description
 */
class FormAdapter(context: Context, data: ArrayList<String>) :
    LeeViewAdapter<String>(context, data) {
    init {
        addItemStyles(FormItem())
    }
}