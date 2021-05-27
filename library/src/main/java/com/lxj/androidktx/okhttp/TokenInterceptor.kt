package com.lxj.androidktx.okhttp

import com.lxj.androidktx.core.sp
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset

/**
 * 通用的Token拦截器，支持json response text callback.
 */
class TokenInterceptor(var tokenField: String = "token",
                       var tokenCreator: (()->String)? = null,
                       var onGetBodyData: (resData: String) -> Unit) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val tokenValue = if(tokenCreator!=null) tokenCreator!!() else sp().getString("token", null)
        if (tokenValue != null) {
            request = request.newBuilder().addHeader(tokenField, tokenValue).build()
        }
        val response = chain.proceed(request)
        if (response.header("content-type")?.contains("application/json") == true) {
            //json type
            val source = response.body()?.source()
            source?.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val data = source?.buffer()?.clone()?.readString(Charset.forName("UTF-8"))
            if (data != null) onGetBodyData(data)
        }
        return response
    }

}