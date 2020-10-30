package com.lxj.androidktx.okhttp

import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

fun RequestBody.streamBodyFromFile(filePath: String) =
        RequestBody.create(MediaType.parse("application/octet-stream"), File(filePath))

fun RequestBody.streamBodyFromBytes(bytes: ByteArray) =
        RequestBody.create(MediaType.parse("application/octet-stream"), bytes)

fun RequestBody.streamBodyFromString(string: String) =
        RequestBody.create(MediaType.parse("application/octet-stream"), string)