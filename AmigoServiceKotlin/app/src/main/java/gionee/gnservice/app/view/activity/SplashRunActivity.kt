package gionee.gnservice.app.view.activity

import android.os.Bundle
import com.gionee.gnservice.statistics.StatisticsUtil
import com.lee.library.base.BaseActivity
import com.s.main.sdk.SplashView
import com.s.main.sdk.SplashViewCallBack
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.StatisticsConstants
import gionee.gnservice.app.databinding.ActivitySplashBinding
import gionee.gnservice.app.vm.SplashViewModel

/**
 * 开屏热启动页面
 */
class SplashRunActivity :
    BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash, SplashViewModel::class.java) {

    private var splashView: SplashView? = null

    override fun bindData(savedInstanceState: Bundle?) {
        initSplash()
    }

    override fun bindView() {
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
        StatisticsUtil.onEvent(this, StatisticsConstants.Splash_Fetch_Times, "热启动")
        splashView = SplashView(this)
        splashView?.setAdBound(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
        splashView?.setAdJumpView(null, 5)
        splashView?.setAdIds(BuildConfig.screenId, BuildConfig.screenAppId)
        splashView?.setAdLoadCallBack(object : SplashViewCallBack {
            override fun onAdJump(p0: String?) {
                finishAnimator()
            }

            override fun onAdFailed(p0: String?, p1: String?) {
                finishAnimator()
            }

            override fun onAdTimeOut(p0: String?) {
                StatisticsUtil.onEvent(this@SplashRunActivity, StatisticsConstants.Splash_Result, "热启动_拉取失败")
                finishAnimator()
            }

            override fun onAdDismissed(p0: String?) {
                finishAnimator()
            }

            override fun onAdPresent(p0: String?) {
                StatisticsUtil.onEvent(this@SplashRunActivity, StatisticsConstants.Splash_Result, "热启动_成功曝光")
            }

            override fun onAdClick(p0: String?) {
                StatisticsUtil.onEvent(this@SplashRunActivity, StatisticsConstants.Splash_Result, "热启动_广告点击")
            }
        })
        binding.frameSplashContainer.addView(splashView)
    }

    /**
     * 关闭当前页面
     */
    private fun finishAnimator() {
        finish()
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
    }

}
