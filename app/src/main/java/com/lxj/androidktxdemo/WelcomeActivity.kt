package com.lxj.androidktxdemo

import com.lxj.androidktx.base.BaseActivity
import com.lxj.androidktx.core.postDelay
import com.lxj.androidktx.core.startActivity

class WelcomeActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_welcome

    override fun initView() {
        supportActionBar?.hide()
        val fromNotification = intent?.getBooleanExtra("fromNotification", false)?:false
        postDelay(100){
            startActivity<MainActivity>()
            if(fromNotification) startActivity<DemoActivity>()
            finish()
        }
    }

    override fun initData() {
    }
}