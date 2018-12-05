package com.lxj.androidktxdemo

import android.support.test.runner.AndroidJUnit4
import com.lxj.androidktx.core.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
@RunWith(AndroidJUnit4::class)
class CommonExtTest {
    @Test
    fun test() {
        "${dp2px(100)}".v()
        "${px2dp(100)}".v()
        toast("测试短吐司")
        longToast("测试长吐司")
    }
}
