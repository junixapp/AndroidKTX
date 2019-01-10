package com.lxj.androidktxdemo

import android.app.Application
import com.lxj.androidktx.AndroidKtxConfig
import com.lxj.androidktx.core.mmkv

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
class AndroidKtxApp: Application(){
    override fun onCreate() {
        super.onCreate()
//        AndroidKtxConfig.init(this)
        AndroidKtxConfig.init(context = this,
                isDebug = BuildConfig.DEBUG,
                defaultLogTag = "androidktx",
                sharedPrefName = "demo")

//        mmkv(id = "user").putString("b", "cc")
//        mmkv().putString("a", "1231")
//        mmkv().getFloat("f", 123f)
//        mmkv().clearAll()
//        // 其他略过
    }
}