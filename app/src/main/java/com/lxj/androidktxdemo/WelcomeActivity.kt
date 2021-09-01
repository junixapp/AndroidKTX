package com.lxj.androidktxdemo

import com.lxj.androidktx.base.BaseActivity
import com.lxj.androidktx.core.postDelay
import com.lxj.androidktx.core.startActivity

class WelcomeActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_welcome

    override fun initView() {
        val fromNotification = intent?.getBooleanExtra("fromNotification", false)?:false
        postDelay(2000){
            startActivity<MainActivity>()
            if(fromNotification) startActivity<DemoActivity>()
            finish()
        }
    }

    override fun initData() {
    }
}