package com.lxj.androidktx

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import com.lxj.androidktx.util.EncryptUtils

/**
 * Description: 字符串相关扩展
 * Create by lxj, at 2018/12/4
 */

//******************************* hash，加密，解密相关 *************************************
fun String.md5() = EncryptUtils.encryptMD5ToString(this)

fun String.sha1() = EncryptUtils.encryptSHA1ToString(this)

fun String.sha256() = EncryptUtils.encryptSHA256ToString(this)

fun String.sha512() = EncryptUtils.encryptSHA512ToString(this)

/**
 * 随机数增强的md5算法
 * @param salt 加盐的值
 */
fun String.md5Hmac(salt: String) = EncryptUtils.encryptHmacMD5ToString(this, salt)

fun String.sha1Hmac(salt: String) = EncryptUtils.encryptHmacSHA1ToString(this, salt)

fun String.sha256Hmac(salt: String) = EncryptUtils.encryptHmacSHA256ToString(this, salt)

/**
 * DES对称加密
 * @param key 长度必须是8位
 */
fun String.encryptDES(key: String) = EncryptUtils.encryptDES(this, key)

/**
 * DES对称解密
 * @param key 长度必须是8位
 */
fun String.decryptDES(key: String) = EncryptUtils.decryptDES(this, key)



//******************************* span相关 *************************************
/**
 * 将一段文字中指定range的文字改变大小
 * @param range 要改变大小的文字的范围
 * @param scale 缩放值，大于1，则比其他文字大；小于1，则比其他文字小；默认是1.5
 */
fun String.toSizeSpan(range: IntRange, scale: Float = 1.5f): SpannableString {
    return SpannableString(this).apply {
        setSpan(RelativeSizeSpan(scale), range.start, range.endInclusive, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

/**
 * 将一段文字中指定range的文字改变前景色
 * @param range 要改变前景色的文字的范围
 * @param color 要改变的颜色，默认是红色
 */
fun String.toColorSpan(range: IntRange, color: Int = Color.RED): SpannableString {
    return SpannableString(this).apply {
        setSpan(ForegroundColorSpan(color), range.start, range.endInclusive, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

/**
 * 将一段文字中指定range的文字改变背景色
 * @param range 要改变背景色的文字的范围
 * @param color 要改变的颜色，默认是红色
 */
fun String.toBackgroundColorSpan(range: IntRange, color: Int = Color.RED): SpannableString {
    return SpannableString(this).apply {
        setSpan(BackgroundColorSpan(color), range.start, range.endInclusive, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

/**
 * 将一段文字中指定range的文字添加删除线
 * @param range 要添加删除线的文字的范围
 */
fun String.toStrikethrougthSpan(range: IntRange): SpannableString {
    return SpannableString(this).apply {
        setSpan(StrikethroughSpan(), range.start, range.endInclusive, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

