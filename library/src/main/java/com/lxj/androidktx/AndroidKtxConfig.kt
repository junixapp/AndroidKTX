package com.lxj.androidktx

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.lxj.androidktx.core.load
import com.lxj.androidktx.util.DirManager
import com.yuyh.library.imgsel.ISNav

/**
 * Description: 统一配置扩展方法中的变量
 * Create by lxj, at 2018/12/4
 */
@SuppressLint("StaticFieldLeak")
object AndroidKtxConfig {
    lateinit var context: Context
    var isDebug = true
    var defaultLogTag = "androidktx"
    var sharedPrefName = "androidktx"

    /**
     * 初始化配置信息，必须调用
     * @param isDebug 是否是debug模式，默认为true
     */
    fun init(context: Context,
             isDebug: Boolean = true,
             defaultLogTag: String = AndroidKtxConfig.defaultLogTag,
             sharedPrefName: String = AndroidKtxConfig.sharedPrefName
    ) {
        this.context = context
        this.isDebug = isDebug
        this.defaultLogTag = defaultLogTag
        this.sharedPrefName = sharedPrefName
        ISNav.getInstance().init { _, path, imageView -> imageView?.load(path) }
//        DirManager.init(context)
        if(context is Application){
            Utils.init(context)
        }
        ToastUtils.setGravity(Gravity.CENTER, 0 , 20)
        ToastUtils.setBgColor(Color.parseColor("#222222"))
        ToastUtils.setMsgColor(Color.WHITE)
    }
}