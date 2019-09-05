package gionee.gnservice.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.v4.app.FragmentActivity
import com.android.droi.books.BooksInit
import com.cmcm.cmgame.CmGameSdk
import com.cmcm.cmgame.gamedata.CmGameAppInfo
import com.dl.infostream.InfoStreamManager
import com.gionee.gnservice.AmigoServiceApp
import com.lee.library.utils.DensityUtil
import com.lee.library.utils.SPUtil
import com.s.main.sdk.SDK
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.Constants.Companion.READING_ACTIVITY_NAME
import gionee.gnservice.app.service.LocalService
import gionee.gnservice.app.service.RemoteService
import gionee.gnservice.app.tool.CommonTool
import gionee.gnservice.app.tool.CommonTool.Companion.event
import gionee.gnservice.app.tool.CommonTool.Companion.minute
import gionee.gnservice.app.tool.GameImageLoader
import gionee.gnservice.app.tool.GlideTool
import gionee.gnservice.app.tool.ReflectTool
import gionee.gnservice.app.view.activity.MainActivity
import gionee.gnservice.app.view.activity.SplashRunActivity
import java.util.concurrent.ThreadPoolExecutor

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description
 */
class App : AmigoServiceApp() {

    private val TAG: String = App::class.java.simpleName

    private var mainActivity: FragmentActivity? = null

    /**
     * 热启动标识符
     */
    private var isRun: Boolean = false
    /**
     * 退出到后台的起始时间
     */
    private var pauseTime: Long = 0

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
        //在主进程中初始化
        if (applicationContext.packageName == currentProcessName) {
            initLocalService()
            injectCallback()
            initComponent()
            Thread(Runnable { initSDK() }).start()
        }
    }

    /**
     * app工具组件初始化
     */
    private fun initComponent() {
        SPUtil.getInstance(this)
        GlideTool.getInstance(this)
    }

    /**
     * 第三方SDK初始化
     */
    private fun initSDK() {
        //资讯初始化
        InfoStreamManager.getInstance().init(this, BuildConfig.newsId, BuildConfig.newsKey, false)

        //小说初始化
        BooksInit.getInstance().setUserInfo(this, "3")

        //游戏初始化
        initCmGameSdk()

        //百策广告初始化
        SDK.init(this, BuildConfig.screenChannel)
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

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun injectCallback() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (activity?.localClassName!!.contains(Constants.ACTIVITY_PACKAGE_PATH)) {
                    DensityUtil.setDensity(this@App, activity)
                }
                if (activity.localClassName == "gionee.gnservice.app.view.activity.MainActivity") {
                    mainActivity = activity as FragmentActivity?
                }
            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {
                if (isRun) {
                    isRun = false
                    val time = System.currentTimeMillis() - pauseTime
                    if (time > 1000) event(time)
                    if (time > minute(5)) startActivity(
                        Intent(this@App, SplashRunActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }

                //阅读小说计时开始
                if (READING_ACTIVITY_NAME == activity?.localClassName) {
                    ReflectTool.timer(mainActivity!!, "resumeTimer")
                }
            }

            override fun onActivityPaused(activity: Activity?) {
                isRun = true
                pauseTime = System.currentTimeMillis()

                //阅读小说计时结束
                if (READING_ACTIVITY_NAME == activity?.localClassName) {
                    ReflectTool.timer(mainActivity!!, "pauseTimer")
                }
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                if (activity is MainActivity) {
                    isRun = false
                }
            }
        })
    }

    /**
     * 启动守护服务 开始任务
     */
    private fun initLocalService() {
        if (!CommonTool.thisServiceHasRun(this, LocalService::class.java.simpleName)) {
            startService(Intent(this, LocalService::class.java))
            startService(Intent(this, RemoteService::class.java))
        }
    }

}