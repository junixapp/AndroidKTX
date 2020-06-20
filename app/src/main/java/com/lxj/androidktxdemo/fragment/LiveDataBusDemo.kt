package com.lxj.androidktxdemo.fragment

import androidx.lifecycle.Observer
import com.lxj.androidktx.core.loge
import com.lxj.androidktx.core.observeState
import com.lxj.androidktx.core.postDelay
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.androidktxdemo.R
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.fragment_livedata_bus.*

/**
 * Description:
 * Create by dance, at 2019/1/8
 */
class LiveDataBusDemo : BaseFragment(){
    override fun getLayoutId() = R.layout.fragment_livedata_bus

    val sData = StateLiveData<String>()
    override fun initView() {
        super.initView()
        sData.state.observe(this, Observer {
            loge("it")
        })
        btnSendString.setOnClickListener {
            sData.postLoading()
            postDelay(1000){
                sData.postError("获取失败")
            }
//            "click".v()
        }
        XPopup.Builder(context).asLoading().observeState(this, sData, title = "自定义文字")
    }
}
