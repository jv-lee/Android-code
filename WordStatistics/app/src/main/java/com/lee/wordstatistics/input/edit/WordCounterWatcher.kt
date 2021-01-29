package com.lee.wordstatistics.input.edit

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import com.lee.wordstatistics.WordCounter

/**
 * @author jv.lee
 * @date 2021/1/29
 * @description
 */
abstract class WordCounterWatcher(context: Context) : TextWatcher {

    private val wordCounter by lazy { WordCounter(context) }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val count = wordCounter.getWordCount(s.toString())
        changeTextLimit(wordCounter,s.toString(),count)
    }

    abstract fun changeTextLimit(wordCounter: WordCounter,text:String,count:Int)

}