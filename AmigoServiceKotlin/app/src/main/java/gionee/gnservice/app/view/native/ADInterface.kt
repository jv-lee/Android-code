package gionee.gnservice.app.view.native

import android.app.Activity
import android.webkit.JavascriptInterface
import com.lee.library.widget.WebViewEx
import gionee.gnservice.app.tool.ACallback
import gionee.gnservice.app.tool.AManager

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description 广告 javascriptInterface接口
 */
class ADInterface(activity: Activity, web: WebViewEx) {

    private var activity: Activity? = activity
    private var web: WebViewEx? = web

    companion object {
        const val NAME = "ad"
    }

    @JavascriptInterface
    fun showVideoAd(adId: String, methodName: String) {
        activity?.runOnUiThread {
            AManager.getInstance()
                .showVideoAD(activity, adId)
                { isSuccess -> web?.loadUrl("javascript:$methodName($isSuccess)") }
        }
    }

    @JavascriptInterface
    fun showScreenAd(adID: String) {
        activity?.runOnUiThread {
            AManager.getInstance()
                .showScreen(adID, activity!!)
                { isSuccess -> web?.loadUrl("javascript:screenCall($isSuccess)") }
        }
    }


}