package com.lxj.androidktxdemo

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.base.BaseFragment
import kotlinx.android.synthetic.main.adapter_pager2.*

class DemoFragment(var position: Int): BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        LogUtils.e("onViewCreated $position")
    }

    override fun onStart() {
        super.onStart()
        LogUtils.e("onStart $position")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.e("onResume $position")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.e("onPause $position")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.e("onStop $position")

    }

    override fun getLayoutId() = R.layout.adapter_pager2

    override fun initView() {
        text.text = "${position}"
    }

    override fun initData() {
    }
}