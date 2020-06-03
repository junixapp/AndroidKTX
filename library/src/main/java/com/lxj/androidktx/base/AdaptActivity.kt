package com.lxj.androidktx.base

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils

/**
 * Description:
 * Create by dance, at 2019/5/16
 */
abstract class AdaptActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarLightMode(this,isLightMode())//显示黑字体
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT).setBackgroundResource(getStatusBarColor())
        super.onCreate(savedInstanceState)
    }

    override fun getResources(): Resources {
        if(ScreenUtils.isLandscape()){
            return AdaptScreenUtils.adaptWidth(super.getResources(), getDesignHeight())
        }
        return AdaptScreenUtils.adaptWidth(super.getResources(), getDesignWidth())
    }

    open fun getDesignWidth() = 375
    open fun getDesignHeight() = 750

    open fun isLightMode() = true

    open fun getStatusBarColor() = android.R.color.transparent


}