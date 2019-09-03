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

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 游戏窗口
 */
class GameActivity : BaseFullActivity<ActivityGameBinding, ViewModel>(R.layout.activity_game, null) {

    private var url: String? = null
    private var type: Int? = null

    override fun bindData(savedInstanceState: Bundle?) {
        backExitEnable(true)
        url = intent.getStringExtra(Constants.URL)
        type = intent.getIntExtra(Constants.V_TYPE, 1)
    }

    override fun bindView() {
        binding.web.addWebStatusListenerAdapter(object: WebViewEx.WebStatusListenerAdapter() {
            override fun callProgress(progress: Int) {
                LogUtil.i("progress:$progress")
                if (progress > 90) {
                    binding.web.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                }
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
        binding.web.exResume()
    }

    override fun onPause() {
        super.onPause()
        binding.web.exPause()
    }

    override fun onDestroy() {
        binding.web.exDestroy()
        super.onDestroy()
    }

}
