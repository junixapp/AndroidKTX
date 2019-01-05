package com.lxj.androidktx.okhttp

import okhttp3.*
import java.io.File

/**
 * Description:
 * Create by lxj, at 2018/12/27
 */
data class RequestWrapper(
        private var tag: Any = OkWrapper.javaClass,
        private var url: String = "",
        private var headers: ArrayList<Pair<String, String>> = arrayListOf(),
        private var params: ArrayList<Pair<String, Any>> = arrayListOf()
) {
    fun headers(vararg headers: Pair<String, Any>): RequestWrapper {
        headers.forEach { this.headers.add(Pair(it.first, "${it.second}")) }
        return this
    }

    fun params(vararg params: Pair<String, Any>): RequestWrapper {
        params.forEach { this.params.add(Pair(it.first, if (it.second is File) it.second else "${it.second}")) }
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

    fun buildPostRequest(): Request {
        return bodyBuilder().post(buildRequestBody()).build()
    }
    fun buildPutRequest(): Request {
        return bodyBuilder().put(buildRequestBody()).build()
    }
    fun buildDeleteRequest(): Request {
        return bodyBuilder().delete(buildRequestBody()).build()
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
                } else if (it.second is File) {
                    val file = it.second as File
                    builder.addFormDataPart(it.first, file.name, RequestBody.create(MediaType.parse(file.mediaType()), file))
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

    private fun isMultiPart() = params.any { it.second is File }

    private fun urlParams(): String {
        val queryParams = if (params().isEmpty()) "" else "?" + params.joinToString(separator = "&", transform = {
            "${it.first}=${it.second}"
        })
        return "${url()}$queryParams"
    }

}

