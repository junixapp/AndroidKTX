package com.lxj.androidktx.core


/**
 * Description: 字符串处理相关
 * Create by lxj, at 2018/12/7
 */

/**
 * 是否是手机号
 */
fun String.isPhone() = "(\\+\\d+)?1[3458]\\d{9}$".toRegex().matches(this)

/**
 * 是否是邮箱地址
 */
fun String.isEmail() = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?".toRegex().matches(this)

/**
 * 是否是身份证号码
 */
fun String.isIDCard() = "[1-9]\\d{16}[a-zA-Z0-9]".toRegex().matches(this)

/**
 * 是否是中文字符
 */
fun String.isChinese() = "^[\u4E00-\u9FA5]+$".toRegex().matches(this)
