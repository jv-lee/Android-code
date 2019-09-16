package gionee.gnservice.app.view.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import com.gionee.gnservice.statistics.StatisticsUtil
import com.lee.library.base.BaseActivity
import com.lee.library.base.BaseFullActivity
import com.lee.library.permission.PermissionManager
import com.lee.library.permission.PermissionRequest
import com.lee.library.utils.LogUtil
import com.mobgi.MobgiAds
import com.s.main.sdk.SplashView
import com.s.main.sdk.SplashViewCallBack
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.Cache
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.StatisticsConstants
import gionee.gnservice.app.databinding.ActivitySplashBinding
import gionee.gnservice.app.model.server.RetrofitUtils
import gionee.gnservice.app.vm.SplashViewModel

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 子入口 开屏广告
 */
class SplashSubActivity :
    BaseFullActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash, SplashViewModel::class.java),
    PermissionRequest {

    private var isLogin: Boolean = false
    private var isSplash: Boolean = false
    private var isOldAd: Boolean = false

    private var splashView: SplashView? = null

    override fun bindData(savedInstanceState: Bundle?) {
        //获取权限信息 初始化sdk
        PermissionManager.getInstance()
            .attach(this)
            .request(
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_CALENDAR,
                android.Manifest.permission.ACCESS_NETWORK_STATE
            )
            .listener(this)
    }

    override fun bindView() {
    }

    override fun onPermissionSuccess() {
        StatisticsUtil.onEvent(this, StatisticsConstants.Desktop_Start)
        initSplash()
        initLogin()
        initSDK()
    }

    override fun onPermissionFiled(permission: String?) {
        finish()
    }

    override fun onResume() {
        super.onResume()
        splashView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        splashView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        splashView?.destroy()
        splashView = null
    }

    /**
     * 初始化开屏广告
     */
    private fun initSplash() {
        StatisticsUtil.onEvent(this, StatisticsConstants.Splash_Fetch_Times, StatisticsConstants.Label_Cold_Start)
        splashView = SplashView(this)
        splashView?.setAdBound(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
        splashView?.setAdJumpView(null, 5)
        splashView?.setAdIds(BuildConfig.screenId, BuildConfig.screenAppId)
        splashView?.setAdLoadCallBack(object : SplashViewCallBack {
            override fun onAdJump(p0: String?) {
                LogUtil.i("splash -> onAdJump:$p0")
                isSplash = true
                sendMain()
            }

            override fun onAdFailed(p0: String?, p1: String?) {
                LogUtil.i("splash -> onAdFailed:$p0----$p1")
                isSplash = true
                sendMain()
            }

            override fun onAdTimeOut(p0: String?) {
                LogUtil.i("splash -> onAdTimeOut:$p0")
                StatisticsUtil.onEvent(this@SplashSubActivity, StatisticsConstants.Splash_Result, StatisticsConstants.Label_Cold_Start_TimeOut)
                isSplash = true
                sendMain()
            }

            override fun onAdDismissed(p0: String?) {
                LogUtil.i("splash -> onAdDismissed:$p0")
                isSplash = true
                sendMain()
            }

            override fun onAdPresent(p0: String?) {
                LogUtil.i("splash -> onAdPresent:$p0")
                StatisticsUtil.onEvent(this@SplashSubActivity, StatisticsConstants.Splash_Result, StatisticsConstants.Label_Cold_Start_Success)
            }

            override fun onAdClick(p0: String?) {
                LogUtil.i("splash -> onAdClick:$p0")
                StatisticsUtil.onEvent(this@SplashSubActivity, StatisticsConstants.Splash_Result, StatisticsConstants.Label_Cold_Start_AdClick)
                isSplash = true
                sendMain()
            }
        })
        binding.frameSplashContainer.addView(splashView)
    }

    /**
     * 初始化登录信息
     */
    private fun initLogin() {
        //获取登录信息
        viewModel.model.login().observe(this, Observer {
            RetrofitUtils.instance.saveSessionKey(it?.sessionKey)
            RetrofitUtils.instance.saveUser(it)
            Cache.saveParameter(it!!)
            isLogin = true
            sendMain()
        })
    }

    /**
     * 初始化乐逗广告
     */
    private fun initSDK() {
        StatisticsUtil.onEvent(this, StatisticsConstants.Ads_Init_Times)
        MobgiAds.init(applicationContext, BuildConfig.mobgiAppId, object : MobgiAds.InitCallback {
            override fun onSuccess() {
                LogUtil.i("changeRepeat mobgi success")
                StatisticsUtil.onEvent(
                    this@SplashSubActivity,
                    StatisticsConstants.Ads_Init_Result,
                    StatisticsConstants.Ads_Init_Success
                )
                isOldAd = true
                sendMain()
            }

            override fun onError(throwable: Throwable) {
                LogUtil.i("changeRepeat mobgi error:$throwable")
                StatisticsUtil.onEvent(
                    this@SplashSubActivity,
                    StatisticsConstants.Ads_Init_Result,
                    StatisticsConstants.Ads_Init_Error
                )
                isOldAd = true
                sendMain()
            }
        })
    }

    /**
     * 启动主页面
     */
    private fun sendMain() {
        if (!isLogin || !isSplash || !isOldAd) {
            return
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.IS_SUB, true)
        if (getIntent().extras != null) {
            intent.putExtras(getIntent()!!.extras!!)
        }
        intent.data = getIntent().data
        startActivity(intent)
        finish()
    }

}
