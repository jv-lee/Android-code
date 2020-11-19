package com.lee.calendar

import org.junit.Test

/**
 * @author jv.lee
 * @date 2020/11/19
 * @description
 */
class PageTest {

    @Test
    fun test(){
        val pageSize = 20
        val total = 6
        println(totalPage(pageSize,total))
    }

    fun totalPage(pageSize: Int, total: Int): Int {
        val page1 = total / pageSize
        val page2 = if (total != 0 && total < pageSize) 1 else if ((total % pageSize) != 0) 1 else 0
        return page1 + page2
    }
}