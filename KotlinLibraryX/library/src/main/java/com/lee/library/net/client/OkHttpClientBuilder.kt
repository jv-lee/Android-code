package com.lee.library.net.client

import com.lee.library.net.interceptor.RetryInterceptor
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

/**
 * OkHttpClient构建器
 * @author jv.lee
 * @date 2020/3/20
 */
class OkHttpClientBuilder {

    companion object {
        private const val DEFAULT_TIMEOUT = 10L
    }

    private var safeClient: OkHttpClient? = null
    private var unSafeClient: OkHttpClient? = null

    fun getSafeClient(): OkHttpClient {
        return safeClient ?: kotlin.run {
            val builder = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)

            runSafeSocketFactory { sslSocketFactory, x509TrustManager ->
                builder.sslSocketFactory(sslSocketFactory, x509TrustManager)
            }
            builder.build()
        }.also { safeClient = it }
    }

    /**
     * 忽略所有证书的 OKHttpClient
     * @return
     */
    fun getUnSafeClient(): OkHttpClient {
        return unSafeClient ?: kotlin.run {
            val builder: OkHttpClient.Builder =
                getSafeClient().newBuilder()
                    .protocols(listOf(Protocol.HTTP_1_1))
                    .addInterceptor(RetryInterceptor(3))
                    .connectionPool(ConnectionPool(5, 30, TimeUnit.SECONDS))
                    .hostnameVerifier(getUnsafeHostNameVerifier())

            runUnsafeSocketFactory { sslSocketFactory, x509TrustManager ->
                builder.sslSocketFactory(sslSocketFactory, x509TrustManager)
            }
            builder.build()
        }.also { unSafeClient = it }
    }

    private fun getUnsafeHostNameVerifier(): HostnameVerifier {
        return HostnameVerifier { _, _ -> true }
    }
}