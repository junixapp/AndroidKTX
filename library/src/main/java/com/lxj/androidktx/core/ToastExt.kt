package com.lxj.androidktx.core

import com.blankj.utilcode.util.ToastUtils

/**
 * 短吐司
 */
fun Any.toast(text: String?) {
    if(!text.isNullOrEmpty()) ToastUtils.showShort(text)
}

/**
 * 长吐司
 */
fun Any.toastLong(text: String?) {
    if(!text.isNullOrEmpty()) ToastUtils.showLong(text)
}