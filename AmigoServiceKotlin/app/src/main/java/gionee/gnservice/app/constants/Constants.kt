package gionee.gnservice.app.constants

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 项目通用常量
 */
class Constants {

    companion object {
        /**
         * activity包路径
         */
        const val ACTIVITY_PACKAGE_PATH = "gionee.gnservice.app.view.activity"
        /**
         * session_key标识符
         */
        const val SESSION_KEY = "session_key"

        /**
         * 用户数据标识符
         */
        const val USER_KEY = "user_key"

        /**
         * 是否是子入口
         */
        const val IS_SUB = "is_sub"

        /**
         * 通知栏标识符
         */
        const val NOTIFICATION_LABLE = "notification"

        const val TITLE = "title"

        const val URL = "url"

        const val V_TYPE = "v_type"


        /**
         * 磁铁跳转页面
         */
        const val MAGNET_TYPE_TAB = 1
        const val MAGNET_TYPE_GAME = 2
        const val MAGNET_TYPE_ACTIVE = 3
        const val MAGNET_TYPE_SDK_H5 = 4

        const val MAGNET_NEWS = 1
        const val MAGNET_NOVEL = 2
        const val MAGNET_WALLET = 3
        const val MAGNET_WECHAT = 6
        const val MAGNET_GAME = 7
        const val MAGNET_VIDEO = 17
        const val MAGNET_WALLET_TASK = 19
    }


}