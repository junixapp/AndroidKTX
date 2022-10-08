package com.lxj.androidktx.okhttp

import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.core.*
import com.lxj.androidktx.okhttp.getSync
import com.lxj.androidktx.okhttp.http
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.nio.charset.Charset

/**
 * 自动刷新token的通用拦截器，基于token放置于请求header的前提下，你需要实现以下3个逻辑
 * 1. 如何保存你的token
 * 2. 如何获取你的token
 * 3. 判断token过期的条件
 * 4. 如果刷新token，其实是一个同步的http请求
 * @param tokenField token字段，有的叫token，有的叫Authorization，你不传默认就叫token
 */
class AuthRefreshTokenInterceptor(var tokenField: String = "token",
        var getToken: ()->String?,
        var saveToken: (Headers, String)->Unit,
        var isTokenExpire: ()->Boolean,
        var refreshToken: ()->Unit,
        var onGetJsonData: ((String,Headers,String)->Unit)? = null,
        ) : Interceptor {

    val tag = "RefreshTokenInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
//        if(!NetworkUtils.isConnected()){
//            ToastUtils.showShort(AndroidKTX.context.string(R.string.network_disconnected_please_check))
//        }

        var request = chain.request()
        //1. check token
        if(isTokenExpire()){

        }else{
            val tokenValue = getToken()
            if (!tokenValue.isNullOrEmpty()) {
                request = request.newBuilder().addHeader(tokenField, tokenValue).build()
            }
        }

        val response = chain.proceed(request)
        if(isTokenExpire()){
            refreshToken()
            //resend request
            val newRequest: Request = chain.request()
                .newBuilder()
                .build()
            return chain.proceed(newRequest)
        }else{
            val contentType = response.header("content-type")
            if (contentType?.contains("application/json") == true ||
                contentType?.contains("text/html") == true) { //存在一些不遵守规范的后端选手
                //json type
                val source = response.body()?.source()
                source?.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val data = source?.buffer()?.clone()?.readString(Charset.forName("UTF-8"))
                val isJson = data.isJsonObject() || data.isJsonArray()
                if (isJson ){
                    if(onGetJsonData!=null)onGetJsonData!!(request.url().toString(),  response.headers(), data!!,)
                    saveToken(response.headers(), data!!)
                }
            }
        }

//        if(response.isSuccessful){
//            //请求成功，检测token是否过期
//            checkToken()
//        } else if(response.code==401){
//            //token已过期，刷新token
//            LogUtils.dTag(tag, "token已过期，正在刷新token...")
//            getToken()
//            LogUtils.dTag(tag, "token刷新成功...")
//            //使用新的Token，创建发起请求
//            val newRequest: Request = chain.request()
//                .newBuilder()
////                .header("Authorization", res.data?.accessToken)
//                .build()
//            return chain.proceed(newRequest)
//        }
        return response
//        try {
//
//        }catch (e: Exception){
//            LogUtils.e("request failed to ${request.url().toString()}")
//            e.printStackTrace()
////            ToastUtils.showShort(defError)
//        }
//        if (response.header("content-type")?.contains("application/json") == true) {
//            //json type
//            val source = response.body()?.source()
//            source?.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
//            val data = source?.buffer()?.clone()?.readString(Charset.forName("UTF-8"))
//            if (data != null) onGetBodyData(data)
//        }
//        return chain.proceed(request)
    }

//    fun saveToken(response: Response){
//        val token = response.headers.get("Authorization") ?:""
//        val exp = response.headers.get("exp") ?:""
//        if(!token.isNullOrEmpty())sp().putString("token", token?:"")
//        if(!exp.isNullOrEmpty())sp().putString("token_exp", exp?:"")
//    }

    /**
     * 检测token过期时间，起预防作用
     *
     */
    fun checkToken(){
        val oldToken = sp().getString("token", "")
        val token_exp = sp().getString("token_exp", "")
        if(oldToken.isNullOrEmpty() || token_exp.isNullOrEmpty()) return
        val expInMills = token_exp.toDateMills() //date: "2022-10-12 18:43:14"
        val duration = expInMills - System.currentTimeMillis()
        if(duration > 60000 * 9) {
            LogUtils.dTag(tag, "检测token正常，还有${ (duration - 60000 * 9)/1000 }秒过期...")
            return
        }
        LogUtils.dTag(tag, "检测token已过期，正在刷新token...")
        getToken()
        LogUtils.dTag(tag, "检测token过期后，刷新token成功...")
    }

//    /**
//     * 重新请求token并存储到本地
//     */
//    fun getToken(){
//        val oldToken = sp().getString("token", "")
//        var res = "login/user/user/login/refToken".http()
//            .params(mapOf("token" to oldToken!!, "way" to 1))
//            .getSync<BaseResponse<String>>()
//        if(res?.success==true){
//            sp().putString("token", res!!.data)
//        }
//    }
}