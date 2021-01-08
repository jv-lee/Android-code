package com.lee.wordstatistics.input.edit

import android.text.InputFilter
import android.text.Spanned
import com.dreame.reader.common.ui.widgets.edittext.LengthWatcher
import java.text.BreakIterator
import java.util.*

/**
 * Author: xiaoshilin
 * Date: 2020/6/1
 */
class WordLengthBySpaceInputFilter : InputFilter {
    companion object {
        const val DEFAULT_LIMIT_SIZE = 30
        const val DEFAULT_MIN_SIZE = 1
    }

    var maxLength = DEFAULT_LIMIT_SIZE
    var minLength = DEFAULT_MIN_SIZE
    var watcher: LengthWatcher? = null

    private var currentWordCount = 0

    override fun filter(sourceOriginal: CharSequence?, start: Int, end: Int, destOriginal: Spanned?, dstart: Int, dend: Int): CharSequence? {
        val source = sourceOriginal?.toString() ?: ""
        val safeDestOriginal = destOriginal?.toString() ?: ""
        var currentWordAfterDest = currentWordCount

        val dest = if (dstart < dend && safeDestOriginal.length >= dend) { //先计算删除的删除
            (if (dstart == 0 && dend == safeDestOriginal.length) {
                ""
            } else if (dstart == 0) {
                safeDestOriginal.substring(dend, safeDestOriginal.length)
            } else if (dend == safeDestOriginal.length) {
                safeDestOriginal.substring(0, dstart)
            } else {
                safeDestOriginal.substring(0, dstart) + safeDestOriginal.substring(dend, safeDestOriginal.length)
            }).apply {
                currentWordAfterDest = calculateWordCount(this).count
            }
        } else {
            safeDestOriginal
        }

        if (currentWordAfterDest == maxLength || source.isEmpty()) { //删完刚好满
            currentWordCount = currentWordAfterDest
            watchLength()
            return ""
        }

        val destStartHalf = dest.substring(0, dstart)
        val destEndHalf = if (dstart < dest.length) dest.substring(dstart) else ""

        val resultCalculate = if (dest.isNotEmpty()) { //原先字符串不为空，才需要处理
            val resultString = StringBuilder(destStartHalf).append(sourceOriginal)
                .append(destEndHalf)

            calculateWordCount(resultString.toString())
        } else {
            calculateWordCount(source)
        }

        if (resultCalculate.count <= maxLength) {
            currentWordCount = resultCalculate.count
            watchLength()
            return null
        }

        val wordIterator = resultCalculate.iterator
        var iteratorStart = wordIterator.first()
        var iteratorEnd = wordIterator.next()

        while (iteratorEnd != BreakIterator.DONE) {
            val s = sourceOriginal?.substring(0, iteratorEnd) ?: ""
            val resultString = StringBuilder(destStartHalf).append(s)
                .append(destEndHalf)
            val calculateWordCount = calculateWordCount(resultString.toString())
            if (calculateWordCount.count >= maxLength) {
                currentWordCount = calculateWordCount.count
                watchLength()
                return s
            }

            iteratorStart = iteratorEnd
            iteratorEnd = wordIterator.next()
        }

        currentWordCount = currentWordAfterDest //异常结果
        watchLength()
        return ""
    }

    fun clear() {
        currentWordCount = 0
    }

    private fun watchLength() {
        watcher?.onChanged(currentWordCount, minLength, maxLength)
    }

    private fun calculateWordCount(inputOriginal: String): CalculateResult {
        val input = inputOriginal.replace('.', ',', false) //替换只用于计算字数
        var count = 0
        val wordIterator = BreakIterator.getWordInstance(Locale.ENGLISH)
        wordIterator.setText(input)
        var start = wordIterator.first()
        var end = wordIterator.next()

        while (end != BreakIterator.DONE) {
            val c = input[start]

            start = end
            end = wordIterator.next()

            if (isNotSplitChar(c)) {
                count++
            }
        }
        return CalculateResult(wordIterator, count)
    }

    /**
     * 不是标点符号及空格及表情
     */
    private fun isNotSplitChar(c: Char): Boolean {
        val unicodeBlock = Character.UnicodeBlock.of(c)
        return ((unicodeBlock != Character.UnicodeBlock.BASIC_LATIN || Character.isLetterOrDigit(c)) //英文标点符号及空格是BASIC_LATIN，但是英文字母也是BASIC_LATIN，所以这里要排除
                && unicodeBlock != Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                && unicodeBlock != Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                && unicodeBlock != Character.UnicodeBlock.HIGH_SURROGATES
                && unicodeBlock != Character.UnicodeBlock.LOW_SURROGATES
                && unicodeBlock != Character.UnicodeBlock.GENERAL_PUNCTUATION) //不是标点符号
    }

    private data class CalculateResult(val iterator: BreakIterator, val count: Int)
}