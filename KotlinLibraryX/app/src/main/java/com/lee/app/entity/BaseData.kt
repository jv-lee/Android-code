package com.lee.app.entity

/**
 *
 * @author jv.lee
 * @date 2020/3/20

 */
data class BaseData<T>(val success: String, val data: T)

data class Tab(val id: Int, val icon: String, val title: String)