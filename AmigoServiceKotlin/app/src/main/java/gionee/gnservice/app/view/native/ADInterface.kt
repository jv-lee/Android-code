package gionee.gnservice.app.view.native

import android.app.Activity
import android.webkit.JavascriptInterface

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description 广告 javascriptInterface接口
 */
class ADInterface {

    private var activity: Activity? = null

    constructor(activity: Activity) {
        this.activity = activity
    }

    @JavascriptInterface
    fun showVideoAd(adId: String, methodName: String) {
        activity?.runOnUiThread {
            //            AManager.getInstance().showVideoAD(this, adId, { isSuccess ->
//            binding.web.loadUrl("javascript:nativeCallback('$methodName',$isSuccess)")
//            })
        }
    }

    @JavascriptInterface
    fun showScreenAd(adID: String) {
        activity?.runOnUiThread {
            //            AManager.getInstance().showScreen(adID, mActivity, { isSuccess ->
//                if (web != null) {
//                    web.loadUrl("javascript:screenCall($isSuccess)")
//                }
//            })
        }
    }


}