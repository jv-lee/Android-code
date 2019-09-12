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
        const val Label_Cold_Start: String = "冷启动"
        const val Label_Cold_Start_TimeOut: String = "冷启动_拉取超时"
        const val Label_Cold_Start_Success: String = "冷启动_成功曝光"
        const val Label_Cold_Start_AdClick: String = "冷启动_广告点击"
        const val Label_Run_Start: String = "热启动"
        const val Label_Run_Start_TimeOut: String = "热启动_拉取超时"
        const val Label_Run_Start_Success: String = "热启动_成功曝光"
        const val Label_Run_Start_AdClick: String = "热启动_广告点击"

        const val Splash_Result: String = "Splash_Result"

        //冷启动 来源
        const val Cold_Start: String = "Cold_Start"

        //通知栏 统计
        const val Client_Notification: String = "Client_Notification"

        //用户中心
        const val Exposure_UCenterPage: String = "Exposure_UCenterPage"
        //tab点击
        const val USER_TAGSCLICK: String = "User_TagsClick"
        const val Label_TAB_NEWS: String = "资讯"
        const val Label_TAB_VIDEO: String = "视频"
        const val Label_TAB_NOVEL: String = "小说"
        const val Label_TAB_GAME: String = "游戏"
        const val Label_TAB_WALLET: String = "钱包"

        const val MAGNET_CLICK: String = "Magnet_Click"

        //悬浮窗
        const val Float_Icon_Permission: String = "Float_Icon_Permission"
        const val Label_Permission_Success: String = "已授权"
        const val Label_Permission_Failed: String = "未授权"
        const val Float_Icon_Click: String = "Float_Icon_Click"

        //资讯
        const val News_Details_Click: String = "News_Details_Click"
        const val News_Slide: String = "News_Slide"

        //小说
        val BOOKS_GLIDE = "Books_glide"//小说界面滑动
        val BOOKS_CLICK = "Books_Click"//小说点击打开上报
        val BOOKS_CLICK_LABLE = "打开小说"//小说点击打开上报Lable

    }
}