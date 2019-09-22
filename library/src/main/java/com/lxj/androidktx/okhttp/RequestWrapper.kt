package com.lxj.androidktx.okhttp

import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.progressmanager.body.ProgressInfo
import okhttp3.*
import java.io.File
import java.lang.Exception

/**
 * Description:
 * Create by lxj, at 2018/12/27
 */
data class RequestWrapper(
        private var tag: Any = "",
        private var url: String = "",
        var savePath: String = "",
        private var headers: ArrayList<Pair<String, String>> = arrayListOf(),
        private var params: ArrayList<Pair<String, Any>> = arrayListOf()
) {
    fun headers(vararg headers: Pair<String, Any>): RequestWrapper {
        headers.forEach { this.headers.add(Pair(it.first, "${it.second}")) }
        return this
    }

    fun headers(map: Map<String, Any>): RequestWrapper {
        map.forEach { this.headers.add(Pair(it.key, "${it.value}")) }
        return this
    }

    fun params(vararg params: Pair<String, Any>): RequestWrapper {
        params.forEach { this.params.add(Pair(it.first, if (it.second is File || it.second is Array<*> || it.second is List<*>) it.second else "${it.second}")) }
        return this
    }

    fun params(map: Map<String, Any>): RequestWrapper {
        map.forEach { this.params.add(Pair(it.key, if (it.value is File || it.value is Array<*>) it.value else "${it.value}")) }
        return this
    }

    /**
     * 下载文件的保存路径
     */
    fun savePath(path: String): RequestWrapper {
        this.savePath = path
        return this
    }

    private fun url() = url
    fun tag() = tag
    private fun params() = params
    fun buildGetRequest(): Request {
        return Request.Builder().url(urlParams())
                .apply {
                    OkWrapper.globalHeaders.forEach { addHeader(it.first, it.second) }
                    headers.forEach { addHeader(it.first, it.second) }
                }
                .get().build()
    }

    fun buildPostRequest(customReqBody: RequestBody? = null): Request {
        return bodyBuilder().post(customReqBody?: buildRequestBody()).build()
    }
    fun buildPutRequest(customReqBody: RequestBody? = null): Request {
        return bodyBuilder().put(customReqBody?: buildRequestBody()).build()
    }
    fun buildDeleteRequest(customReqBody: RequestBody? = null): Request {
        return bodyBuilder().delete(customReqBody?: buildRequestBody()).build()
    }
    private fun bodyBuilder(): Request.Builder{
        return Request.Builder().url(url())
                .apply {
                    OkWrapper.globalHeaders.forEach { addHeader(it.first, it.second) }
                    headers.forEach { addHeader(it.first, it.second) }
                }
    }

    private fun buildRequestBody(): RequestBody {
        return if (isMultiPart()) {
            // multipart/form-data
            val builder = MultipartBody.Builder()
            params.forEach {
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
                }else if(it.second is List<*>){ //multi file
                    val coll = it.second as List<*>
                    if(coll.isNotEmpty() && coll[0] is File){
                        coll.forEach {el->
                            val file = el as File
                            builder.addFormDataPart(it.first, file.name, RequestBody.create(MediaType.parse(file.mediaType()), file))
                        }
                    }
                }
            }
            builder.setType(MultipartBody.FORM).build()
        } else {
            // form-data url-encoded
            val builder = FormBody.Builder()
            params.forEach { builder.add(it.first, it.second as String) }
            builder.build()
        }
    }

    fun buildJsonBody(json: String): RequestBody{
        return RequestBody.create(MediaType.parse("application/json"), json)
    }

    private fun isMultiPart() = params.any { it.second is File || it.second is Array<*> || it.second is List<*>}

    private fun urlParams(): String {
        val queryParams = if (params().isEmpty()) "" else "?" + params.joinToString(separator = "&", transform = {
            "${it.first}=${it.second}"
        })
        return "${url()}$queryParams"
    }

    fun uploadListener(onProgress: (progressInfo: ProgressInfo?)->Unit, onError: ((id: Long, e: Exception?)->Unit)? = null): RequestWrapper{
        ProgressManager.getInstance().addRequestListener(url, object : ProgressListener{
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

