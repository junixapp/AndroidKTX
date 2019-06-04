package com.lxj.androidktx.okhttp

import com.lxj.androidktx.util.HttpsUtils
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Description: OkHttp封装
 * Create by lxj, at 2018/12/25
 */
object OkWrapper {

    private var httpTimeout = 10000L  //10s
    val globalHeaders = arrayListOf<Pair<String, String>>()
    val requestCache = hashMapOf<Any, Call>()
    var baseUrl = ""
    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .writeTimeout(httpTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(httpTimeout, TimeUnit.MILLISECONDS)
            .connectTimeout(httpTimeout, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLogInterceptor())
            .sslSocketFactory(HttpsUtils.getSslSocketFactory().sSLSocketFactory,
                    HttpsUtils.getSslSocketFactory().trustManager)
            .build()

    /**
     * 设置全局公共Header
     */
    fun headers(vararg headers: Pair<String, String>): OkWrapper {
        headers.forEach { globalHeaders.add(it) }
        return this
    }

    /**
     * 设置拦截器
     */
    fun interceptors(vararg interceptors: Interceptor): OkWrapper {
        val builder = okHttpClient.newBuilder()
        interceptors.forEach { builder.addInterceptor(it) }
        okHttpClient = builder.build()
        return this
    }

    fun baseUrl(url: String): OkWrapper{
        this.baseUrl = url
        return this
    }

    /**
     * 设置自定义的Client
     */
    fun setClient(client: OkHttpClient): OkWrapper{
        okHttpClient = client
        return this
    }


    fun cancel(tag: Any){
        requestCache[tag]?.cancel()
        requestCache.remove(tag)
    }
}