package com.lee.app.adapter

import android.app.Dialog
import android.content.Context
import com.lee.library.adapter.base.BaseViewAdapter

/**
 *
 * @author jv.lee
 * @date 2020/9/7
 */
class FormAdapter(context: Context) :
    BaseViewAdapter<String>(context) {

    companion object {
        val dialogMap by lazy { HashMap<Int, Dialog>() }
    }

    init {
        addItemStyles(FormItem())
    }
}