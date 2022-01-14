package com.lxj.androidktxdemo.entity

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Description:
 * Create by dance, at 2018/12/11
 */
data class User(
        var id: String = UUID.randomUUID().toString(),
        var name: String,
        var age: Int = 0,
        var date : Date? = null,
        var gender: UserGender = UserGender.Man
)

enum class UserGender{
        @SerializedName(value = "1")
        Man,
        @SerializedName(value = "2")
        Women
}

data class HttpResult<T>(
        var status: String? = null,
        var errcode: String? = null,
        var errmsg: String? = null,
        var data: T? = null
){
    fun isSuccess() = status=="success" || errcode=="0"
}