package gionee.gnservice.app.constants

/**
 * @author jv.lee
 * @date 2019/8/26.
 * @description 统计常量
 */
class StatisticsConstants {
    companion object {
        //桌面点击
        const val Desktop_Start: String = "Desktop_Start"

        //乐逗广告
        const val Ads_Init_Times: String = "Ads_Init_Times"
        const val Ads_Init_Result: String = "Ads_Init_Result"
        const val Ads_Init_Success: String = "成功"
        const val Ads_Init_Error: String = "失败"

        //开屏广告
        const val Splash_Fetch_Times: String = "Splash_Fetch_Times"
        const val Splash_Result: String = "Splash_Result"

        //用户中心
        const val Exposure_UCenterPage: String = "Exposure_UCenterPage"

        //冷启动 来源
        const val Cold_Start: String = "Cold_Start"

        //通知栏 统计
        const val Client_Notification: String = "Client_Notification"
    }
}