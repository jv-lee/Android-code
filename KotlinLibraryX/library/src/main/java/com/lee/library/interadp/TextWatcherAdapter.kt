package com.lee.library.interadp

import android.text.Editable
import android.text.TextWatcher

/**
 * 文本输入接口实现帮助类
 * @author jv.lee
 * @date 2021/11/26
 */
interface TextWatcherAdapter : TextWatcher {
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

    }
}