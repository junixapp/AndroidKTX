package com.lxj.androidktxdemo.fragment

import com.lxj.androidktx.bus.LiveDataBus
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.v
import com.lxj.androidktxdemo.R
import com.lxj.androidktxdemo.entity.User
import kotlinx.android.synthetic.main.fragment_livedata_bus.*

/**
 * Description:
 * Create by dance, at 2019/1/8
 */
class LiveDataBusDemo : BaseFragment(){
    override fun getLayoutId() = R.layout.fragment_livedata_bus

    override fun initView() {
        super.initView()
        btnSendString.setOnClickListener {

//            "click".v()
            LiveDataBus.with<String>("key1").setValue("我是来自LiveDataBus的消息")
        }

        btnSendObject.setOnClickListener {
            "click btnSendObject".v()
            LiveDataBus.with<User>("key2").setValue(User("李晓俊", 2333))
        }
    }
}
