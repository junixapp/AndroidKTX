package com.lxj.androidktx.core

import java.lang.IllegalArgumentException

/**
 * 把集合分成数量固定的几组
 */
fun <T> ArrayList<T>.groupByCount(count: Int = 1): List<List<T>>{
    if(count<1) throw IllegalArgumentException("count不能小于1")
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