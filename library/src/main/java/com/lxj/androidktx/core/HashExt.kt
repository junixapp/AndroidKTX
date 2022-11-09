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
 * DES对称加密，输出的是Base64字符串
 * @param key 长度必须是8位
 */
fun String.encryptDESBase64(key: String) =
        Base64.encodeToString(
                EncryptUtils.encryptDES(this.toByteArray(), key.toByteArray(), "DES/CBC/PKCS5Padding", null),
                Base64.NO_WRAP
        )

fun String.encryptDESHex(key: String) = EncryptUtils.encryptDES2HexString(this.toByteArray(), key.toByteArray(), "DES/CBC/PKCS5Padding", null)


/**
 * DES对称解密Base64字符串
 * @param key 长度必须是8位
 */
fun String.decryptBase64DES(key: String) = String(EncryptUtils.decryptDES(Base64.decode(this, Base64.NO_WRAP), key.toByteArray(), "DES/CBC/PKCS5Padding", null))

fun String.decryptHexDES(key: String) = String(EncryptUtils.decryptHexStringDES(this, key.toByteArray(), "DES/CBC/PKCS5Padding", null))

/**
 * AES对称加密，输出的是Base64字符串
 * @param key 长度必须是16位
 */
fun String.encryptAESBase64(key: String) = Base64.encodeToString(
        EncryptUtils.encryptAES(this.toByteArray(), key.toByteArray(), "AES/ECB/PKCS5Padding", null),
        Base64.NO_WRAP
)

fun String.encryptAESHex(key: String) = EncryptUtils.encryptAES2HexString(this.toByteArray(), key.toByteArray(), "AES/ECB/PKCS5Padding", null)

/**
 * AES对称解密Base64字符串
 * @param key 长度必须是16位
 */
fun String.decryptBase64AES(key: String) = String(EncryptUtils.decryptAES(Base64.decode(this, Base64.NO_WRAP), key.toByteArray(), "AES/ECB/PKCS5Padding", null))
fun String.decryptHexAES(key: String) = String(EncryptUtils.decryptHexStringAES(this, key.toByteArray(), "AES/ECB/PKCS5Padding", null))

