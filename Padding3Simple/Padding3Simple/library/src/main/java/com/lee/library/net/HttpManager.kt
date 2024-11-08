package com.lee.library.net

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lee.library.base.BaseApplication
import com.lee.library.net.adapter.DoubleDefaultAdapter
import com.lee.library.net.adapter.IntegerDefaultAdapter
import com.lee.library.net.adapter.LongDefaultAdapter
import com.lee.library.net.client.OkHttpClientBuilder
import com.lee.library.net.request.IRequest
import com.lee.library.net.request.Request
import com.lee.library.utils.LogUtil
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.protobuf.ProtoConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File

/**
 * @author jv.lee
 * @date 2020/3/20
 * @description Retrofit库封装
 */
class HttpManager private constructor() {

    companion object {
        @Volatile
        private var instance: HttpManager? = null
        private var gson: Gson? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: HttpManager().also { instance = it }
            }

        fun getGson() = gson ?: GsonBuilder()
            .registerTypeAdapter(Int::class.java, IntegerDefaultAdapter())
            .registerTypeAdapter(Double::class.java, DoubleDefaultAdapter())
            .registerTypeAdapter(Long::class.java, LongDefaultAdapter())
            .create().also { gson = it }

    }

    private val mServiceMap by lazy { HashMap<String, Any>() }
    private val mInterceptors by lazy { ArrayList<Interceptor>() }
    private var mClient: OkHttpClient? = null
    private var mDownloadClient: OkHttpClient? = null

    fun putInterceptor(interceptor: Interceptor) {
        mInterceptors.add(interceptor)
    }

    fun getClient(): OkHttpClient? {
        return getOkHttpClient(Request("https://android.cn", IRequest.ConverterType.JSON))
    }

    fun <T> getService(serviceClass: Class<T>, request: Request): T {
        if (request.isDownload) {
            return createService(serviceClass, request)
        }
        return if (mServiceMap.containsKey(serviceClass.name + request.key)) {
            mServiceMap[serviceClass.name + request.key] as T
        } else {
            val service = createService(serviceClass, request)
            mServiceMap.put(serviceClass.name + request.key, service!!)
            service
        }
    }

    fun <T> getService(serviceClass: Class<T>, request: Request, client: OkHttpClient): T {
        if (request.isDownload) {
            return createService(serviceClass, request, client)
        }
        return if (mServiceMap.containsKey(serviceClass.name)) {
            mServiceMap[serviceClass.name + request.key] as T
        } else {
            val service = createService(serviceClass, request, client)
            mServiceMap.put(serviceClass.name + request.key, service!!)
            service
        }
    }

    private fun <T> createService(serviceClass: Class<T>, request: Request): T {
        return createService(serviceClass, request, getOkHttpClient(request))
    }

    private fun <T> createService(
        serviceClass: Class<T>,
        request: Request,
        client: OkHttpClient
    ): T {
        val builder = Retrofit.Builder()
            .baseUrl(request.baseUrl)
            .addConverterFactory(getConverterFactory(request.converterType))

        val callAdapter = getCallAdapter(request.callType)
        if (callAdapter != null) {
            builder.addCallAdapterFactory(callAdapter)
        }

        val retrofit = builder.client(client).build()
        return retrofit.create(serviceClass)
    }

    @Synchronized
    private fun getOkHttpClient(request: Request): OkHttpClient {
        if (request.isDownload && mDownloadClient != null) {
            return mDownloadClient as OkHttpClient
        }
        if (!request.isDownload && mClient != null) {
            return mClient as OkHttpClient
        }

        val builder = OkHttpClientBuilder().getSafeClient().newBuilder()

        //cache
        val httpCacheDirectory = File(BaseApplication.getContext().cacheDir, "OkHttpCache")
        builder.cache(Cache(httpCacheDirectory, 10 * 1024 * 1024))

        mInterceptors.map {
            builder.addInterceptor(it)
        }

        val client = builder.build()
        if (request.isDownload) {
            mDownloadClient = client
        } else {
            mClient = client
        }

        return client
    }

    private fun getConverterFactory(type: Int): Converter.Factory {
        return when (type) {
            IRequest.ConverterType.STRING -> ScalarsConverterFactory.create()
            IRequest.ConverterType.JSON -> GsonConverterFactory.create(getGson())
            IRequest.ConverterType.PROTO -> ProtoConverterFactory.create()
            else -> ScalarsConverterFactory.create()
        }
    }

    private fun getCallAdapter(type: Int): CallAdapter.Factory? {
        return when (type) {
            IRequest.CallType.COROUTINE -> CoroutineCallAdapterFactory()
//            IRequest.CallType.OBSERVABLE -> RxJava2CallAdapterFactory.create()
            else -> null
        }
    }

}