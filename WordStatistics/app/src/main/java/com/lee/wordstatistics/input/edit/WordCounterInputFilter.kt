package com.lee.wordstatistics.input.edit

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import com.dreame.reader.common.ui.widgets.edittext.LengthWatcher
import com.lee.wordstatistics.WordCounter

/**
 * Author: xiaoshilin
 * Date: 2020/6/1
 */
class WordCounterInputFilter(context: Context) : InputFilter {
    companion object {
        const val DEFAULT_LIMIT_SIZE = 30
        const val DEFAULT_MIN_SIZE = 1
    }

    var maxLength = DEFAULT_LIMIT_SIZE
    var minLength = DEFAULT_MIN_SIZE
    var watcher: LengthWatcher? = null

    private var currentWordCount = 0

    private val wordCounter = WordCounter(context.applicationContext)

    override fun filter(
        sourceOriginal: CharSequence?,
        start: Int,
        end: Int,
        destOriginal: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val source = sourceOriginal?.toString() ?: ""
        val dest = destOriginal?.toString() ?: ""

        //字符清空
        if (source.isEmpty() && dstart == 0) {
            clear()
            watchLength()
            return ""
        }

        //删除校验
        if (source.isEmpty()) {
            val content = dest + source
            currentWordCount = wordCounter.getWordCount(content.substring(0, content.length - 1))
            watchLength()
            return null
        }

        //满足长度 限制空格输入
        if (currentWordCount == maxLength && source == " ") {
            return ""
        }

        //输入校验
        val content = dest + source
        currentWordCount = wordCounter.getWordCount(content)
        if (currentWordCount <= maxLength) {
            watchLength()
            return null
        }

        //粘贴校验
        val dCount = wordCounter.getWordCount(dest)
        val sCount = wordCounter.getWordCount(source)
        //有内容超出长度粘贴校验
        if (dCount < maxLength && (sCount + dCount) > maxLength) {
            currentWordCount = maxLength
            watchLength()
            return wordCounter.getWordLimitContent(dest + source, maxLength - dCount)
            //无内容超出长度粘贴校验
        } else if (dCount == 0 && sCount > maxLength) {
            currentWordCount = maxLength
            watchLength()
            return wordCounter.getWordLimitContent(source, maxLength)
        }

        //条件未满足
        return ""
    }

    fun clear() {
        currentWordCount = 0
    }

    private fun watchLength() {
        watcher?.onChanged(currentWordCount, minLength, maxLength)
    }

}