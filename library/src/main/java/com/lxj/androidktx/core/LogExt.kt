package com.lxj.androidktx.core

import android.util.Log
import com.lxj.androidktx.AndroidKtxConfig

/**
 * Description: log相关，日志的开关和默认tag通过AndroidKtxConfig来配置
 * Create by lxj, at 2018/12/5
 */

private enum class LogLevel{
    Verbose, Debug, Info, Warn, Error
}

@Deprecated(message = "推荐使用logv")
fun String.v(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Verbose, tag, this )
}

fun Any.logv(tag: String = AndroidKtxConfig.defaultLogTag, msg: String = ""){
    intervalLog(LogLevel.Verbose, tag, msg )
}

@Deprecated(message = "推荐使用logd")
fun String.d(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Debug, tag, this )
}

fun Any.logd(tag: String = AndroidKtxConfig.defaultLogTag, msg: String = ""){
    intervalLog(LogLevel.Debug, tag, msg )
}

@Deprecated(message = "推荐使用logi")
fun String.i(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Info, tag, this )
}

fun Any.logi(tag: String = AndroidKtxConfig.defaultLogTag, msg: String = ""){
    intervalLog(LogLevel.Info, tag, msg )
}

@Deprecated(message = "推荐使用logw")
fun String.w(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Warn, tag, this )
}

fun Any.logw(tag: String = AndroidKtxConfig.defaultLogTag, msg: String = ""){
    intervalLog(LogLevel.Warn, tag, msg )
}

@Deprecated(message = "推荐使用loge")
fun String.e(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Error, tag, this )
}

fun Any.loge(tag: String = AndroidKtxConfig.defaultLogTag, msg: String = ""){
    intervalLog(LogLevel.Error, tag, msg )
}

private fun intervalLog(level: LogLevel, tag: String, msg: String){
    if(AndroidKtxConfig.isDebug){
        when (level) {
            LogLevel.Verbose -> Log.v(tag, msg)
            LogLevel.Debug -> Log.d(tag, msg)
            LogLevel.Info -> Log.i(tag, msg)
            LogLevel.Warn -> Log.w(tag, msg)
            LogLevel.Error -> Log.e(tag, msg)
        }
    }
}
