package com.lxj.androidktxdemo


import android.support.test.runner.AndroidJUnit4
import com.lxj.androidktx.core.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LogExtTest {
    @Test
    fun test() {
        "我是测试".v()
        "我是测试".i()
        "我是测试".w()
        "我是测试".d()
        "我是测试".e()
    }
}
