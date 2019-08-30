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
        const val BASE_ACT: String = "act"

        //登陆接口
        const val ACT_LOGIN: String = "login.sign"
        //获取全局配置
        const val ACT_CONFIG: String = "diyConfig.getAllDiyConfig"
        //获取推送奖励
        const val ACT_PUSH_AWARD: String = "activity.getPushAward"
        //获取红点通知数
        const val ACT_RED_POINT: String = "red.getAllRedList"
        //获取磁铁信息
        const val ACT_MAGNET: String = "banner.getMagnet"
        //获取视频分类
        const val ACT_VIDEO_CATEGORY: String = "video.init"
        //获取视频列表
        const val ACT_VIDEO_LIST: String = "video.getVideoListByCid"

    }
}