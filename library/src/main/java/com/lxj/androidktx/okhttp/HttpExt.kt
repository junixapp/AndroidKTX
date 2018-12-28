package com.lxj.androidktx.okhttp

import com.lxj.androidktx.core.e
import com.lxj.androidktx.core.toBean
import okhttp3.*
import java.io.IOException

/**
 * Description: Http相关扩展
 * Create by lxj, at 2018/12/19
 */

// "http://www.baidu.com".http().get<Bean>()
// "http://www.baidu.com".http().get<Bean>(callback)

/**
 * 配置基本信息：全局header，tag
 */
fun String.http(tag: Any = this): RequestWrapper{
    return RequestWrapper(tag, url = this)
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
                onFailure(call, IOException(" request to ${url()} is fail; http code: ${response.code()}!"))
            }
        }
    })
}


