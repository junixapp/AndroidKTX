package com.lxj.androidktx.core

import android.os.SystemClock
import com.tencent.mmkv.MMKV

/**
 * Description: MMKV的扩展
 * Create by lxj, at 2019/1/10
 */

fun Any.mmkv(id: String? = null) = if (id == null) MMKV.defaultMMKV() else MMKV.mmkvWithID(id)

val _set_divider = "__________"
/**
 * 将一个字符串添加到List中，能去重复，且有序。注意获取的时候要用：getStringList，
 * @param key key值
 * @param s 要添加的字符串
 */
fun MMKV.addToList(key: String, s: String) {
    getStringSet(key, mutableSetOf())?.apply {
        add("$s$_set_divider${System.nanoTime()}")
        putStringSet(key, this)
    }
}

/**
 * 获取字符串列表
 */
fun MMKV.getStringList(key: String): List<String> {
    val srcSet = getStringSet(key, mutableSetOf())
    return srcSet!!.toSortedSet(comparator = Comparator { o1, o2 ->
        o1.split(_set_divider)[1].compareTo(o2.split(_set_divider)[1])
    }).map { it.split(_set_divider)[0] }
}


/**
 * 移除指定key的列表中的某个元素
 */
fun MMKV.removeFromList(key: String, el: String) {
    val set = getStringSet(key, mutableSetOf())
    set!!.forEach { s ->
        if (s.split(_set_divider)[0] == el) {
            set.remove(s)
        }
    }
    //update
    putStringSet(key, set)
}