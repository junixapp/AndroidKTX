package com.lxj.androidktxdemo.fragment

import com.lxj.androidktxdemo.R
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
        }
    }
}
