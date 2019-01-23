package com.lxj.androidktx.okhttp

import com.lxj.androidktx.core.e
import com.lxj.androidktx.core.toBean
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.net.URLConnection.getFileNameMap


/**
 * Description: Http相关扩展
 * Create by lxj, at 2018/12/19
 */


/**
 * http扩展，使用起来像这样：
 * 协程中使用：    "http://www.baidu.com".http().get<Bean>()
 * 非协程中使用：  "http://www.baidu.com".http().get<Bean>(callback)
 *
 * @param tag 请求的tag
 */
fun String.http(tag: Any = this): RequestWrapper {
    return RequestWrapper(tag, url = this)
}

/**
 * get请求，阻塞调用，需在协程中使用。结果为空即为失败，并会将失败信息打印日志。
 */
inline fun <reified T> RequestWrapper.get(): Deferred<T?> {
    return doRequest(buildGetRequest(), this)
}

/**
 * callback style，不在协程中使用
 */
inline fun <reified T> RequestWrapper.get(cb: HttpCallback<T>) {
    callbackRequest(buildGetRequest(), cb, this)
}

/**
 * post请求，阻塞调用，需在协程中使用。结果为空即为失败，并会将失败信息打印日志。
 */
inline fun <reified T> RequestWrapper.post(): Deferred<T?> {
    return doRequest(buildPostRequest(), this)
}

/**
 * callback style，不在协程中使用
 */
inline fun <reified T> RequestWrapper.post(cb: HttpCallback<T>) {
    callbackRequest(buildPostRequest(), cb, this)
}

/**
 * put请求，阻塞调用，需在协程中使用。结果为空即为失败，并会将失败信息打印日志。
 */
inline fun <reified T> RequestWrapper.put(): Deferred<T?> {
    return doRequest(buildPostRequest(), this)
}

/**
 * callback style，不在协程中使用
 */
inline fun <reified T> RequestWrapper.put(cb: HttpCallback<T>) {
    callbackRequest(buildPostRequest(), cb, this)
}

/**
 * delete请求，阻塞调用，需在协程中使用。结果为空即为失败，并会将失败信息打印日志。
 */
inline fun <reified T> RequestWrapper.delete(): Deferred<T?> {
    return doRequest(buildPostRequest(), this)
}

/**
 * callback style，不在协程中使用
 */
inline fun <reified T> RequestWrapper.delete(cb: HttpCallback<T>) {
    callbackRequest(buildPostRequest(), cb, this)
}


inline fun <reified T> doRequest(request: Request, reqWrapper: RequestWrapper): Deferred<T?> {
    val req = request.newBuilder().tag(reqWrapper.tag())
            .build()
    val call = OkWrapper.okHttpClient.newCall(req)
            .apply { OkWrapper.requestCache[reqWrapper.tag()] = this } //cache req
    OkWrapper.requestCache.remove(reqWrapper.tag())
//    return if (response.isSuccessful) {
//        if ("ktx" is T) {
//            response.body()!!.string() as T
//        } else {
//            response.body()!!.string().toBean<T>()
//        }
//    } else {
//        "request to ${request.url()} is fail; http code: ${response.code()}!".e()
//        null
//    }
    val deferred = CompletableDeferred<T?>()
    deferred.invokeOnCompletion {
        if (deferred.isCancelled)
            call.cancel()
    }
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            deferred.completeExceptionally(e)
        }
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                if ("ktx" is T) {
                    deferred.complete(response.body()!!.string() as T)
                } else {
                    deferred.complete(response.body()!!.string().toBean<T>())
                }
            } else {
                //not throw
//              deferred.completeExceptionally(IOException(response))
                "request to ${request.url()} is fail; http code: ${response.code()}!".e()
                deferred.complete(null) //pass null
            }
        }
    })
    return deferred
}

inline fun <reified T> callbackRequest(request: Request, cb: HttpCallback<T>, reqWrapper: RequestWrapper) {
    val req = request.newBuilder().tag(reqWrapper.tag()).build()
    OkWrapper.okHttpClient.newCall(req).apply {
        OkWrapper.requestCache[reqWrapper.tag()] = this //cache req
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                OkWrapper.requestCache.remove(reqWrapper.tag())
                cb.onFail(e)
            }

            override fun onResponse(call: Call, response: Response) {
                OkWrapper.requestCache.remove(reqWrapper.tag())
                if (response.isSuccessful) {
                    if ("ktx" is T) {
                        cb.onSuccess(response.body()!!.string() as T)
                    } else {
                        cb.onSuccess(response.body()!!.string().toBean<T>())
                    }
                } else {
                    cb.onFail(IOException("request to ${request.url()} is fail; http code: ${response.code()}!"))
                }
            }
        })
    }

}

fun File.mediaType(): String {
    return getFileNameMap().getContentTypeFor(name) ?: when (extension.toLowerCase()) {
        "json" -> "application/json"
        "js" -> "application/javascript"
        "apk" -> "application/vnd.android.package-archive"
        "md" -> "text/x-markdown"
        "webp" -> "image/webp"
        else -> "application/octet-stream"
    }
}

