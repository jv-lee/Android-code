package com.lee.library.net.client

import okhttp3.*
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * @author jv.lee
 * @date 2020/3/20
 * @description
 */
class OkHttpClientBuilder {

    private val DEFAULT_TIMEOUT = 10L

    private var safeClient: OkHttpClient? = null
    private var unSafeClient: OkHttpClient? = null

    fun getSafeClient(): OkHttpClient {
        if (safeClient == null) {
            safeClient = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketFactoryCompat())
                .build()

        }
        return safeClient!!
    }

    /**
     * 忽略所有证书的 OKHttpClient
     * @return
     */
    fun getUnSafeClient(): OkHttpClient? {
        if (unSafeClient == null) {
            val builder: OkHttpClient.Builder =
                getSafeClient().newBuilder()
                    .protocols(listOf(Protocol.HTTP_1_1))
                    .addInterceptor(RetryInterceptor(3))
                    .connectionPool(
                        ConnectionPool(
                            5,
                            30,
                            TimeUnit.SECONDS
                        )
                    ) // 短时间内有大量请求时（下载整本小说），在华为7.0手机上会抛oom异常，加快清理空闲连接的频率
                    .hostnameVerifier(getUnsafeHostNameVerifier())
            val unsafeSocketFactory = getUnsafeSocketFactory()
            if (unsafeSocketFactory != null) {
                builder.sslSocketFactory(unsafeSocketFactory)
            }
            unSafeClient = builder.build()
        }
        return unSafeClient
    }

    private fun getUnsafeHostNameVerifier(): HostnameVerifier {
        return HostnameVerifier { _, _ -> true }
    }

    private fun getUnsafeSocketFactory(): SSLSocketFactory? {
        val trustAllCerts =
            arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }
            })
        try {
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, SecureRandom())
            return sc.socketFactory
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 重试拦截器
     */
    internal class RetryInterceptor(
        var maxRetry: Int
    ) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            var response = chain.proceed(request)
            var retryNum = 0
            while (!response.isSuccessful && retryNum < maxRetry) {
                retryNum++
                response = chain.proceed(request)
            }
            return response
        }

    }

}