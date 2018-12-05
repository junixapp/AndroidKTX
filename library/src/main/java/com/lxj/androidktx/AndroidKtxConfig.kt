package com.lxj.androidktx

/**
 * Description: 统一配置扩展方法中的变量
 * Create by lxj, at 2018/12/4
 */
object AndroidKtxConfig {
    var isDebug = true
    var defaultLogTag = "AndroidKTX"

    /**
     * 初始化配置信息
     * @param isDebug 是否是debug模式，默认为true
     */
    fun init(isDebug: Boolean = true,
             defaultLogTag: String = AndroidKtxConfig.defaultLogTag
    ) {
        this.isDebug = isDebug
        this.defaultLogTag = defaultLogTag
    }
}