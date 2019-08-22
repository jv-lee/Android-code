package gionee.gnservice

import android.content.Context
import android.support.multidex.MultiDex
import com.android.droi.books.BooksInit
import com.cmcm.cmgame.CmGameSdk
import com.cmcm.cmgame.gamedata.CmGameAppInfo
import com.dl.infostream.InfoStreamManager
import com.gionee.gnservice.AmigoServiceApp
import com.s.main.sdk.SDK
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.utils.GameImageLoader

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description
 */
class App : AmigoServiceApp() {

    private val TAG: String = App::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        //在主进程中初始化
        if (applicationContext.packageName == currentProcessName) {
            initSDK()
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initSDK() {
        //资讯初始化
        InfoStreamManager.getInstance().init(this, BuildConfig.NEWS_ID, BuildConfig.NEWS_KEY, false)

        //小说初始化
        BooksInit.getInstance().setUserInfo(this, "3")

        //游戏初始化
        initCmGameSdk()

        //百策广告初始化
        SDK.init(this, BuildConfig.SCREEN_CHANNEL)
    }

    /**
     * 游戏SDK初始化
     */
    private fun initCmGameSdk() {
        // 注意：如果有多个进程，请务必确保这个初始化逻辑只会在一个进程里运行
        val cmGameAppInfo = CmGameAppInfo()
        // GameSdkID，向我方申请
        cmGameAppInfo.appId = "jinliyonghuzhongxin"
        // 游戏host地址，向我方申请
        cmGameAppInfo.appHost = "https://jlyhzx-xyx-sdk-svc.beike.cn"

        // 游戏退出时，增加游戏推荐弹窗，提高游戏的点击个数，注释此行可去掉此功能
        cmGameAppInfo.quitGameConfirmFlag = true

        // 可设置进入游戏默认静音，默认有声，设置true为游戏静音
        // cmGameAppInfo.setMute(true);

        // Sigmob广告参数设置，demo中的是测试参数，正式接入时需要替换为申请的正式参数
        val sigmobInfo = CmGameAppInfo.SigmobInfo()
        // Sigmob广告APPId
        sigmobInfo.appId = "1665"
        // Sigmob广告APPKey
        sigmobInfo.appKey = "4f63fb277fde0eb2"
        // Sigmob广告激励视频位置Id，即placementId
        sigmobInfo.rewardVideoId = "e3c019a6966"
        cmGameAppInfo.sigmobInfo = sigmobInfo

        CmGameSdk.initCmGameSdk(this, cmGameAppInfo, GameImageLoader(), BuildConfig.DEBUG)
    }

}