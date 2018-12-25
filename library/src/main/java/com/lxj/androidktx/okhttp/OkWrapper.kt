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
    val globalHeaders = arrayListOf<Pair<String, String>>()

    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .writeTimeout(AndroidKtxConfig.httpTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(AndroidKtxConfig.httpTimeout, TimeUnit.MILLISECONDS)
            .connectTimeout(AndroidKtxConfig.httpTimeout, TimeUnit.MILLISECONDS)
            .build()

    fun headers(vararg headers: Pair<String, String>): OkWrapper {
        headers.forEach { globalHeaders.add(it) }
        return this
    }

    fun interceptors(vararg interceptors: Interceptor): OkWrapper {
        okHttpClient = okHttpClient.newBuilder().build()
        return this
    }

    /**
     * 转为Headers
     */
    fun genGlobalHeaders(): Headers {
        val builder = Headers.Builder()
        globalHeaders.forEach { builder.add(it.first, it.second) }
        return  builder.build()
    }

}