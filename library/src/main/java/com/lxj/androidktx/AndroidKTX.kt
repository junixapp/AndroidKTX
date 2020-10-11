package com.lxj.androidktx

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * Description: 统一配置扩展方法中的变量
 * Create by lxj, at 2018/12/4
 */
@SuppressLint("StaticFieldLeak")
object AndroidKTX {
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
             defaultLogTag: String = AndroidKTX.defaultLogTag,
             sharedPrefName: String = AndroidKTX.sharedPrefName
    ) {
        this.context = context
        this.isDebug = isDebug
        this.defaultLogTag = defaultLogTag
        this.sharedPrefName = sharedPrefName
        if(context is Application){
            Utils.init(context)
        }
        ToastUtils.setGravity(Gravity.CENTER, 0 , 50)
//        ToastUtils.setBgColor(Color.parseColor("#222222"))
        ToastUtils.setBgResource(R.drawable._ktx_toast_bg)
        ToastUtils.setMsgColor(Color.WHITE)
        initRefresh()
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT)
    }

    fun initRefresh() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
//            layout.setPrimaryColorsId(R.color.bg_color, R.color.colorPrimary)
            layout.setPrimaryColors(Color.parseColor("#f1f1f1"), Color.parseColor("#111111"))
            ClassicsHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setPrimaryColors(Color.parseColor("#f1f1f1"), Color.parseColor("#111111"))
            ClassicsFooter(
                    context
            ).setDrawableSize(20f)
        }
    }
}