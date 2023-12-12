package com.lxj.androidktx.core

import com.blankj.utilcode.util.ToastUtils
import com.hjq.toast.Toaster

/**
 * 短吐司
 */
fun Any.toast(text: String?) {
    if(!text.isNullOrEmpty()) Toaster.show(text)
}

/**
 * 长吐司
 */
fun Any.toastLong(text: String?) {
    if(!text.isNullOrEmpty()) Toaster.showLong(text)
}