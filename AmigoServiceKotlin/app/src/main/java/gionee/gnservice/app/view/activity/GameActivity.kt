package gionee.gnservice.app.view.activity

import android.arch.lifecycle.ViewModel
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.JavascriptInterface
import com.lee.library.base.BaseFullActivity
import com.lee.library.utils.LogUtil
import com.lee.library.widget.WebViewEx
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.databinding.ActivityGameBinding
import gionee.gnservice.app.view.native.JSInterface


class GameActivity : BaseFullActivity<ActivityGameBinding, ViewModel>(R.layout.activity_game, null) {

    private var isOnPause: Boolean = false
    private var url: String? = null
    private var type: Int? = null

    override fun bindData(savedInstanceState: Bundle?) {
        backExitEnable(true)
        url = intent.getStringExtra(Constants.URL)
        type = intent.getIntExtra(Constants.V_TYPE, 1)
    }

    override fun bindView() {
        binding.web.setWebStatusCallBack(object : WebViewEx.WebStatusCallBack {
            override fun callStart() {
            }

            override fun callSuccess() {
            }

            override fun callFailed() {
            }

            override fun callProgress(progress: Int) {
                LogUtil.i("progress:$progress")
                if (progress > 90) {
                    binding.web.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                }
            }

            override fun callScroll() {
            }
        })
        binding.web.addJavascriptInterface(JSInterface(applicationContext), "client")
        binding.web.addJavascriptInterface(this, "ctx")
        binding.web.visibility = View.INVISIBLE
        binding.web.loadUrl(url)
        LogUtil.i("loadUrl:$url")
    }

    @JavascriptInterface
    fun showVideoAd(adId: String, methodName: String) {
        runOnUiThread {
            //            AManager.getInstance().showVideoAD(this, adId, { isSuccess ->
//            binding.web.loadUrl("javascript:nativeCallback('$methodName',$isSuccess)")
//            })
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.web.canGoBack()) {
                binding.web.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        if (type == 1) {
            if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        } else {
            if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
        super.onResume()
        if (isOnPause) {
            binding.web.onResume()
        }
        isOnPause = false
    }

    override fun onPause() {
        super.onPause()
        binding.web.onPause()
        isOnPause = true
    }

    override fun onDestroy() {
        binding.web.clearCache(true)
        binding.web.clearHistory()
        binding.web.visibility = View.GONE
        binding.web.removeAllViews()
        binding.web.destroy()
        isOnPause = false
        super.onDestroy()
    }

}
