package com.lxj.androidktxdemo

import android.app.Application
import com.lxj.androidktx.AndroidKtxConfig

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
class AndroidKtxApp: Application(){
    override fun onCreate() {
        super.onCreate()
        AndroidKtxConfig.init(this)
    }
}