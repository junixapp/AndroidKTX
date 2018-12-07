package com.lxj.androidktx.core


import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.Gson
import com.lxj.androidktx.AndroidKtxConfig
import com.lxj.androidktx.util.NetworkUtils
import java.io.Serializable

/**
 * Description:  通用扩展
 * Create by dance, at 2018/12/5
 */

/** dp和px转换 **/
fun Any.dp2px(dpValue: Float): Int {
    return (dpValue * AndroidKtxConfig.context.resources.displayMetrics.density + 0.5f).toInt()
}
fun Any.dp2px(dpValue: Int): Int {
    return (dpValue * AndroidKtxConfig.context.resources.displayMetrics.density + 0.5f).toInt()
}
fun Any.px2dp(pxValue: Int): Float {
    return pxValue / AndroidKtxConfig.context.resources.displayMetrics.density + 0.5f
}
fun Any.px2dp(pxValue: Float): Float {
    return pxValue / AndroidKtxConfig.context.resources.displayMetrics.density + 0.5f
}

/** toast相关 **/
fun Any.toast(msg: CharSequence) {
    Toast.makeText(AndroidKtxConfig.context, msg, Toast.LENGTH_SHORT).show()
}
fun Any.longToast(msg: CharSequence) {
    Toast.makeText(AndroidKtxConfig.context, msg, Toast.LENGTH_LONG).show()
}

/** json相关 **/
fun Any.toJson() = Gson().toJson(this)

/** Window相关 **/
fun Any.windowWidth(): Int{
    val windowManager = AndroidKtxConfig.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.width
}

fun Any.windowHeight(): Int{
    val windowManager = AndroidKtxConfig.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.height
}

/** 网络相关 **/
/**
 * 当前网络是否有连接
 */
fun Any.isNetworkConnected() = NetworkUtils.isConnected()

/**
 * 当前是否是Wifi连接
 */
fun Any.isWifiConnected() = NetworkUtils.isWifiConnected()

/**
 * 当前是否是移动数据连接
 */
fun Any.isMobileConnected() = NetworkUtils.isMobileData()


/**
 * 数组转bundle
 */
fun Array<out Pair<String, Any?>>.toBundle(): Bundle? {
    return Bundle().apply {
        forEach { it ->
            val value = it.second
            when (value) {
                null -> putSerializable(it.first, null as Serializable?)
                is Int -> putInt(it.first, value)
                is Long -> putLong(it.first, value)
                is CharSequence -> putCharSequence(it.first, value)
                is String -> putString(it.first, value)
                is Float -> putFloat(it.first, value)
                is Double -> putDouble(it.first, value)
                is Char -> putChar(it.first, value)
                is Short -> putShort(it.first, value)
                is Boolean -> putBoolean(it.first, value)
                is Serializable -> putSerializable(it.first, value)
                is Parcelable -> putParcelable(it.first, value)

                is IntArray -> putIntArray(it.first, value)
                is LongArray -> putLongArray(it.first, value)
                is FloatArray -> putFloatArray(it.first, value)
                is DoubleArray -> putDoubleArray(it.first, value)
                is CharArray -> putCharArray(it.first, value)
                is ShortArray -> putShortArray(it.first, value)
                is BooleanArray -> putBooleanArray(it.first, value)

                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> putCharSequenceArray(it.first, value as Array<CharSequence>)
                    value.isArrayOf<String>() -> putStringArray(it.first, value as Array<String>)
                    value.isArrayOf<Parcelable>() -> putParcelableArray(it.first, value as Array<Parcelable>)
                }
            }
        }
    }

}