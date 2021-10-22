package com.lxj.androidktx.base

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils

/**
 * Description: 使用的是AndroidUtilCode的pt适配，方式是宽高比等比放大的方式。
 * 该方式不适合平板，在平板上会显示的很大。如果不需要适配平板，可以调用：
 *      AdaptScreenUtils.closeAdapt(resources)
 *
 * 此时等同于dp适配
 * Create by dance, at 2019/5/16
 */
abstract class AdaptActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarLightMode(this, isLightStatusBar())//显示黑字体
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT).setBackgroundResource(getStatusBarColor())
        super.onCreate(savedInstanceState)
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


}