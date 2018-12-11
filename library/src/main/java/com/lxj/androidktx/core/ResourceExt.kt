package com.lxj.androidktx.core

import com.lxj.androidktx.AndroidKtxConfig

/**
 * Description: 资源操作相关
 * Create by dance, at 2018/12/11
 */

fun Any.getColor(id: Int) = AndroidKtxConfig.context.resources.getColor(id)

fun Any.getString(id: Int) = AndroidKtxConfig.context.resources.getString(id)

fun Any.getStringArray(id: Int) = AndroidKtxConfig.context.resources.getStringArray(id)

fun Any.getDrawable(id: Int) = AndroidKtxConfig.context.resources.getDrawable(id)

fun Any.getDimensionPx(id: Int) = AndroidKtxConfig.context.resources.getDimensionPixelSize(id)