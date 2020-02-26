package com.lxj.androidktx.base

import android.view.View
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.gone
import com.lxj.androidktx.core.visible
import kotlinx.android.synthetic.main._ktx_activity_titlebar.*

abstract class TitleBarActivity : AdaptActivity(){
    override fun getLayoutId(): Int {
        return R.layout._ktx_activity_titlebar
    }

    override fun initView() {
        flBody.addView(View.inflate(this, getBodyLayout(), null))
        titleBar.leftImageView().click { finish() }
    }

    fun setStatusBarColor(color: Int) = fakeTitleBar.setBackgroundColor(color)

    fun titleBar() = titleBar

    fun hideTitleDivider() = titleDivider.gone()

    abstract fun getBodyLayout(): Int

    fun hideTitleBar(){
        titleBar().gone()
        fakeTitleBar.gone()
        titleDivider.gone()
    }
    fun showTitleBar(){
        titleBar().visible()
        fakeTitleBar.visible()
        titleDivider.visible()
    }
}