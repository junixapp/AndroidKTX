package com.lxj.androidktx.okhttp

import com.lxj.androidktx.core.e
import com.lxj.androidktx.core.toBean
import com.lxj.androidktx.core.toJson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Description: Http相关扩展
 * Create by lxj, at 2018/12/19
 */

// "http://www.baidu.com".http().get<Bean>()
// "http://www.baidu.com".http().get<Bean>(callback)

// https://api.gulltour.com/v1/common/nations

/**
 * 配置基本信息：全局header，tag
 */
fun String.http(tag: Any = this): Request{
    return  Request.Builder()
            .url(this)
            .headers(OkWrapper.genGlobalHeaders())
            .tag(tag)
            .build()
}

/**
 * header配置
 */
fun Request.header(vararg headers: Pair<String, String>):Request{
    return newBuilder()
            .apply { headers.forEach { addHeader(it.first, it.second) } }
            .build()
}

/**
 * 执行Get请求
 */
inline fun <reified T> Request.get(callback: HttpCallback<T>){
    val request = newBuilder().get().build()

    OkWrapper.okHttpClient.newCall(request).enqueue(object : Callback{
        override fun onFailure(call: Call, e: IOException) {
            callback.onFail(e)
        }

        override fun onResponse(call: Call, response: Response) {
            if(response.isSuccessful){
                callback.onSuccess(response.body()!!.string().toBean<T>())
            }else{
//                onFailure(call)
            }
        }
    })
}

inline fun <reified T> Request.get():T{
    val request = newBuilder().get().build()
    val response = OkWrapper.okHttpClient.newCall(request).execute()
    if(response.isSuccessful){
        return response.body()!!.string().toBean<T>()
    }else{
        throw IOException(" Reqeust to ${url()} is fail!")
    }
}




interface HttpCallback<T>{
    fun onSuccess(t: T)
    fun onFail(e: IOException){}
}

data class Aa(
        var code: Int = 0,
        var message: String = "",
        var data: Any
)

//fun main(args: Array<String>) {
//    "https://api.gulltour.com/v1/common/nations".http()
//            .get(object : HttpCallback<Aa>{
//                override fun onSuccess(t: Aa) {
//                    print(t.toJson())
//                }
//            })
//}

fun main(args: Array<String>) {

}