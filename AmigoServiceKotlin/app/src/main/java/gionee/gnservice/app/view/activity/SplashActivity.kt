package gionee.gnservice.app.view.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import com.gionee.gnservice.statistics.StatisticsUtil
import com.google.gson.Gson
import com.lee.library.base.BaseActivity
import com.lee.library.permission.PermissionManager
import com.lee.library.permission.PermissionRequest
import com.lee.library.utils.LogUtil
import com.mobgi.MobgiAds
import com.qq.e.ads.cfg.GDTAD.initSDK
import com.s.main.sdk.SplashView
import com.s.main.sdk.SplashViewCallBack
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.ActivitySplashBinding
import gionee.gnservice.app.model.entity.Data
import gionee.gnservice.app.model.server.RetrofitUtils
import gionee.gnservice.app.vm.SplashViewModel

class SplashActivity :
    BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash, SplashViewModel::class.java),
    PermissionRequest {

    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.READ_CALENDAR,
        android.Manifest.permission.WRITE_CALENDAR,
        android.Manifest.permission.READ_PHONE_STATE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var isLogin: Boolean = false
    private var isSplash: Boolean = false
    private var isOldAd: Boolean = false

    private var splashView: SplashView? = null

    override fun bindData(savedInstanceState: Bundle?) {
        //获取权限信息 初始化sdk
        PermissionManager.getInstance()
            .attach(this)
            .request(*PERMISSIONS)
            .listener(this)
    }

    override fun bindView() {
    }

    override fun onPermissionSuccess() {
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
        splashView = SplashView(this)
        splashView?.setAdBound(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
        splashView?.setAdJumpView(null, 5)
        splashView?.setAdIds(BuildConfig.screenId, BuildConfig.screenAppId)
        splashView?.setAdLoadCallBack(object : SplashViewCallBack {
            override fun onAdJump(p0: String?) {
                isSplash = true
                sendMain()
            }

            override fun onAdFailed(p0: String?, p1: String?) {
                isSplash = true
                sendMain()
            }

            override fun onAdTimeOut(p0: String?) {
                isSplash = true
                sendMain()
            }

            override fun onAdDismissed(p0: String?) {
                isSplash = true
                sendMain()
            }

            override fun onAdPresent(p0: String?) {
            }

            override fun onAdClick(p0: String?) {
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
            isLogin = true
            sendMain()
        })
    }

    /**
     * 初始化乐逗广告
     */
    private fun initSDK() {
        MobgiAds.init(applicationContext, BuildConfig.mobgiAppId, object : MobgiAds.InitCallback {
            override fun onSuccess() {
                isOldAd = true
                sendMain()
            }

            override fun onError(throwable: Throwable) {
                isOldAd = true
                sendMain()
            }
        })
    }

    /**
     * 启动主页面
     */
    private fun sendMain() {
        if (!isLogin && !isSplash && !isOldAd) {
            return
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(getIntent().extras)
        intent.data = getIntent().data
        startActivity(intent)
        finish()
    }

}
