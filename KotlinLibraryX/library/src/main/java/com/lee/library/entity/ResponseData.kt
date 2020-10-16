package com.lee.library.entity

/**
 * @author jv.lee
 * @date 2020/10/13
 * @description
 */
data class ResponseData<T>(val code: Int, val msg: String, val data: T)