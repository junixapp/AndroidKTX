package com.lxj.androidktx

import android.annotation.SuppressLint
import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * Description: 统一配置扩展方法中的变量
 * Create by lxj, at 2018/12/4
 */
@SuppressLint("StaticFieldLeak")
object AndroidKtxConfig {
    lateinit var context: Context
    var isDebug = true
    var defaultLogTag = "androidktx"
    var sharedPrefName = "androidktx"

    /**
     * 初始化配置信息，必须调用
     * @param isDebug 是否是debug模式，默认为true
     */
    fun init(context: Context,
             isDebug: Boolean = true,
             defaultLogTag: String = AndroidKtxConfig.defaultLogTag,
             sharedPrefName: String = AndroidKtxConfig.sharedPrefName
    ) {
        this.context = context
        this.isDebug = isDebug
        this.defaultLogTag = defaultLogTag
        this.sharedPrefName = sharedPrefName
        MMKV.initialize(this.context)
    }
}