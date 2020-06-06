package com.lxj.androidktx.core

import android.util.Base64
import com.blankj.utilcode.util.EncryptUtils

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
fun String.encryptDES(key: String) =
        Base64.encodeToString(
                EncryptUtils.encryptDES(this.toByteArray(), key.toByteArray(), "DES/CBC/PKCS5Padding", null),
                Base64.NO_WRAP
        )


/**
 * DES对称解密
 * @param key 长度必须是8位
 */
fun String.decryptDES(key: String) = String(EncryptUtils.decryptDES(Base64.decode(this, Base64.NO_WRAP), key.toByteArray(), "DES/CBC/PKCS5Padding", null))

/**
 * AES对称加密
 * @param key 长度必须是16位
 */
fun String.encryptAES(key: String) = Base64.encodeToString(
        EncryptUtils.encryptAES(this.toByteArray(), key.toByteArray(), "AES/ECB/PKCS5Padding", null),
        Base64.NO_WRAP
)

/**
 * AES对称解密
 * @param key 长度必须是16位
 */
fun String.decryptAES(key: String) = String(EncryptUtils.decryptAES(Base64.decode(this, Base64.NO_WRAP), key.toByteArray(), "AES/ECB/PKCS5Padding", null))

