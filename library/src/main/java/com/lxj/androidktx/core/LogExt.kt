package com.lxj.androidktx.core

import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.AndroidKtxConfig

/**
 * Description: log相关，日志的开关和默认tag通过AndroidKtxConfig来配置
 * Create by lxj, at 2018/12/5
 */

private enum class LogLevel{
    Verbose, Debug, Info, Warn, Error
}

fun String.logv(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Verbose, tag, this )
}

fun Any.logv(tag: String, msg: String){
    intervalLog(LogLevel.Verbose, tag, msg )
}
fun Any.logv(msg: String){
    intervalLog(LogLevel.Verbose, AndroidKtxConfig.defaultLogTag, msg )
}

fun String.logd(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Debug, tag, this )
}

fun Any.logd(tag: String, msg: String){
    intervalLog(LogLevel.Debug, tag, msg )
}
fun Any.logd(msg: String){
    intervalLog(LogLevel.Debug, AndroidKtxConfig.defaultLogTag, msg )
}

fun String.logi(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Info, tag, this )
}

fun Any.logi(tag: String, msg: String){
    intervalLog(LogLevel.Info, tag, msg )
}
fun Any.logi(msg: String){
    intervalLog(LogLevel.Info, AndroidKtxConfig.defaultLogTag, msg )
}

fun String.logw(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Warn, tag, this )
}
fun Any.logw(tag: String, msg: String){
    intervalLog(LogLevel.Warn, tag, msg )
}
fun Any.logw(msg: String){
    intervalLog(LogLevel.Warn, AndroidKtxConfig.defaultLogTag, msg )
}

fun String.loge(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Error, tag, this )
}

fun Any.loge(tag: String, msg: String){
    intervalLog(LogLevel.Error, tag, msg )
}
fun Any.loge(msg: String){
    intervalLog(LogLevel.Error, AndroidKtxConfig.defaultLogTag, msg )
}

private fun intervalLog(level: LogLevel, tag: String, msg: String){
    if(AndroidKtxConfig.isDebug){
        when (level) {
            LogLevel.Verbose -> LogUtils.vTag(tag, msg)
            LogLevel.Debug -> LogUtils.dTag(tag, msg)
            LogLevel.Info -> LogUtils.iTag(tag, msg)
            LogLevel.Warn -> LogUtils.wTag(tag, msg)
            LogLevel.Error -> LogUtils.eTag(tag, msg)
        }
    }
}
