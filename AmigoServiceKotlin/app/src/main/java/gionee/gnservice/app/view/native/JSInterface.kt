package gionee.gnservice.app.view.native

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.model.server.RetrofitUtils
import gionee.gnservice.app.network.HttpUtil
import java.util.*

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description 基础组件 javascriptInterface接口
 */
class JSInterface(context: Context) {

    private var context: Context? = context

    companion object {
        const val NAME = "client"
    }

    @JavascriptInterface
    fun onEvent(eventId: String, label: String){

    }

    @JavascriptInterface
    fun getReq(url: String, headers: String): String {
        return getHttp(url, headers).syncGet()
    }

    // 获取客户端信息，返回 uid、nick、gender（性别）、avatar（头像地址）、versionName、versionCode
    // 使用 json 格式返回
    @JavascriptInterface
    fun getInfo(): String {
        val user = RetrofitUtils.instance.getUser() ?: return "{}"

        val map = HashMap<String, Any>()
        map["versionName"] = BuildConfig.VERSION_NAME
        map["versionCode"] = BuildConfig.VERSION_CODE
        map["uid"] = user.userInfo.id
        map["jlUid"] = user.userInfo.jl_uid
        map["nick"] = user.userInfo.nick
        map["gender"] = user.userInfo.gender
        map["avatar"] = user.userInfo.avatar
        return Gson().toJson(map)
    }

    // 获取签名，返回 {"ts": "时间戳，单位秒", "sign": "签名串"}
    @JavascriptInterface
    fun getSign(): String {
        val map: MutableMap<String, String> = HashMap()
        val ts = RetrofitUtils.instance.getTs()
        val sessionKey = RetrofitUtils.instance.getSessionKey()
        map["ts"] = ts
        map["sessionKey"] = sessionKey
        map["version"] = BuildConfig.VERSION_NAME
        map["sign"] = RetrofitUtils.instance.getSign(sessionKey, ts)!!

        return Gson().toJson(map)
    }

    // 通过客户端获取接口域名
    @JavascriptInterface
    fun getBaseUri(): String {
        return BuildConfig.BASE_URI
    }

    private fun getHttp(url: String, headers: String): HttpUtil {
        return getHttpBuilder(url, headers)
            .build()
    }

    private fun getHttpBuilder(url: String, headers: String): HttpUtil.Builder {
        Log.e(this.javaClass.simpleName,"url: $url, headers: $headers")
        val type = object : TypeToken<HashMap<String, String>>() {}.type
        val hashMap = Gson().fromJson<HashMap<String, String>>(headers, type)

        return HttpUtil.Builder(url)
            .setHeaders(hashMap)
    }

}