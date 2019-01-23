package com.lxj.androidktx.core

import com.tencent.mmkv.MMKV

/**
 * Description: MMKV的扩展
 * Create by lxj, at 2019/1/10
 */

fun Any.mmkv(id: String? = null) = if(id==null)MMKV.defaultMMKV() else MMKV.mmkvWithID(id)

/**
 * 将一个字符串添加到Set中
 * @param key key值
 * @param s 要添加的字符串
 */
fun MMKV.addToSet(key: String, s: String){
    getStringSet(key, mutableSetOf())?.apply {
        add(s)
        putStringSet(key, this)
    }
}
