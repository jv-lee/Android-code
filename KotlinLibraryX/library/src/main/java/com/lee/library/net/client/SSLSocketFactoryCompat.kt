package com.lee.library.net.client

import android.os.Build
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


/**
 * @author jv.lee
 * @date 2020/4/20
 * @description
 */
class SSLSocketFactoryCompat : SSLSocketFactory {
    private val delegate: SSLSocketFactory

    constructor() {
        val sc: SSLContext = SSLContext.getInstance("TLS")
        sc.init(null, null, null)
        delegate = sc.socketFactory
    }

    constructor(delegate: SSLSocketFactory?) {
        if (delegate == null) {
            throw NullPointerException()
        }
        this.delegate = delegate
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return delegate.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.supportedCipherSuites
    }

    private fun enableTls12(socket: Socket): Socket {
        if (Build.VERSION.SDK_INT == 19) {
            if (socket is SSLSocket) {
                socket.enabledProtocols = TLS_V12_ONLY
            }
        }
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(
        s: Socket?,
        host: String?,
        port: Int,
        autoClose: Boolean
    ): Socket {
        return enableTls12(delegate.createSocket(s, host, port, autoClose))
    }

    @Throws(IOException::class)
    override fun createSocket(host: String?, port: Int): Socket {
        return enableTls12(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        host: String?,
        port: Int,
        localHost: InetAddress?,
        localPort: Int
    ): Socket {
        return enableTls12(delegate.createSocket(host, port, localHost, localPort))
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress?, port: Int): Socket {
        return enableTls12(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress?,
        port: Int,
        localAddress: InetAddress?,
        localPort: Int
    ): Socket {
        return enableTls12(delegate.createSocket(address, port, localAddress, localPort))
    }

    companion object {
        private val TLS_V12_ONLY = arrayOf("TLSv1.2")
    }
}