package com.lxj.androidktx.base

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.BarUtils
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.gone
import com.lxj.androidktx.core.height
import com.lxj.androidktx.core.visible
import com.lxj.androidktx.widget.TitleBar

abstract class TitleBarActivity : AdaptActivity(){
    protected val fakeTitleBar : View by lazy { findViewById(R.id.fakeTitleBar) }
    protected val flBody : ViewGroup by lazy { findViewById(R.id.flBody) }
    protected val titleBar : TitleBar by lazy { findViewById(R.id.titleBar) }
    protected val titleDivider : View by lazy { findViewById(R.id.titleDivider) }
    override fun getLayoutId(): Int {
        return R.layout._ktx_activity_titlebar
    }

    override fun initView() {
        fakeTitleBar.height(BarUtils.getStatusBarHeight())
        flBody.addView(View.inflate(this, getBodyLayout(), null))
        titleBar.leftImageView().click { finish() }
    }

    fun setStatusBarColor(color: Int) = fakeTitleBar.setBackgroundColor(color)

    fun hideTitleDivider() = titleDivider.gone()

    abstract fun getBodyLayout(): Int

    fun hideTitleBar(){
        titleBar.gone()
        fakeTitleBar.gone()
        titleDivider.gone()
    }
    fun hideTitleBarWithMarginTop(){
        titleBar.gone()
        titleDivider.gone()
    }

    fun showTitleBar(){
        titleBar.visible()
        fakeTitleBar.visible()
        titleDivider.visible()
    }
}