package com.lxj.androidktx.core


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