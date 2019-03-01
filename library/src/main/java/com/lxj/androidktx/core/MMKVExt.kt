package com.lxj.androidktx.core

import com.tencent.mmkv.MMKV
import java.util.concurrent.CopyOnWriteArraySet

/**
 * Description: MMKV的扩展
 * Create by lxj, at 2019/1/10
 */

fun Any.mmkv(id: String? = null) = if (id == null) MMKV.defaultMMKV() else MMKV.mmkvWithID(id)

val _set_divider = "_____androidktx_____"
/**
 * 将一个字符串添加到List中，能去重复，且有序。注意获取的时候要用：getStringList，
 * @param key key值
 * @param s 要添加的字符串
 * @param isReplace 是否去重，默认为true
 */
inline fun MMKV.addToList(key: String, a: Any, isReplace: Boolean = true) {
    val s = if(a is String) a else a.toJson()
    getStringSet(key, mutableSetOf())?.apply {
        val safeSet = CopyOnWriteArraySet<String>(this)
        if(isReplace){
            safeSet.forEach { el ->
                if (el.split(_set_divider)[0] == s) {
                    safeSet.remove(el)
                }
            }
        }
        safeSet.add("$s$_set_divider${System.nanoTime()}")
        putStringSet(key, safeSet)
    }
}


/**
 * 获取字符串列表
 */
inline fun <reified T> MMKV.getList(key: String): MutableList<T> {
    val srcSet = getStringSet(key, mutableSetOf())
    val list = srcSet!!.toSortedSet(comparator = Comparator { o1, o2 ->
        val arr1 = o1.split(_set_divider)
        val arr2 = o2.split(_set_divider)
        if (!o1.contains(_set_divider) || !o1.contains(_set_divider)) return@Comparator 0
        arr1[1].compareTo(arr2[1])
    }).map { it.split(_set_divider)[0] }
    return mutableListOf<T>().apply{
        list.forEach { add(it.toBean<T>()) }
    }
}


/**
 * 移除指定key的列表中的某个元素
 */
fun MMKV.removeFromList(key: String, el: Any) {
    val set = getStringSet(key, mutableSetOf())
    val safeSet = CopyOnWriteArraySet<String>(set)
    val target = if(el is String) el else el.toJson()
    safeSet.forEach { s ->
        if (s.split(_set_divider)[0] == target) {
            safeSet.remove(s)
        }
    }
    putStringSet(key, safeSet)
}