package com.lxj.androidktx.base

import android.os.Bundle
import com.lxj.androidktx.core.postDelay
import com.lxj.statelayout.StateLayout
import kotlinx.android.synthetic.main._ktx_activity_titlebar.*

abstract class StateTitleBarActivity : TitleBarActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(autoShowContent()) postDelay(400){showContent()}
    }

    var stateLayout : StateLayout? = null
    override fun initView() {
        super.initView()
        stateLayout = StateLayout(this).wrap(flBody)
        onConfigStateLayout()
        stateLayout!!.showLoading()
    }
    /**
     * 用来对StateLayout进行各种配置
     */
    open fun onConfigStateLayout(){

    }

    open fun showContent() = stateLayout?.showContent()
    open fun showLoading() = stateLayout?.showLoading()
    open fun showError() = stateLayout?.showError()
    open fun showEmpty(noDataIconRes: Int = 0){
        if(noDataIconRes!=0){
            stateLayout?.showEmpty(noDataIconRes)
        }else{
            stateLayout?.showEmpty()
        }
    }

    //是否自动显示Content
    open fun autoShowContent() = false

}