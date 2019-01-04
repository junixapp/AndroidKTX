package com.lxj.androidktx.core

import android.content.Context
import com.lxj.androidktx.AndroidKtxConfig

/**
 * Description: SharedPreferences相关
 * Create by dance, at 2018/12/5
 */

fun Context.spEditor() = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .edit()

/**
 *  put系列
 */
fun Context.putStringToSP(key: String, value: String) {
    spEditor().putString(key, value).apply()
}
fun Context.putIntToSP(key: String, value: Int) {
    spEditor().putInt(key, value).apply()
}
fun Context.putBooleanToSP(key: String, value: Boolean) {
    spEditor().putBoolean(key, value).apply()
}
fun Context.putFloatToSP(key: String, value: Float) {
    spEditor().putFloat(key, value).apply()
}
fun Context.putLongToSP(key: String, value: Long) {
    spEditor().putLong(key, value).apply()
}
fun Context.putStringSetToSP(key: String, value: Set<String>) {
    spEditor().putStringSet(key, value).apply()
}

/**
 * get系列
 */
fun Context.getStringFromSP(key: String, defVal: String = "") = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getString(key, defVal)
fun Context.getIntFromSP(key: String, defVal: Int = 0) = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getInt(key, defVal)
fun Context.getBooleanFromSP(key: String, defVal: Boolean = false) = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getBoolean(key, defVal)
fun Context.getFloatFromSP(key: String, defVal: Float = 0f) = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getFloat(key, defVal)
fun Context.getLongFromSP(key: String, defVal: Long = 0L) = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getLong(key, defVal)
fun Context.getStringSetFromSP(key: String, defVal: Set<String> = setOf()) = AndroidKtxConfig.context.getSharedPreferences(AndroidKtxConfig.sharedPrefName, Context.MODE_PRIVATE)
        .getStringSet(key, defVal)

fun Context.clearSharedPref(){
    spEditor().clear().apply()
}