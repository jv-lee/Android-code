package build

/**
 * 项目插件配置信息
 * @author jv.lee
 * @date 2021/10/1
 */
object BuildPlugin {
    const val APPLICATION = "com.android.application"
    const val LIBRARY = "com.android.library"

    const val KOTLIN_ANDROID = "kotlin-android"
    const val KOTLIN_KAPT = "kotlin-kapt"
    const val KOTLIN_PARCELIZE = "kotlin-parcelize"
    const val PROTOBUF = "com.google.protobuf"
    const val HILT = "dagger.hilt.android.plugin"
}