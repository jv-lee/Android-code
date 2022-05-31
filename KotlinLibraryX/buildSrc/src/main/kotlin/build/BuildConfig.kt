package build

/**
 * 项目基础配置信息
 * @author jv.lee
 * @date 2021/10/1
 */
object BuildConfig {
    const val applicationId = "com.core.lee"

    const val compileSdk = 31
    const val minSdk = 19
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "1.0.0"

    const val multiDex = true

    const val SUPPORT_LIBRARY_VECTOR_DRAWABLES = true

    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"

    val TEST_INSTRUMENTATION_RUNNER_ARGUMENTS = mapOf(
        "leakcanary.FailTestOnLeakRunListener" to "listener"
    )
}



