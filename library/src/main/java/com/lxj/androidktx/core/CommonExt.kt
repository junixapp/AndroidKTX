package com.lxj.androidktx.core


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.lxj.androidktx.AndroidKTX
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.Serializable
import java.util.*


/**
 * Description:  通用扩展
 * Create by dance, at 2018/12/5
 */

/** dp和px转换 **/
fun Context.dp2px(dpValue: Float): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    return (pxValue / resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.sp2px(spValue: Float): Int {
    return (spValue * resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    return (pxValue / resources.displayMetrics.scaledDensity + 0.5f).toInt()
}


fun Fragment.dp2px(dpValue: Float): Int {
    return context!!.dp2px(dpValue)
}

fun Fragment.px2dp(pxValue: Float): Int {
    return context!!.px2dp(pxValue)
}

fun Fragment.sp2px(dpValue: Float): Int {
    return context!!.sp2px(dpValue)
}

fun Fragment.px2sp(pxValue: Float): Int {
    return context!!.px2sp(pxValue)
}


fun View.px2dp(pxValue: Float): Int {
    return context!!.px2dp(pxValue)
}

fun View.dp2px(dpValue: Float): Int {
    return context!!.dp2px(dpValue)
}

fun View.sp2px(dpValue: Float): Int {
    return context!!.sp2px(dpValue)
}

fun View.px2sp(pxValue: Float): Int {
    return context!!.px2sp(pxValue)
}

fun RecyclerView.ViewHolder.px2dp(pxValue: Float): Int {
    return itemView.px2dp(pxValue)
}

fun RecyclerView.ViewHolder.dp2px(dpValue: Float): Int {
    return itemView.dp2px(dpValue)
}

fun RecyclerView.ViewHolder.sp2px(dpValue: Float): Int {
    return itemView.sp2px(dpValue)
}

fun RecyclerView.ViewHolder.px2sp(pxValue: Float): Int {
    return itemView.px2sp(pxValue)
}

/** 动态创建Drawable **/
fun Context.createDrawable(color: Int = Color.TRANSPARENT, radius: Float = 0f,
                           strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
                           enableRipple: Boolean = true,
                           rippleColor: Int = Color.parseColor("#88999999"),
                           gradientStartColor: Int = 0, gradientEndColor : Int = 0, gradientOrientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT): Drawable {
    val content = GradientDrawable().apply {
        cornerRadius = radius
        setStroke(strokeWidth, strokeColor)
        gradientType = GradientDrawable.LINEAR_GRADIENT
        if(gradientStartColor!=0 || gradientEndColor!=0){
            orientation = gradientOrientation
            colors = intArrayOf(gradientStartColor, gradientEndColor)
        }else{
            setColor(color)
        }
    }
    if (Build.VERSION.SDK_INT >= 21 && enableRipple) {
        return RippleDrawable(ColorStateList.valueOf(rippleColor), content, null)
    }
    return content
}

fun Fragment.createDrawable(color: Int = Color.TRANSPARENT, radius: Float = 0f,
                            strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
                            enableRipple: Boolean = true,
                            rippleColor: Int = Color.parseColor("#88999999"),
                            gradientStartColor: Int = 0, gradientEndColor : Int = 0, gradientOrientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT): Drawable {
    return context!!.createDrawable(color, radius, strokeColor, strokeWidth, enableRipple, rippleColor, gradientStartColor = gradientStartColor,
    gradientEndColor = gradientEndColor, gradientOrientation = gradientOrientation)
}

fun View.createDrawable(color: Int = Color.TRANSPARENT, radius: Float = 0f,
                        strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
                        enableRipple: Boolean = true,
                        rippleColor: Int = Color.parseColor("#88999999"),
                        gradientStartColor: Int = 0, gradientEndColor : Int = 0, gradientOrientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT): Drawable {
    return context!!.createDrawable(color, radius, strokeColor, strokeWidth, enableRipple, rippleColor, gradientStartColor = gradientStartColor,
            gradientEndColor = gradientEndColor, gradientOrientation = gradientOrientation)
}

/** json相关 **/
fun Any.toJson(dateFormat: String = "yyyy-MM-dd HH:mm:ss", lenient: Boolean = false)
        = GsonBuilder().setDateFormat(dateFormat)
        .apply {
            if(lenient) setLenient()
        }
        .create().toJson(this)

inline fun <reified T> String.toBean(dateFormat: String = "yyyy-MM-dd HH:mm:ss", lenient: Boolean = false)
        = GsonBuilder().setDateFormat(dateFormat)
        .apply {
            if(lenient) setLenient()
        }.create()
        .fromJson<T>(this, object : TypeToken<T>() {}.type)

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


fun Any.runOnUIThread(action: () -> Unit) {
    Handler(Looper.getMainLooper()).post { action() }
}

/**
 * 将Bitmap保存到相册
 */
fun Bitmap.saveToAlbum(format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG, quality: Int = 100, filename: String = "", callback: ((path: String?, uri: Uri?) -> Unit)? = null) {
    GlobalScope.launch {
        try {
            //1. create path
            val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/" + Environment.DIRECTORY_PICTURES
            val dirFile = File(dirPath)
            if (!dirFile.exists()) dirFile.mkdirs()
            val ext = when (format) {
                Bitmap.CompressFormat.PNG -> ".png"
                Bitmap.CompressFormat.JPEG -> ".jpg"
                Bitmap.CompressFormat.WEBP -> ".webp"
            }
            val target = File(dirPath, (if (filename.isEmpty()) System.currentTimeMillis().toString() else filename) + ext)
            if (target.exists()) target.delete()
            target.createNewFile()
            //2. save
            compress(format, quality, FileOutputStream(target))
            //3. notify
            MediaScannerConnection.scanFile(AndroidKTX.context, arrayOf(target.absolutePath),
                    arrayOf("image/$ext")) { path, uri ->
                runOnUIThread {
                    callback?.invoke(path, uri)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            runOnUIThread { callback?.invoke(null, null) }
        }
    }
}

//一天只做一次
fun Any.doOnceInDay(actionName: String = "", action: () -> Unit, whenHasDone: (()->Unit)? = null) {
    val key = "once_in_day_last_check_${actionName}"
    val today = Date()
    val todayFormat = TimeUtils.date2String(today, "yyyy-MM-dd")
    val last = sp().getString(key, "")
    if (last != null && last.isNotEmpty() && last == todayFormat) {
        //说明执行过
        whenHasDone?.invoke()
        return
    }
    sp().putString(key, todayFormat)
    action()
}

//只执行一次的行为
fun Any.doOnlyOnce(actionName: String = "", action: () -> Unit, whenHasDone: (()->Unit)? = null) {
    val key = "has_done_${actionName}"
    val hasDone = sp().getBoolean(key, false)
    if (hasDone) {
        //说明执行过
        whenHasDone?.invoke()
        return
    }
    sp().putBoolean(key, true)
    action()
}

//500毫秒内只做一次
val _innerHandler = Handler(Looper.getMainLooper())
val _actionCache = arrayListOf<String>()

/**
 * 事件节流
 * @param actionName 事件的名字
 * @param time 事件的节流时间
 * @param action 事件
 */
fun Any.doOnceIn( actionName: String, time: Long = 500, action: ()->Unit){
    if(_actionCache.contains(actionName)) return
    _actionCache.add(actionName)
    action() //执行行为
    _innerHandler.postDelayed({
        if(_actionCache.contains(actionName)) _actionCache.remove(actionName)
    }, time)
}