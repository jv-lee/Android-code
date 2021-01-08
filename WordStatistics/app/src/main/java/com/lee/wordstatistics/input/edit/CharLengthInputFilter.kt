package com.dreame.reader.common.ui.widgets.edittext

import android.text.InputFilter
import android.text.Spanned

/**
 * Author: xiaoshilin
 * Date: 2020/4/28
 */
class CharLengthInputFilter() : InputFilter {
    companion object {
        const val DEFAULT_LIMIT_SIZE = 30
        const val DEFAULT_MIN_SIZE = 1
        fun getLength(str: String): Int {
            return str.length
        }
    }

    var maxLength = DEFAULT_LIMIT_SIZE
    var minLength = DEFAULT_MIN_SIZE
    var watcher: LengthWatcher? = null

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        val currentLength = getLength(dest.toString())
        val inputLength = getLength(source.toString())
        val deleteLength = getLength(dest.substring(dstart, dend))
        val resultLength = currentLength + inputLength - deleteLength
        val overLength = resultLength - maxLength

        if (overLength > 0) { //超出长度
            watcher?.onChanged(maxLength, minLength, maxLength)

            return source.substring(0, inputLength - overLength)
        }
        watcher?.onChanged(resultLength, minLength, maxLength)
        return null
    }
}