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

    override fun initView() {
    }
    override fun initData() {
//        LogUtils.eTag("tag", "${javaClass.simpleName} initData")
        postDelay(500){stateLayout?.showContent()}
    }



}