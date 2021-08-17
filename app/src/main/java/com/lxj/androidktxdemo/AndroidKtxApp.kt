package com.lxj.androidktxdemo

import androidx.multidex.MultiDexApplication
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.entity.User

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
class AndroidKtxApp: MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
//        AndroidKtxConfig.init(this)
        AndroidKTX.init(context = this,
                isDebug = BuildConfig.DEBUG,
                defaultLogTag = "androidktx",
                sharedPrefName = "demo")


//        "sss".e()

        val user = sp().getObject<User>("user")
        LogUtils.d("user: ${user}")
        sp().putObject("user", User(name = "李晓俊", age = 1000))
        LogUtils.d("user: ${sp().getObject<User>("user")}")


    }
}