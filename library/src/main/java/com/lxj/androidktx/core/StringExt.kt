package com.lxj.androidktx.core

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * 解析url的查询参数
 */
fun String.parseQueryParams(): Map<String, String>{
    var index = lastIndexOf("?") + 1
    var queryParam = hashMapOf<String, String>()
    if(index>0){
        var query = substring(index, length)
        query.split("&").forEach {
            if(it.contains("=")){
                var arr = it.split("=")
                if(arr.size>1){
                    queryParam[arr[0]] = arr[1]
                }
            }
        }
    }
    return queryParam
}


fun String?.isJsonObject(): Boolean{
    if(this.isNullOrEmpty()) return false
    return try {
        JSONObject(this)
        true
    }catch (e: JSONException){
        false
    }
}

fun String?.isJsonArray(): Boolean{
    if(this.isNullOrEmpty()) return false
    return try {
        JSONArray(this)
        true
    }catch (e: JSONException){
        false
    }
}