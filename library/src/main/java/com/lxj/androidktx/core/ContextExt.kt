package com.lxj.androidktx.core

import android.content.Context

/**
 * Description:  Context相关
 * Create by dance, at 2018/12/5
 */

fun Context.dp2px(dpValue: Float): Int{
    return  (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Int): Float{
    return pxValue / resources.displayMetrics.density + 0.5f
}