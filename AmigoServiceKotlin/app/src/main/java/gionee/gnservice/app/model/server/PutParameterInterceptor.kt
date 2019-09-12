package gionee.gnservice.app.model.server

import android.util.Log
import com.lee.library.utils.LogUtil
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.constants.ServerConstants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 所有接口必传参数 添加拦截器
 */
class PutParameterInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url()
        if (!url.queryParameter(ServerConstants.BASE_ACT).equals(ServerConstants.ACT_LOGIN)) {
            val ts = RetrofitUtils.instance.getTs()
            val sessionKey = RetrofitUtils.instance.getSessionKey()
            val sign = RetrofitUtils.instance.getSign(sessionKey, ts)
            val newUrl = url.newBuilder()
                .addEncodedQueryParameter("sessionKey", sessionKey)
                .addEncodedQueryParameter("sign", sign)
                .addEncodedQueryParameter("ts", ts)
                .addEncodedQueryParameter("version", BuildConfig.VERSION_NAME)
                .addEncodedQueryParameter("version_code", BuildConfig.VERSION_CODE.toString())
                .build()
            request = request.newBuilder()
                .addHeader("Connection", "close")
                .url(newUrl).build()
            toLog(request)
        }
        //设置拦截返回
        return chain.proceed(request)
    }

    private fun toLog(request: Request) {
        Log.e("PutParameterInterceptor", "request -> ${request.url().host()} ${request.url().encodedPath()} ")
        val params = StringBuilder()
        for (queryParameterName in request.url().queryParameterNames()) {
            params.append(queryParameterName).append(":").append(request.url().queryParameter(queryParameterName))
                .append("\n")
        }
        Log.e("PutParameterInterceptor", "params -> \n$params")
    }
}