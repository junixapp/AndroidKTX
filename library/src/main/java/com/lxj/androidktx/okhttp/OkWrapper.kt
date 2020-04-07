package com.lxj.androidktx.okhttp

import com.lxj.androidktx.okhttp.cookie.PersistentCookieStore
import com.lxj.androidktx.util.HttpsUtils
import me.jessyan.progressmanager.ProgressManager
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

/**
 * Description: OkHttp封装
 * Create by lxj, at 2018/12/25
 */
object OkWrapper {
    const val DefaultUrlTag = "okhttp"
    private var httpTimeout = 15000L  //15s
    val globalHeaders = arrayListOf<Pair<String, String>>()
    val requestCache = hashMapOf<Any, Call>()
    val baseUrlMap = hashMapOf<Any, String>() //存储多个baseUrl, key使用tag来存储
    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .writeTimeout(httpTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(httpTimeout, TimeUnit.MILLISECONDS)
            .connectTimeout(httpTimeout, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(HttpLogInterceptor())
            .cookieJar(PersistentCookieStore())
            .sslSocketFactory(HttpsUtils.getSslSocketFactory().sSLSocketFactory,
                    HttpsUtils.getSslSocketFactory().trustManager)
            .build()

    init {
        okHttpClient = ProgressManager.getInstance().with(okHttpClient.newBuilder()).build()
    }

    /**
     * 自定义超时时间
     */
    fun timeout(timeout: Long){
        val builder = okHttpClient.newBuilder()
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
        okHttpClient = builder.build()
    }

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

    fun baseUrl(tag: String = DefaultUrlTag, url: String): OkWrapper{
        if(!url.endsWith("/")){
            throw IllegalArgumentException("baseUrl必须以/结尾")
        }
        baseUrlMap[tag] = url
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