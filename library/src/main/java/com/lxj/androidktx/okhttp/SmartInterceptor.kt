package com.lxj.androidktx.okhttp

import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.core.isJsonArray
import com.lxj.androidktx.core.isJsonObject
import com.lxj.androidktx.core.sp
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset

/**
 * 通用的拦截器，支持json response text callback.
 * 1. 支持token插入
 * 2. 支持response的hook，适用于要对返回数据进行解密的场景
 * @param tokenField token的字段名
 * @param tokenCreator token值的获取函数
 * @param networkErrorToast 网络失败的提示文字，如果为空则不检测网络失败
 * @param onGetJsonData 获取到响应数据的回调
 */
class SmartInterceptor(var tokenField: String = "token",
                       var tokenCreator: (()->String)? = null,
                       var networkErrorToast: String? = null,
                       var onGetJsonData: ((url: String,json: String, header: Headers) -> Unit)? = null ,
                       var onHookResponse: ((url: String, data: String?, response: Response) -> Response)? = null
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!networkErrorToast.isNullOrEmpty() && !NetworkUtils.isConnected()){
            ToastUtils.showShort(networkErrorToast)
        }

        var request = chain.request()
        val tokenValue = if(tokenCreator!=null) tokenCreator!!() else sp().getString("token", null)
        if (tokenValue != null) {
            request = request.newBuilder().addHeader(tokenField, tokenValue).build()
        }
        val response = chain.proceed(request)
        val contentType = response.header("content-type")
        if (contentType?.contains("application/json") == true ||
            contentType?.contains("text/html") == true) { //存在一些不遵守规范的后端选手
            val source = response.body()?.source()
            source?.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val data = source?.buffer()?.clone()?.readString(Charset.forName("UTF-8"))
            val isJson = data.isJsonObject() || data.isJsonArray()
            if (isJson) onGetJsonData?.invoke(request.url().toString(), data!!, response.headers())
            if (onHookResponse!=null) return onHookResponse!!(request.url().toString(), data, response)
        }
        return response
    }

}