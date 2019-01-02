package com.lxj.androidktx.okhttp

import com.lxj.androidktx.AndroidKtxConfig
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Description: OkHttp封装
 * Create by lxj, at 2018/12/25
 */
object OkWrapper {
    private val globalHeaders = arrayListOf<Pair<String, String>>()

    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .writeTimeout(AndroidKtxConfig.httpTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(AndroidKtxConfig.httpTimeout, TimeUnit.MILLISECONDS)
            .connectTimeout(AndroidKtxConfig.httpTimeout, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLogInterceptor())
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

    /**
     * 转为Headers
     */
    fun genGlobalHeaders(): Headers {
        return  pairs2Headers(globalHeaders)
    }

    /**
     * 设置自定义的Client
     */
    fun setClient(client: OkHttpClient){
        okHttpClient = client
    }

    fun pairs2Headers(pairs: ArrayList<Pair<String, String>>): Headers {
        val builder = Headers.Builder()
        pairs.forEach { builder.add(it.first, it.second) }
        return  builder.build()
    }
}