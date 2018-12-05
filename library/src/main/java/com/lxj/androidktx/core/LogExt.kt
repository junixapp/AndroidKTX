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

fun String.v(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Verbose, tag, this )
}

fun String.d(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Debug, tag, this )
}
fun String.i(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Info, tag, this )
}
fun String.w(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Warn, tag, this )
}
fun String.e(tag: String = AndroidKtxConfig.defaultLogTag){
    intervalLog(LogLevel.Error, tag, this )
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
