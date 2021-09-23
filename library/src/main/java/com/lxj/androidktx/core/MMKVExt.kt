package com.lxj.androidktx.core

import android.os.Parcelable
import com.tencent.mmkv.MMKV


/**
 * Description: MMKV相关
 * Create by dance, at 2018/12/5
 */

fun Any.mmkv(id: String? = null) = if(id.isNullOrEmpty()) MMKV.defaultMMKV() else MMKV.mmkvWithID(id)

/**
 *  put系列
 */
fun MMKV.putString(key: String, value: String) = encode(key, value)

fun MMKV.putInt(key: String, value: Int) = encode(key, value)

fun MMKV.putBoolean(key: String, value: Boolean) = encode(key, value)

fun MMKV.putFloat(key: String, value: Float) = encode(key, value)

fun MMKV.putLong(key: String, value: Long) = encode(key, value)

fun MMKV.putStringSet(key: String, value: Set<String>) = encode(key, value)

/**
 *  get系列
 */
fun MMKV.getString(key: String, value: String) = decodeString(key, value)

fun MMKV.getInt(key: String, value: Int) = decodeInt(key, value)

fun MMKV.getBoolean(key: String, value: Boolean) = decodeBool(key, value)

fun MMKV.getFloat(key: String, value: Float) = decodeFloat(key, value)

fun MMKV.getLong(key: String, value: Long) = decodeLong(key, value)

fun MMKV.getStringSet(key: String, value: Set<String>) = decodeStringSet(key, value)


/**
 * 对象操作，速度一般，无需实现Parcelable接口；如果存取场景不频繁，使用这个足够
 */
fun MMKV.putObject(key: String, obj: Any?) {
    encode(key, obj?.toJson()?:"")
}

inline fun <reified T> MMKV.getObject(key: String): T? {
    val string = getString(key, null)
    if(string==null || string.isEmpty())return null
    return decodeString(key, null)?.toBean<T>()
}

/**
 * 极速的对象操作，需要实现Parcelable接口；如果是频繁存取场景，推荐使用这个方法
 */
fun MMKV.putParcelable(key: String, value: Parcelable?) = encode(key, value)
inline fun <reified T : Parcelable> MMKV.getParcelable(key: String) = decodeParcelable(key, T::class.java)

fun MMKV.clear(key: String)  = removeValueForKey(key)
