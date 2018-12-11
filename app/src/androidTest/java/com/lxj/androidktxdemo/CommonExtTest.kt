package com.lxj.androidktxdemo

import android.support.test.runner.AndroidJUnit4
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.entity.User
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Description:
 * Create by dance, at 2018/12/5
 */

data class UserTest(
        var name: String,
        var age: Int
)
@RunWith(AndroidJUnit4::class)
class CommonExtTest {
    @Test
    fun test() {
        "${dp2px(100)}".v()
        "${px2dp(100)}".v()



    }
}
