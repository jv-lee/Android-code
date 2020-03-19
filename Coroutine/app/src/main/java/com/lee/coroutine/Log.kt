package com.lee.coroutine

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author jv.lee
 * @date 2020/3/19
 * @description
 */
val dateFormat = SimpleDateFormat("HH:mm:ss:SSS")

val now = {
    dateFormat.format(Date(System.currentTimeMillis()))
}

fun log(msg: Any?) = println("${now()} [${Thread.currentThread().name}] $msg")