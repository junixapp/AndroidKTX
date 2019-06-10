package com.lxj.androidktx.okhttp

import com.lxj.androidktx.core.toBean
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okio.Okio
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.net.URLConnection.getFileNameMap


/**
 * http扩展，使用起来像这样：
 * 协程中使用：    "http://www.baidu.com".http().get<Bean>().await()
 * 非协程中使用：  "http://www.baidu.com".http().get<Bean>(callback)
 * Create by lxj, at 2018/12/19
 * @param tag 请求的tag
 */
fun String.http(tag: Any = this): RequestWrapper {
    return RequestWrapper(tag, url = "${OkWrapper.baseUrl}${this}")
}

/**
 * get请求，需在协程中使用。结果为空即为http请求失败，并会将失败信息打印日志。
 */
inline fun <reified T> RequestWrapper.get(): Deferred<T?> {
    return defferedRequest(buildGetRequest(), this)
}

/**
 * callback style，不在协程中使用
 */
inline fun <reified T> RequestWrapper.get(cb: HttpCallback<T>) {
    callbackRequest(buildGetRequest(), cb, this)
}

/**
 * post请求，需在协程中使用。结果为空即为http请求失败，并会将失败信息打印日志。
 */
inline fun <reified T> RequestWrapper.post(): Deferred<T?> {
    return defferedRequest(buildPostRequest(), this)
}

inline fun <reified T> RequestWrapper.postJson(json: String): Deferred<T?> {
    return defferedRequest(buildPostRequest(buildJsonBody(json)), this)
}

/**
 * callback style，不在协程中使用
 */
inline fun <reified T> RequestWrapper.post(cb: HttpCallback<T>) {
    callbackRequest(buildPostRequest(), cb, this)
}

inline fun <reified T> RequestWrapper.postJson(json: String, cb: HttpCallback<T>) {
    callbackRequest(buildPostRequest(buildJsonBody(json)), cb, this)
}

/**
 * put请求，需在协程中使用。结果为空即为http请求失败，并会将失败信息打印日志。
 */
inline fun <reified T> RequestWrapper.put(): Deferred<T?> {
    return defferedRequest(buildPutRequest(), this)
}

inline fun <reified T> RequestWrapper.putJson(json: String): Deferred<T?> {
    return defferedRequest(buildPutRequest(buildJsonBody(json)), this)
}

/**
 * callback style，不在协程中使用
 */
inline fun <reified T> RequestWrapper.put(cb: HttpCallback<T>) {
    callbackRequest(buildPutRequest(), cb, this)
}

inline fun <reified T> RequestWrapper.putJson(json: String, cb: HttpCallback<T>) {
    callbackRequest(buildPutRequest(buildJsonBody(json)), cb, this)
}

/**
 * delete请求，需在协程中使用。结果为空即为http请求失败，并会将失败信息打印日志。
 */
inline fun <reified T> RequestWrapper.delete(): Deferred<T?> {
    return defferedRequest(buildDeleteRequest(), this)
}

/**
 * callback style，不在协程中使用
 */
inline fun <reified T> RequestWrapper.delete(cb: HttpCallback<T>) {
    callbackRequest(buildDeleteRequest(), cb, this)
}


inline fun <reified T> defferedRequest(request: Request, reqWrapper: RequestWrapper): Deferred<T?> {
    val req = request.newBuilder().tag(reqWrapper.tag())
            .build()
    val call = OkWrapper.okHttpClient.newCall(req)
            .apply { OkWrapper.requestCache[reqWrapper.tag()] = this } //cache req
    val deferred = CompletableDeferred<T?>()
    deferred.invokeOnCompletion {
        if (deferred.isCancelled) {
            OkWrapper.requestCache.remove(reqWrapper.tag())
            call.cancel()
        }
    }
    try {
        val response = call.execute()
        if (response.isSuccessful && response.body()!=null) {
            when {
                T::class.java == String::class.java -> deferred.complete(response.body()!!.string() as T)
                T::class.java == File::class.java -> {
                    val file = File(reqWrapper.savePath)
                    if(!file.exists())file.createNewFile()
                    response.body()!!.byteStream().copyTo(file.outputStream())
                    deferred.complete(file as T)
                }
                else -> deferred.complete(response.body()!!.string().toBean<T>())
            }
        } else {
            deferred.complete(null) //not throw, pass null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        deferred.complete(null) //pass null
    } finally {
        OkWrapper.requestCache.remove(reqWrapper.tag())
    }
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
                    if (T::class.java == String::class.java) {
                        cb.onSuccess(response.body()!!.string() as T)
                    } else {
                        cb.onSuccess(response.body()!!.string().toBean<T>())
                    }
                    when {
                        T::class.java == String::class.java -> cb.onSuccess(response.body()!!.string() as T)
                        T::class.java == File::class.java -> {
                            val file = File(reqWrapper.savePath)
                            if(!file.exists())file.createNewFile()
                            response.body()!!.byteStream().copyTo(file.outputStream())
                            cb.onSuccess(file as T)
                        }
                        else -> cb.onSuccess(response.body()!!.string().toBean<T>())
                    }
                } else {
                    cb.onFail(IOException("request to ${request.url()} is fail; http code: ${response.code()}!"))
                }
            }
        })
    }
}

// parse some new media type.
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

