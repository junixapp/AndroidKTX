package com.lxj.androidktx.okhttp

import com.lxj.androidktx.core.toBean
import okhttp3.Request
import java.io.IOException

/**
 * Description:
 * Create by lxj, at 2018/12/27
 */
data class RequestWrapper(
        var tag: Any = OkWrapper.javaClass,
        var url: String = "",
        var headers: ArrayList<Pair<String, String>> = arrayListOf(),
        var params:  ArrayList<Pair<String, String>> = arrayListOf()
){
    fun headers(vararg headers: Pair<String, String>): RequestWrapper{
        headers.forEach { this.headers.add(it) }
        return this
    }

    fun params(vararg params: Pair<String, String>): RequestWrapper{
        params.forEach { this.params.add(it) }
        return this
    }

    inline fun <reified T> get():T{
        val request = Request.Builder().get().build()
        val response = OkWrapper.okHttpClient.newCall(request).execute()
        if(response.isSuccessful){
            return response.body()!!.string().toBean<T>()
        }else{
            throw IOException(" reqeust to ${this.url} is fail; http code: ${response.code()}!")
        }
    }
}
