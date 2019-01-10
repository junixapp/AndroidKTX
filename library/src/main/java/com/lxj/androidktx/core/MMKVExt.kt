package com.lxj.androidktx.core

import com.tencent.mmkv.MMKV

/**
 * Description: MMKV的扩展
 * Create by lxj, at 2019/1/10
 */

fun Any.mmkv(id: String? = null) = if(id==null)MMKV.defaultMMKV() else MMKV.mmkvWithID(id)
