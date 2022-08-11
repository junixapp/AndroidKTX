package com.lxj.androidktx.okhttp

import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.core.sp
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

/**
 * 通用的Token拦截器，支持json response text callback.
 * @param tokenField token的字段名
 * @param tokenCreator token值的获取函数
 * @param networkErrorToast 网络失败的提示文字，如果为空则不检测网络失败
 * @param onGetJsonData 获取到响应数据的回调
 */
class TokenInterceptor(var tokenField: String = "token",
                       var tokenCreator: (()->String)? = null,
                       var networkErrorToast: String? = null,
                       var onGetJsonData: ((url: String,json: String) -> Unit)? = null  ) : Interceptor {
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
            contentType?.contains("text/html") == true) {
            //json type
            val source = response.body()?.source()
            source?.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val data = source?.buffer()?.clone()?.readString(Charset.forName("UTF-8"))
            var isJsonData = false
            if(data!=null){
                isJsonData = try {
                    JSONObject(data)
                    true
                }catch (e: JSONException){
                    false
                }
            }
            if (isJsonData && onGetJsonData!=null) onGetJsonData!!(request.url().toString(), data!!)
        }
        return response
    }

}