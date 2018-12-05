package com.lxj.androidktx.core

import com.lxj.androidktx.util.EncryptUtils

/**
 * Description: 哈希，加密相关
 * Create by lxj, at 2018/12/5
 */
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

