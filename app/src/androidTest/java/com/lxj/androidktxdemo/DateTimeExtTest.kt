package com.lxj.androidktxdemo


import android.support.test.runner.AndroidJUnit4
import com.lxj.androidktx.core.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DateTimeExtTest {
    @Test
    fun test() {
        Date().time.toDateString().v()
        // 默认格式：yyyy-MM-dd HH:mm:ss
        "2018-12-07 17:28:39".toDateMills().toString().v()
        (1544174919000L).toDateString().v()

        // 自定义格式
        "2018-12-07".toDateMills(format = "yyyy-MM-dd").toString().v()
        (1544174919000L).toDateString(format = "yyyy-MM-dd").v()
    }
}
