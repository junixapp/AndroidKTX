package com.lxj.androidktx.core

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description: 时间日期相关
 * Create by lxj, at 2018/12/7
 */

/**
 *  字符串日期格式（比如：2018-4-6)转为毫秒
 *  @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果您的格式不一样，则需要传入对应的格式
 */
fun String.toDateMills(format: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(format, Locale.getDefault()).parse(this).time


/**
 * Long类型时间戳转为字符串的日期格式
 * @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果您的格式不一样，则需要传入对应的格式
 */
fun Long.toDateString(format: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(format, Locale.getDefault()).format(Date(this))

fun Int.toDateString(format: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(format, Locale.getDefault()).format(Date(this.toLong()))

/**
 * 将毫秒值转为 hh:mm:ss 格式
 */
fun Long.toMediaTime(): String{
    val formatBuilder =  StringBuilder()
    val formatter =  Formatter(formatBuilder, Locale.getDefault())
    if (this < 0) {
        return "00:00"
    }
    val seconds = (this % DateUtils.MINUTE_IN_MILLIS) / DateUtils.SECOND_IN_MILLIS
    val minutes = (this % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS
    val hours = (this % DateUtils.DAY_IN_MILLIS) / DateUtils.HOUR_IN_MILLIS
    formatBuilder.setLength(0)
    if (hours > 0) {
        return formatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString()
    }
    return formatter.format("%02d:%02d", minutes, seconds).toString()
}