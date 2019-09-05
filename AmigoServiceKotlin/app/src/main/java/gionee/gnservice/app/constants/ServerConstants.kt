package gionee.gnservice.app.constants

import gionee.gnservice.app.BuildConfig

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 服务器常量
 */
class ServerConstants {

    companion object {
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
        //获取更新信息
        const val ACT_VERSION_INFO: String = "diyConfig.getVersionContent"
        //主界面tab排序
        const val ACT_TAB_INDEX: String = "login.initConf"
        //获取红包雨奖励
        const val ACT_RED_PACKAGE_AWARD: String = "activity.getRedPacketAward"
        //新手任务奖励
        const val ACT_NOVICE_AWARD: String = "times.getInit"
        //时长上报接口
        const val ACT_ADD_TIME: String = "times.subAddTime"
        //获取视频分类
        const val ACT_VIDEO_CATEGORY: String = "video.init"
        //获取视频列表
        const val ACT_VIDEO_LIST: String = "video.getVideoListByCid"

    }
}