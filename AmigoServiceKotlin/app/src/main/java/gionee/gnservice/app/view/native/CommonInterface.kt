package gionee.gnservice.app.view.native

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface
import com.gionee.gnservice.statistics.StatisticsUtil
import com.gionee.gnservice.wxapi.WXLoginListener
import com.gionee.gnservice.wxapi.WeChatUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lee.library.livedatabus.LiveDataBus
import com.lee.library.utils.NetworkUtil
import gionee.gnservice.app.App
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.model.server.RetrofitUtils
import gionee.gnservice.app.network.HttpUtil
import gionee.gnservice.app.tool.CommonTool
import gionee.gnservice.app.view.activity.GameActivity
import gionee.gnservice.app.view.activity.MainActivity
import gionee.gnservice.app.view.activity.WebActivity
import java.util.*

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description 通用组件 javascriptInterface接口
 */
class CommonInterface(activity: Activity) {

    private var activity: Activity? = activity

    companion object {
        const val NAME = "common"
    }

    //通用web页面跳转
    @JavascriptInterface
    fun startActivity(url: String, title: String?) {
        activity?.runOnUiThread {
            activity?.startActivity(
                Intent(activity, WebActivity::class.java)
                    .putExtra(Constants.URL, url)
                    .putExtra(Constants.V_TYPE, 1)
                    .putExtra(Constants.TITLE, title)
            )
        }
    }

    //本地钱包子页面跳转路径  BuildConfig.JS_URI + 页面地址
    @JavascriptInterface
    fun startLocalActivity(url: String, title: String?) {
        activity?.runOnUiThread {
            activity?.startActivity(
                Intent(activity, WebActivity::class.java)
                    .putExtra(Constants.URL, BuildConfig.JS_URI + url)
                    .putExtra(Constants.V_TYPE, 1)
                    .putExtra(Constants.TITLE, title)
            )
        }
    }

    @JavascriptInterface
    fun startGame(url: String, vtype: Int, gid: String) {
        activity?.runOnUiThread {
            activity?.startActivity(
                Intent(activity, GameActivity::class.java)
                    .putExtra(Constants.URL, url)
                    .putExtra(Constants.V_TYPE, vtype)
            )
        }
    }

    @JavascriptInterface
    fun startPage(code: Int, arg: String) {
        LiveDataBus.getInstance().getChannel(EventConstants.START_PAGE).postValue(code)
    }

    @JavascriptInterface
    fun isLogin(): Boolean {
        return App.main.getUserCenterFragment().isLogin
    }

    @JavascriptInterface
    fun goLogin() {
        activity?.runOnUiThread {
            App.main.getUserCenterFragment().login()
        }
    }

    @JavascriptInterface
    fun isConnected(): Boolean {
        return NetworkUtil.isConnected(activity?.applicationContext)
    }

    @JavascriptInterface
    fun checkRedPoint() {
        LiveDataBus.getInstance().getChannel(EventConstants.UPDATE_RED_POINT).postValue(0)
    }

    @JavascriptInterface
    fun showWeChat(name: String) {
        activity?.runOnUiThread {
            CommonTool.copy(activity!!, name)
            CommonTool.getWeChatApi(activity!!)
        }
    }

    @JavascriptInterface
    fun goUserCenter() {
        activity?.runOnUiThread {
            App.main.showUserCenter()
        }
    }

}