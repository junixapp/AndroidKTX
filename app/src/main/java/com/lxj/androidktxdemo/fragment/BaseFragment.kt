package com.lxj.androidktxdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.base.PagerLazyFragment

/**
 * Description:
 * Create by lxj, at 2018/12/4
 */
abstract class BaseFragment: PagerLazyFragment(){
    override fun initView() {
        LogUtils.e("${javaClass::getSimpleName} initView")
    }
    override fun initData() {
        LogUtils.e("${javaClass::getSimpleName} initData")

    }
}