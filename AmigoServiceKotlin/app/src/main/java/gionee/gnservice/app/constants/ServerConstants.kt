package gionee.gnservice.app.constants

import gionee.gnservice.app.BuildConfig

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description
 */
class ServerConstants {

    companion object {
        const val BASE_URL = BuildConfig.BASE_URI
        const val DEFAULT_TIMEOUT: Long = 5
        const val READ_TIMEOUT: Long = 5
        const val WRITE_TIMEOUT: Long = 5

        const val BASE_PATH: String = "/app/api.php"
        const val BASE_ACT :String = "act"

        const val ACT_LOGIN: String = "login.sign"
    }
}