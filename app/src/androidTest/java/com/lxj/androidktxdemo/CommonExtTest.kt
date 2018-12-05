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
class SharedPrefExtTest {
    @Test
    fun test() {
        putStringToSP("str", "哈哈")
        putIntToSP("int", 11)
        putBooleanToSP("bool", true)
        putFloatToSP("float", 11.22f)
        putLongToSP("long", 10000000000L)
        putStringSetToSP("stringset", setOf("a", "b", "c"))

        getStringFromSP("a")
    }
}
