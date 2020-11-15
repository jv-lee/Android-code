package com.lee.calendar

import org.junit.Test

/**
 * @author jv.lee
 * @date 2020/11/14
 * @description
 */
class IndexTest {

    @Test
    fun test(){
        val rowCount = 6
        val colCount = 7
        val rowIndex = 1
        val colIndex = 6

        println(codeIndex(rowIndex,colIndex))

        val text = "11"
        println(text.startsWith("1"))
    }


    private fun codeIndex(rowIndex:Int,colIndex:Int):Int{
        val a = rowIndex * 7
        val b = colIndex
        return (a+b) - 1
    }

}