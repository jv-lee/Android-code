package com.lee.app.adapter

import android.app.Dialog
import android.content.Context
import com.lee.library.adapter.base.BaseViewAdapter

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description
 */
class FormAdapter(context: Context, data: ArrayList<String>) :
    BaseViewAdapter<String>(context, data) {

    companion object {
        val dialogMap by lazy { HashMap<Int, Dialog>() }
    }

    init {
        addItemStyles(FormItem())
    }
}