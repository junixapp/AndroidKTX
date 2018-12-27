package com.lxj.androidktx.okhttp

/**
 * Description:
 * Create by lxj, at 2018/12/27
 */
data class RequestWrapper(
        var tag: Any = OkWrapper.javaClass,
        var url: String = "",
        var headers: ArrayList<Pair<String, String>> = arrayListOf(),
        var parmas:  ArrayList<Pair<String, String>> = arrayListOf()

)

fun RequestWrapper.get(){

}