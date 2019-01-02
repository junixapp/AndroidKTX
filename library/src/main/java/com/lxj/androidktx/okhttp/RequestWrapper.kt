package com.lxj.androidktx.okhttp

import com.lxj.androidktx.core.e
import com.lxj.androidktx.core.toBean
import okhttp3.*
import java.io.IOException
import java.net.URLConnection

/**
 * Description:
 * Create by lxj, at 2018/12/27
 */
data class RequestWrapper(
        private var tag: Any = OkWrapper.javaClass,
        private var url: String = "",
        private var headers: ArrayList<Pair<String, String>> = arrayListOf(),
        private var params: ArrayList<Pair<String, String>> = arrayListOf()
) {
    fun headers(vararg headers: Pair<String, Any>): RequestWrapper {
        headers.forEach { this.headers.add(Pair(it.first, "${it.second}")) }
        return this
    }

    fun params(vararg params: Pair<String, Any>): RequestWrapper {
        params.forEach { this.params.add(Pair(it.first, "${it.second}")) }
        return this
    }

    fun headers() = headers
    fun url() = url
    fun params() = params

    /**
     * get请求，阻塞调用，需在协程中使用。结果为空即为失败，并会将失败信息打印日志。
     */
    inline fun <reified T> get(): T? {
        val request = Request.Builder().url(urlParams())
                .headers(OkWrapper.pairs2Headers(headers()))
                .get().build()
        val response = OkWrapper.okHttpClient.newCall(request).execute()
        return if (response.isSuccessful) {
            if ("ktx" is T) {
                response.body()!!.string() as T
            } else {
                response.body()!!.string().toBean<T>()
            }
        } else {
            "request to ${url()} is fail; http code: ${response.code()}!".e()
            null
        }

        // deferred版本
//        val deferred = CompletableDeferred<T>()
//        deferred.invokeOnCompletion {
//            if (deferred.isCancelled) {
//                call.cancel()
//            }
//        }
//        call.enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                deferred.completeExceptionally(e)
//            }
//            override fun onResponse(call: Call, response: Response) {
//                if (response.isSuccessful) {
//                    if ("ktx" is T) {
//                        deferred.complete(response.body()!!.string() as T)
//                    } else {
//                        deferred.complete(response.body()!!.string().toBean<T>())
//                    }
//                } else {
//                    deferred.completeExceptionally(
//                            IOException("request to ${url()} is fail; http code: ${response.code()}!"))
//                }
//            }
//        })
//        return deferred
    }

    /**
     * callback style
     */
    inline fun <reified T> get(cb: HttpCallback<T>) {
        val request = Request.Builder().url(urlParams())
                .headers(OkWrapper.pairs2Headers(headers()))
                .get().build()
        OkWrapper.okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                cb.onFail(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    if ("ktx" is T) {
                        cb.onSuccess(response.body()!!.string() as T)
                    } else {
                        cb.onSuccess(response.body()!!.string().toBean<T>())
                    }
                } else {
                    cb.onFail(IOException("request to ${url()} is fail; http code: ${response.code()}!"))
                }
            }
        })
    }

    fun urlParams(): String {
        val queryParams = if (params().isEmpty()) "" else "?" + params.joinToString(separator = "&", transform = {
            "${it.first}=${it.second}"
        })
        return "${url()}$queryParams"
    }



}

fun main(args: Array<String>) {
    println(arrayListOf<Pair<String, String>>("a" to "b", "1" to "2").joinToString(separator = "&", transform = {
        "${it.first}=${it.second}"
    }))
}
