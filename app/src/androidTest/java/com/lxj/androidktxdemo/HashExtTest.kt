package com.lxj.androidktxdemo

import com.lxj.androidktx.*
import com.lxj.androidktx.core.*
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http  ////d.android.com/tools/testing).
 */
class HashExtTest {
    @Test
    fun testHashAndEcnrypt() {
        val str = """123456"""
        val salt = """androidktx"""

        """"$str".md5()  // ${str.md5()}""".d()
        """"$str".sha1()  // ${str.sha1()}""".d()
        """"$str".sha256()  // ${str.sha256()}""".d()
        """"$str".sha512()  // ${str.sha512()}""".d()
        """"$str".md5Hmac()  // ${str.md5Hmac(salt = salt)}""".d()
        """"$str".sha1Hmac()  // ${str.sha1Hmac(salt = salt)}""".d()
        """"$str".sha256Hmac()  // ${str.sha256Hmac(salt = salt)}""".d()

        val key = """aaaabbbb""" //只能是8位
        val result = str.encryptDES(key)
        """"$str".encryptDES($key)  //$result""".d()
        """"$result".decryptDES($key)  //${result.decryptDES(key)}""".d()
    }

}
