package com.lee.library.mvvm

/**
 * @author jv.lee
 * @date 2020/3/26
 * @description
 */
class CustomException(
    var code: Int,
    var exception: Throwable
) : Throwable(exception.message)