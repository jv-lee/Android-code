package gionee.gnservice.app.model.server

import com.google.gson.Gson
import com.lee.library.utils.LogUtil
import com.lee.library.utils.MD5Util
import com.lee.library.utils.SPUtil
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.ServerConstants.Companion.BASE_URL
import gionee.gnservice.app.constants.ServerConstants.Companion.DEFAULT_TIMEOUT
import gionee.gnservice.app.constants.ServerConstants.Companion.READ_TIMEOUT
import gionee.gnservice.app.constants.ServerConstants.Companion.WRITE_TIMEOUT
import gionee.gnservice.app.model.entity.Login
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * @author jv.lee
 * @date 2019/8/22.
 * @description 网络请求工具
 */
class RetrofitUtils private constructor() {

    private var apiServer: ApiServer? = null

    init {
        val retrofit = Retrofit.Builder().client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        apiServer = retrofit.create(ApiServer::class.java)
    }

    companion object {
        //双重校验锁单例
        val instance: RetrofitUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitUtils()
        }
    }


    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .sslSocketFactory(getSSLSocketFactory())
            .hostnameVerifier(getHostnameVerifier())
            .addInterceptor(GetParameterInterceptor())
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    /**
     * getSSLSocketFactory、getTrustManagers、getHostnameVerifier
     * 使OkHttpClient支持自签名证书，避免Glide加载不了Https图片
     */
    private fun getSSLSocketFactory(): SSLSocketFactory {
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, getTrustManagers(), SecureRandom())
            return sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    private fun getTrustManagers(): Array<TrustManager> {
        return arrayOf(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
    }

    private fun getHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { hostname, session ->
            // 直接返回true，默认verify通过
            true
        }
    }

    /**
     * 获取api接口
     *
     * @return
     */
    fun getApi(): ApiServer {
        return apiServer!!
    }

    /**
     * 获取当前用户SessionKey
     *
     * @return
     */
    fun getSessionKey(): String {
        return SPUtil.get(Constants.SESSION_KEY, "") as String
    }

    /**
     * 保存SessionKey
     *
     * @param sessionKey
     */
    fun saveSessionKey(sessionKey: String?) {
        LogUtil.e("save SessionKey:$sessionKey")
        SPUtil.save(Constants.SESSION_KEY, sessionKey)
    }

    /**
     * 保存登录信息
     *
     * @param data
     */
    fun saveUser(data: Login?) {
        SPUtil.save(Constants.USER_KEY, Gson().toJson(data))
    }

    /**
     * 获取登陆信息
     *
     * @return
     */
    fun getUser(): Login? {
        val userJson = SPUtil.get(Constants.USER_KEY, "") as String
        return if (userJson != "") {
            Gson().fromJson<Login>(userJson, Login::class.java)
        } else null
    }

    /**
     * 签名
     *
     * @param signParams sessionKey或需要签名参数
     * @param ts         时间戳
     * @return
     */
    fun getSign(signParams: String, ts: String): String? {
        try {
            return MD5Util.getMD5("jl_center$signParams$ts")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getTs(): String {
        return System.currentTimeMillis().toString()
    }


}
