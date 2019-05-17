package com.lxj.androidktx.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Description:
 * Create by dance, at 2019/5/16
 */
abstract class BaseActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initData()
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun initView()
    protected abstract fun initData()
}