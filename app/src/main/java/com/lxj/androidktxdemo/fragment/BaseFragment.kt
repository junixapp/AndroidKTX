package com.lxj.androidktxdemo.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lxj.androidktx.*
import kotlinx.android.synthetic.main.fragment_span_ext.*

/**
 * Description:
 * Create by lxj, at 2018/12/4
 */
abstract class BaseFragment: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun getLayoutId():Int

    open fun initView(){
    }
}