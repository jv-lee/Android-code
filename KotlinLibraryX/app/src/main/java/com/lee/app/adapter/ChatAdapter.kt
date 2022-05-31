package com.lee.app.adapter

import android.content.Context
import com.lee.library.adapter.base.BaseViewAdapter

/**
 *
 * @author jv.lee
 * @date 2020/9/7

 */
class ChatAdapter(context: Context, data: ArrayList<String>) :
    BaseViewAdapter<String>(context, data) {
    init {
        addItemStyles(ChatItem())
    }
}