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
class WordLengthInputFilter : InputFilter {
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
        var dest = destOriginal?.toString() ?: ""

        var newCurrentWord = currentWordCount
        if (dstart < dend) { //先计算删除的删除
            newCurrentWord -= this.calculateWordCount(dest.substring(dstart, dend))
                .count

            if (dest.isNotEmpty()) { //原先字符串不为空，才需要处理

                if (dstart == 0 && dend == dest.length) { //全删了
                    newCurrentWord = 0
                } else if (dstart == 0) { //从首位开始删除
                    if (isNotSplitChar(dest[dend - 1]) && isNotSplitChar(dest[dend])) { //如果是拆分的一个单词，则+1
                        newCurrentWord++
                    }
                } else if (dend == dest.length) { //在末尾添加字符
                    if (isNotSplitChar(dest[dstart - 1]) && isNotSplitChar(dest[dstart])) { //如果是拆分的一个单词，则+1
                        newCurrentWord++
                    }
                } else { //在中间删除
                    if (isNotSplitChar(dest[dend - 1]) && isNotSplitChar(dest[dstart])) {  //两端都不是标点符号
                        newCurrentWord++
                    }
                    if (!isNotSplitChar(dest[dend - 1]) && !isNotSplitChar(dest[dstart])) { //如果两端都是标点符号
                        newCurrentWord--
                    }
                }
            }

            dest = if (dstart == 0 && dend == dest.length) {
                ""
            } else if (dstart == 0) {
                safeDestOriginal.substring(dend, safeDestOriginal.length)
            } else if (dend == dest.length) {
                safeDestOriginal.substring(0, dstart)
            } else {
                safeDestOriginal.substring(0, dstart) + safeDestOriginal.substring(dend, safeDestOriginal.length)
            }
        }

        if (newCurrentWord == maxLength || source.isEmpty()) { //删完刚好满
            currentWordCount = newCurrentWord
            watchLength()
            return ""
        }

        if (dest.isNotEmpty()) { //原先字符串不为空，才需要处理
            if (dstart == 0) { //在首位添加字符
                if (isNotSplitChar(dest[0]) && isNotSplitChar(source[source.length - 1])) { //如果原句开头不是标点符号，新句末尾也不是标点符号
                    newCurrentWord--
                }
            } else if (dstart == dest.length) { //在末尾添加字符
                if (isNotSplitChar(dest[dest.length - 1]) && isNotSplitChar(source[0])) { //如果原句开头不是标点符号，新句末尾也不是标点符号
                    newCurrentWord--
                }
            } else { //在中间插入
                newCurrentWord++ //原句被分成两半
                if (isNotSplitChar(dest[dstart - 1]) && isNotSplitChar(source[0])) { //如果原句开头不是标点符号，新句末尾也不是标点符号
                    newCurrentWord--
                }
                if (isNotSplitChar(dest[dstart]) && isNotSplitChar(source[source.length - 1])) { //如果原句开头不是标点符号，新句末尾也不是标点符号
                    newCurrentWord--
                }
            }
        }

        val sourceCalculateResult = this.calculateWordCount(source.toString())

        if (newCurrentWord + sourceCalculateResult.count <= maxLength) {
            newCurrentWord += sourceCalculateResult.count
            currentWordCount = newCurrentWord
            watchLength()
            return null
        }

        var count = 0
        val wordIterator = sourceCalculateResult.iterator
        var start = wordIterator.first()
        var end = wordIterator.next()

        while (end != BreakIterator.DONE) {
            val c = source[start]

            if (isNotSplitChar(c)) {
                count++
                if (newCurrentWord + count >= maxLength) {
                    break
                }
            }

            start = end
            end = wordIterator.next()
        }
        newCurrentWord += count
        currentWordCount = newCurrentWord
        watchLength()
        return source.substring(0, end)
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