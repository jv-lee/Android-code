package gionee.gnservice.app.tool

import android.view.View
import com.lee.library.utils.SPUtil
import gionee.gnservice.app.BuildConfig

/**
 * @author jv.lee
 * @date 2019/8/30.
 * @description
 */
class PrefAccess {

    companion object {

        const val VERSION = "VERSION"
        const val FIRST_OPEN = "FIRST_OPEN"
        const val KEY_GUIDE_FINISHED_NOVEL = "KEY_GUIDE_FINISHED_NOVEL"
        const val KEY_GUIDE_FINISHED_NEWS = "KEY_GUIDE_FINISHED_NEWS"
        const val KEY_GUIDE_FINISHED_VIDEO = "KEY_GUIDE_FINISHED_VIDEO"

        /**
         * 判断当前版本是否是新版本
         */
        fun isOpenUpdate(): Boolean {
            val versionCode = SPUtil.get(VERSION, 0) as Int
            return if (versionCode == BuildConfig.VERSION_CODE) {
                false
            } else {
                SPUtil.save(VERSION, BuildConfig.VERSION_CODE)
                true
            }
        }

        /**
         * 判断首次打开
         */
        fun isFirstOpen(): Boolean {
            val isFirstOpen = SPUtil.get(FIRST_OPEN, true) as Boolean
            return if (isFirstOpen) {
                SPUtil.save(FIRST_OPEN, false)
                true
            } else {
                false
            }
        }

        /**
         * 设置引导图
         */
        fun isGuide(view: View, key: String) {
            if (SPUtil.get(key, true) as Boolean) {
                SPUtil.save(key, false)
                view.visibility = View.VISIBLE
                view.setOnClickListener { view.visibility = View.GONE }
            }
        }
    }

}
