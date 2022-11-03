package com.lee.library.interadp

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

/**
 * TextView EditText文本监听DSL实现
 * @author jv.lee
 * @date 2022/5/11
 */
fun TextView.addTextChangeListener(init: TextWatcherDslImpl.() -> Unit) {
    val listener = TextWatcherDslImpl()
    listener.init()
    this.addTextChangedListener(listener)
}

class TextWatcherDslImpl : TextWatcher {

    private var onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var afterTextChange: ((Editable?) -> Unit)? = null

    fun onTextChanged(method: (CharSequence?, Int, Int, Int) -> Unit) {
        onTextChanged = method
    }

    fun beforeTextChanged(method: (CharSequence?, Int, Int, Int) -> Unit) {
        beforeTextChanged = method
    }

    fun afterTextChanged(method: (Editable?) -> Unit) {
        afterTextChange = method
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged?.invoke(s, start, before, count)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChange?.invoke(s)
    }
}