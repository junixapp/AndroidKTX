package com.lxj.androidktx

/**
 * Description: 统一配置扩展方法中的变量
 * Create by lxj, at 2018/12/4
 */
object AndroidKtxConfig {
    private var isDebug = true


    /**
     * 初始化配置信息
     * @param isDebug 是否是debug模式，默认为true
     */
    fun init(isDebug: Boolean = true
    ) {
        this.isDebug = isDebug
    }
}