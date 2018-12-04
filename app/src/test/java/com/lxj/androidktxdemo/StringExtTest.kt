package com.lxj.androidktxdemo

import com.lxj.androidktx.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class StringExtTest {
    @Test
    fun testHashAndEcnrypt() {
        val str = "123456"
        val salt = "androidktx"
        println("md5: ${str.md5()}")
        println("sha1: ${str.sha1()}")
        println("sha256: ${str.sha256()}")
        println("sha512: ${str.sha512()}")
        println("md5Hmac: ${str.md5Hmac(salt = salt)}")
        println("sha1Hmac: ${str.sha1Hmac(salt = salt)}")
        println("sha256Hmac: ${str.sha256Hmac(salt = salt)}")

        val key = "aaaabbbb" //只能是8位
        val result = str.encryptDES(key)
        println("DES加密结果：$result")
        println("DES解密结果：${result.decryptDES(key)}")
    }

}
