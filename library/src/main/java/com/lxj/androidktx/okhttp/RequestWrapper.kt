package com.lxj.androidktx.okhttp

import com.lxj.androidktx.core.toJson
import com.lxj.androidktx.okhttp.progressmanager.ProgressListener
import com.lxj.androidktx.okhttp.progressmanager.ProgressManager
import com.lxj.androidktx.okhttp.progressmanager.body.ProgressInfo
import com.lxj.androidktx.util.DirManager
import okhttp3.*
import java.io.File
import java.lang.Exception
import java.net.URLEncoder

/**
 * Description:
 * Create by lxj, at 2018/12/27
 */
data class RequestWrapper(
        private var tag: Any = "",
        private var url: String = "",
        private var savePath: String? = "",
        private var headers: ArrayList<Pair<String, String>> = arrayListOf(),
        private var params: Map<String, Any> = mapOf(),
        private var listParams: List<Any>? = null,
        private var isJsonParam: Boolean = false, //是否是json编码
        private var isQueryParam: Boolean = false, //是否是query参数
        private var isMultiPartParam: Boolean = false, //是否是multi-part编码
        private var customReqBody: RequestBody? = null //自定义body
) {
    fun headers(vararg headers: Pair<String, Any>): RequestWrapper {
        headers.forEach { this.headers.add(Pair(it.first, "${it.second}")) }
        return this
    }

    fun headers(map: Map<String, Any>): RequestWrapper {
        map.forEach { this.headers.add(Pair(it.key, "${it.value}")) }
        return this
    }

    /**
     * 设置参数
     * @param map 参数
     * @param isJson 是否是json编码，默认false
     * @param isMultiPart 是否是multi-part编码，默认为false。OkWrapper会自动识别是否为multi-part编码，只有你想强制指定的时候会用到这个参数
     */
    fun params(map: Map<String, Any>, isJson: Boolean = false, isMultiPart: Boolean = false, isQuery: Boolean = false): RequestWrapper {
        isJsonParam = isJson
        isMultiPartParam = isMultiPart
        isQueryParam = isQuery
        this.params = map
        return this
    }

    fun params(list: List<Any>, isJson: Boolean = false): RequestWrapper {
        isJsonParam = isJson
        this.listParams = list
        return this
    }

    /**
     * 自定义任意body
     */
    fun customBody(body: RequestBody): RequestWrapper{
        customReqBody = body
        return this
    }

    /**
     * 以application/json编码的body封装
     */
    fun jsonParam(json: String): RequestWrapper{
        customReqBody = buildJsonBody(json)
        return this
    }

    /**
     * 下载文件的保存路径
     */
    fun savePath(path: String): RequestWrapper {
        this.savePath = path
        return this
    }

    fun savePath() = savePath ?: "${DirManager.downloadDir}/${System.currentTimeMillis()}"

    private fun url() = url
    fun tag() = tag
    private fun params() = params
    fun buildGetRequest(): Request {
        return bodyBuilder().get().build()
    }

    fun buildPostRequest(): Request {
        return bodyBuilder().post(customReqBody?: buildRequestBody()).build()
    }
    fun buildPutRequest(): Request {
        return bodyBuilder().put(customReqBody?: buildRequestBody()).build()
    }
    fun buildDeleteRequest(): Request {
        return bodyBuilder().delete(customReqBody?: buildRequestBody()).build()
    }
    private fun bodyBuilder(): Request.Builder{
        return Request.Builder().url( if(isQueryParam) urlParams() else url())
                .apply {
                    OkExt.globalHeaders.forEach { addHeader(it.first, it.second) }
                    headers.forEach { addHeader(it.first, it.second) }
                }
    }

    private fun buildRequestBody(): RequestBody {
        if (isMultiPartParam || isAutoMultiPart()) {
            val pairs = arrayListOf<Pair<String, Any>>()
            params.forEach { pairs.add(Pair(it.key, if (it.value is File || it.value is Array<*> || it.value is Collection<*>) it.value else "${it.value}")) }
            // 自动识别 multipart/form-data
            val builder = MultipartBody.Builder()
            pairs.forEach {
                if (it.second is String) {
                    builder.addFormDataPart(it.first, it.second as String)
                } else if (it.second is File) { //single file
                    val file = it.second as File
                    builder.addFormDataPart(it.first, file.name, RequestBody.create(MediaType.parse(file.mediaType()), file))
                } else if(it.second is Array<*>){ //multi file
                    val arr = it.second as Array<*>
                    if(arr.isNotEmpty() && arr[0] is File){
                        arr.forEach {el->
                            val file = el as File
                            builder.addFormDataPart(it.first, file.name, RequestBody.create(MediaType.parse(file.mediaType()), file))
                        }
                    }
                }else if(it.second is Collection<*>){ //multi file
                    val coll = it.second as Collection<*>
                    if(coll.isNotEmpty() && coll.iterator().next() is File){
                        coll.forEach {el->
                            val file = el as File
                            builder.addFormDataPart(it.first, file.name, RequestBody.create(MediaType.parse(file.mediaType()), file))
                        }
                    }
                }
            }
            return builder.setType(MultipartBody.FORM).build()
        } else if(isJsonParam){
            // json编码
            return buildJsonBody(if(listParams!=null && params.isEmpty()) listParams!!.toJson(dateFormat = OkExt.dateFormat)
            else params.toJson(dateFormat = OkExt.dateFormat))
        }else{
            // default is form-data/url-encoded
            val builder = FormBody.Builder()
            params.forEach { builder.add(it.key, "${it.value}")  }
            return builder.build()
        }
    }

    private fun buildJsonBody(json: String): RequestBody{
        return RequestBody.create(MediaType.parse("application/json"), json)
    }

    private fun isAutoMultiPart() = params.any { it.value is File ||
            (it.value is Array<*> && (it.value as Array<*>).isArrayOf<File>()) ||
            (it.value is Collection<*> && (it.value as Collection<*>).isNotEmpty()
                    && (it.value as Collection<*>).iterator().next() is File)}

    private fun urlParams(): String {
        val queryParams = if (params().isEmpty()) "" else "?" + params.toList().joinToString(separator = "&", transform = {
            "${it.first}=${URLEncoder.encode(it.second.toString())}"
        })
        return "${url()}$queryParams"
    }

    fun uploadListener(onProgress: (progressInfo: ProgressInfo?)->Unit, onError: ((id: Long, e: Exception?)->Unit)? = null): RequestWrapper{
        ProgressManager.getInstance().addRequestListener(url, object : ProgressListener {
            override fun onProgress(progressInfo: ProgressInfo?) {
                onProgress(progressInfo)
            }
            override fun onError(id: Long, e: Exception?) {
                onError?.invoke(id, e)
            }
        })
        return this
    }

    fun downloadListener(onProgress: (progressInfo: ProgressInfo?)->Unit, onError: ((id: Long, e: Exception?)->Unit)? = null): RequestWrapper{
        ProgressManager.getInstance().addResponseListener(url, object : ProgressListener{
            override fun onProgress(progressInfo: ProgressInfo?) {
                onProgress(progressInfo)
            }
            override fun onError(id: Long, e: Exception?) {
                onError?.invoke(id, e)
            }
        })
        return this
    }
}

