package com.lee.wordstatistics

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/**
 * @author jv.lee
 * @date 2021/1/8
 * @description
 */
class WordCounter constructor(private val context: Context) {

    private var mBreakSpaceRanges: List<IntRange>
    private var mFullWidthCharRanges: List<IntRange>

    init {
        // 分别读取分割字符和全角字符配置文件
        mBreakSpaceRanges = readConfigFile("break_spaces.txt")
        mFullWidthCharRanges = readConfigFile("fullwidth_chars.txt")
    }

    fun getWordLimitContent(content: String?, limit:Int): String {
        if (content == null || content.isEmpty()) {
            return ""
        }
        val wordBuffer = StringBuffer()
        var clearBuffer = false
        var wordCount = 0

        // 按单个字符逐一遍历整个内容串
        val chars = content.toCharArray()
        val count = chars.size
        for (i in 0 until count) {
            if (wordCount == limit) {
                return String(chars.asList().subList(0,i).toCharArray())
            }
            val c = chars[i]
            // 是否是单词拆分符号
            if (isBreakSpace(c)) {
                clearBuffer = true
            } else {
                // 是否是全角字符、全形符号
                if (isFullWidthChar(c)) {
                    wordCount++
                    clearBuffer = true
                } else {
                    // 不满足情况时将此次遍历字符加入到Buffer中
                    wordBuffer.append(c)
                }
            }
            // 末尾字符
            if (i == count - 1) {
                clearBuffer = true
            }
            // 单词拆分符号、全角字符、字符末尾时clearBuffer为true
            if (clearBuffer) {
                clearBuffer = false

                // 碰到以上3种情况时需要清空Buffer对象，字数需要累加
                if (wordBuffer.isNotEmpty()) {
                    wordBuffer.delete(0, wordBuffer.length)
                    wordCount++
                }
            }
        }
        return wordBuffer.toString()
    }

    fun getWordCount(content: String?): Int {
        if (content == null || content.isEmpty()) {
            return 0
        }
        val wordBuffer = StringBuffer()
        var clearBuffer = false
        var wordCount = 0

        // 按单个字符逐一遍历整个内容串
        val chars = content.toCharArray()
        val count = chars.size
        for (i in 0 until count) {
            val c = chars[i]
            // 是否是单词拆分符号
            if (isBreakSpace(c)) {
                clearBuffer = true
            } else {
                // 是否是全角字符、全形符号
                if (isFullWidthChar(c)) {
                    wordCount++
                    clearBuffer = true
                } else {
                    // 不满足情况时将此次遍历字符加入到Buffer中
                    wordBuffer.append(c)
                }
            }
            // 末尾字符
            if (i == count - 1) {
                clearBuffer = true
            }
            // 单词拆分符号、全角字符、字符末尾时clearBuffer为true
            if (clearBuffer) {
                clearBuffer = false

                // 碰到以上3种情况时需要清空Buffer对象，字数需要累加
                if (wordBuffer.isNotEmpty()) {
                    wordBuffer.delete(0, wordBuffer.length)
                    wordCount++
                }
            }
        }
        return wordCount
    }

    // 判断是否是单词拆分符号
    private fun isBreakSpace(c: Char): Boolean {
        mBreakSpaceRanges.forEach {
            if (it.contains(c.toInt())) {
                return true
            }
        }
        return false
    }

    // 判断是否是全角字符、全形符号
    private fun isFullWidthChar(c: Char): Boolean {
        mFullWidthCharRanges.forEach {
            if (it.contains(c.toInt())) {
                return true
            }
        }
        return false
    }

    // 读取配置文件，生成以ValueRange（min-max）为结构的配置列表对象
    private fun readConfigFile(filePath: String): List<IntRange> {
        val intRanges: MutableList<IntRange> = LinkedList()
        try {
            val br = BufferedReader(
                InputStreamReader(context.assets.open(filePath), "UTF-8")
            )
            var s: String?
            while (br.readLine().also { s = it } != null) {
                s?.let {
                    if (it.isNotEmpty() && !it.startsWith("#")) {
                        val min: Int
                        val max: Int
                        if (it.contains("-")) {
                            val values = it.split("-".toRegex()).toTypedArray()
                            min = values[0].substring(2).toInt(16)
                            max = values[1].substring(2).toInt(16)
                        } else {
                            min = it.substring(2).toInt(16)
                            max = min
                        }
                        intRanges.add(min..max)
                    }
                }
            }
            br.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return intRanges
    }
}