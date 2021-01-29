package com.lee.wordstatistics.input.edit

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import com.lee.wordstatistics.WordCounter
import java.util.concurrent.Executors

/**
 * @author jv.lee
 * @date 2021/1/29
 * @description
 */
abstract class WordCounterWatcher(private val context: Context) : TextWatcher {

    private val wordCounter by lazy { WordCounter(context) }
    private val executors = Executors.newSingleThreadExecutor()

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val text = s.toString()
        executors.submit {
            val wordCount = wordCounter.getWordCount(text)
            Handler(Looper.getMainLooper()).post {
                try {
                    changeTextLimit(wordCounter, text, wordCount)
                } catch (e: Exception) {
                }
            }
        }
    }

    abstract fun changeTextLimit(wordCounter: WordCounter, text: String, count: Int)

}