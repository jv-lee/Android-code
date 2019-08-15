package com.lee.library.model.entity

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
data class UserInfo(
    val images: List<String>,
    val info: Info
)

data class Info(
    val description: String,
    val id: Int,
    val name: String
)