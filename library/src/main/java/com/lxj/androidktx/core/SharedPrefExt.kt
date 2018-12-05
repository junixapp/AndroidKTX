package com.lxj.androidktx.core

import android.content.Context
import com.lxj.androidktx.AndroidKtxConfig

/**
 * Description: SharedPreferences相关
 * Create by dance, at 2018/12/5
 */

fun Any.spEditor() = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .edit()

/**
 *  put系列
 */
fun Any.putStringToSP(key: String, value: String) {
    spEditor().putString(key, value).apply()
}
fun Any.putIntToSP(key: String, value: Int) {
    spEditor().putInt(key, value).apply()
}
fun Any.putBooleanToSP(key: String, value: Boolean) {
    spEditor().putBoolean(key, value).apply()
}
fun Any.putFloatToSP(key: String, value: Float) {
    spEditor().putFloat(key, value).apply()
}
fun Any.putLongToSP(key: String, value: Long) {
    spEditor().putLong(key, value).apply()
}
fun Any.putStringSetToSP(key: String, value: Set<String>) {
    spEditor().putStringSet(key, value).apply()
}

/**
 * get系列
 */
fun Any.getStringFromSP(key: String, defVal: String = "") = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getString(key, defVal)
fun Any.getIntFromSP(key: String, defVal: Int = 0) = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getInt(key, defVal)
fun Any.getBooleanFromSP(key: String, defVal: Boolean = false) = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getBoolean(key, defVal)
fun Any.getFloatFromSP(key: String, defVal: Float = 0f) = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getFloat(key, defVal)
fun Any.getLongFromSP(key: String, defVal: Long = 0L) = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getLong(key, defVal)
fun Any.getStringSetFromSP(key: String, defVal: Set<String> = setOf()) = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getStringSet(key, defVal)

fun Any.clearSharedPref(){
    spEditor().clear().apply()
}