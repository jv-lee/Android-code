package gionee.gnservice.app.tool

import com.lee.library.utils.SPUtil
import gionee.gnservice.app.BuildConfig

/**
 * @author jv.lee
 * @date 2019/8/30.
 * @description
 */
class PrefAccess {

    companion object {

        /**
         * 判断当前版本是否是新版本
         */
        fun isOpenUpdate(): Boolean {
            val versionCode = SPUtil.get("versionCode", 0) as Int
            return if (versionCode == BuildConfig.VERSION_CODE) {
                false
            } else {
                SPUtil.save("versionCode", BuildConfig.VERSION_CODE)
                true
            }
        }
    }

}
