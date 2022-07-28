package com.lxj.androidktx.core

import com.blankj.utilcode.util.LogUtils

/**
 * 把集合分成数量固定的几组
 */
fun <T> ArrayList<T>.groupByCount(count: Int = 1): List<List<T>>{
    if(count<1){
        LogUtils.e("count less than 1")
        return arrayListOf<ArrayList<T>>()
    }
    val list = arrayListOf<ArrayList<T>>()
    var subList = arrayListOf<T>()
    forEach { t->
        subList.add(t)
        if(subList.size==count){
            list.add(subList)
            subList = arrayListOf<T>()
        }
    }
    //遍历结束，subList可能不满count
    if(subList.isNotEmpty()) list.add(subList)
    return list
}