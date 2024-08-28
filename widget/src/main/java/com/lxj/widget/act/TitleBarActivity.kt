package com.lxj.widget.act

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.lxj.ext.click
import com.lxj.ext.gone
import com.lxj.ext.height
import com.lxj.ext.visible
import com.lxj.widget.R
import com.lxj.widget.TitleBar

abstract class TitleBarActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarLightMode(this, isLightStatusBar())//显示黑字体
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT).setBackgroundResource(getStatusBarColor())
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initData()
    }

    override fun getResources(): Resources {
        if(ScreenUtils.isPortrait()){
            return AdaptScreenUtils.adaptWidth(super.getResources(), getDesignWidth())
        }
        return AdaptScreenUtils.adaptWidth(super.getResources(), getDesignHeight())
    }

    open fun getDesignWidth() = 375
    open fun getDesignHeight() = 750

    open fun isLightStatusBar() = true

    open fun getStatusBarColor() = android.R.color.transparent


    protected val fakeTitleBar : View by lazy { findViewById(R.id.fakeTitleBar) }
    protected val flBody : ViewGroup by lazy { findViewById(R.id.flBody) }
    protected val titleBar : TitleBar by lazy { findViewById(R.id.titleBar) }
    protected val titleDivider : View by lazy { findViewById(R.id.titleDivider) }
    open fun getLayoutId(): Int {
        return R.layout._ktx_activity_titlebar
    }

    open fun initView() {
        fakeTitleBar.height(BarUtils.getStatusBarHeight())
        flBody.addView(View.inflate(this, getBodyLayout(), null))
        titleBar.leftImageView().click { finish() }
    }
    protected abstract fun initData()


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