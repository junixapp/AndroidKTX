package com.lxj.androidktx

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils
import com.hjq.toast.Toaster
import com.lxj.androidktx.util.DirManager

/**
 * Description: 统一配置扩展方法中的变量
 * Create by lxj, at 2018/12/4
 */
@SuppressLint("StaticFieldLeak")
object AndroidKTX {

    lateinit var context: Context

    /**
     * 初始化配置信息，必须调用
     */
    fun init(context: Application) {
        this.context = context
        ToastUtils.getDefaultMaker().setGravity(Gravity.CENTER, 0 , 0)
        ToastUtils.getDefaultMaker().setBgResource(R.drawable._ktx_toast_bg)
        ToastUtils.getDefaultMaker().setTextColor(Color.WHITE)
        DirManager.init()
        Toaster.init(context)
    }

//    fun initRefresh() {
//        //设置全局的Header构建器
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
////            layout.setPrimaryColorsId(R.color.bg_color, R.color.colorPrimary)
//            layout.setPrimaryColors(Color.parseColor("#f0f0f0"), Color.parseColor("#111111"))
//            ClassicsHeader(context)
//        }
//        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
//            layout.setPrimaryColors(Color.parseColor("#f0f0f0"), Color.parseColor("#111111"))
//            ClassicsFooter(context)
//        }
//    }
}