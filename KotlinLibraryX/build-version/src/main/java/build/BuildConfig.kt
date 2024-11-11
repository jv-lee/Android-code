package build

/**
 * 项目基础配置信息
 * @author jv.lee
 * @date 2021/10/1
 */
object BuildConfig {
    const val APPLICATION_ID = "com.core.lee"

    const val COMPILE_SDK = 35
    const val MIN_SDK = 21
    const val TARGET_SDK = 35
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0.0"

    const val COMPOSE_KOTLIN_COMPILER = "1.5.15"
    const val MULTI_DEX_ENABLE = true
    const val SUPPORT_LIBRARY_VECTOR_DRAWABLES = true
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    val TEST_INSTRUMENTATION_RUNNER_ARGUMENTS = mapOf(
        "leakcanary.FailTestOnLeakRunListener" to "listener"
    )
}



