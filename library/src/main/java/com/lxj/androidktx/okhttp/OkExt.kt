package com.lxj.androidktx.okhttp

import com.lxj.androidktx.okhttp.cookie.PersistentCookieStore
import com.lxj.androidktx.util.HttpsUtils
import me.jessyan.progressmanager.ProgressManager
import okhttp3.*
import java.io.File
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

/**
 * Description: OkHttp封装
 * Create by lxj, at 2018/12/25
 */
object OkExt {
    const val DefaultUrlTag = "okhttp"
    const val NoBaseUrl = ""
    private var httpTimeout = 30000L  //30s
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
    var dateFormat: String = "yyyy-MM-dd HH:mm:ss"
    var globalFailHandler: ((e: Exception?)->Unit)? = null

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
    fun headers(vararg headers: Pair<String, String>): OkExt {
        headers.forEach { globalHeaders.add(it) }
        return this
    }

    /**
     * 设置拦截器
     */
    fun interceptors(vararg interceptors: Interceptor): OkExt {
        val builder = okHttpClient.newBuilder()
        interceptors.forEach { builder.addInterceptor(it) }
        okHttpClient = builder.build()
        return this
    }

    /**
     * 设置baseUrl，必须以/结尾；支持多个baseUrl设置
     */
    fun baseUrl(tag: String = DefaultUrlTag, url: String): OkExt{
        if(!url.endsWith("/")){
            throw IllegalArgumentException("baseUrl必须以/结尾")
        }
        baseUrlMap[tag] = url
        return this
    }

    /**
     * 设置自定义的Client
     */
    fun setClient(client: OkHttpClient): OkExt{
        okHttpClient = client
        return this
    }

    /**
     * 配置解析Json时的时间格式
     */
    fun dateFormat(format: String): OkExt{
        dateFormat = format
        return this
    }

    /**
     * 取消请求
     */
    fun cancel(tag: Any){
        requestCache[tag]?.cancel()
        requestCache.remove(tag)
    }


    /** 一些帮助方法 **/
    fun streamBodyFromFile(filePath: String) =
            RequestBody.create(MediaType.parse("application/octet-stream"), File(filePath))

    fun streamBodyFromBytes(bytes: ByteArray) =
            RequestBody.create(MediaType.parse("application/octet-stream"), bytes)

    fun streamBodyFromString(string: String) =
            RequestBody.create(MediaType.parse("application/octet-stream"), string)
}