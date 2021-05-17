package com.lxj.androidktxdemo.fragment

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.base.StateFragment
import com.lxj.androidktx.core.postDelay

/**
 * Description:
 * Create by lxj, at 2018/12/4
 */
abstract class BaseFragment: StateFragment(){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun initView() {
    }
    override fun initData() {
        postDelay(300){stateLayout?.showContent()}
    }

}