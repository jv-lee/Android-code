package com.lee.library.net.client

/*
 * SSLSocket 扩展函数
 * @author jv.lee
 * @date 2020/3/20
 */

import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

fun runSafeSocketFactory(callback: (SSLSocketFactory, X509TrustManager) -> Unit) {
    val trustManagerFactory =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    val key: KeyStore? = null
    trustManagerFactory.init(key)
    val trustManagers = trustManagerFactory.trustManagers
    if (trustManagers.size != 1 || trustManagers.first() !is X509TrustManager) {
        throw IllegalStateException(
            "Unexpected default trust managers:${Arrays.toString(trustManagers)}"
        )
    }
    val trustManager = trustManagers.first() as X509TrustManager

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, arrayOf(trustManager), null)
    val socketFactory = sslContext.socketFactory

    callback(socketFactory, trustManager)
}

fun runUnsafeSocketFactory(callback: (SSLSocketFactory, X509TrustManager) -> Unit) {
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
        callback(sc.socketFactory, trustAllCerts.first() as X509TrustManager)
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: KeyManagementException) {
        e.printStackTrace()
    }
}