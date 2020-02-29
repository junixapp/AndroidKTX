package com.lxj.androidktx.util

import android.content.Intent
import android.graphics.Color
import com.yuyh.library.imgsel.ISNav
import com.yuyh.library.imgsel.config.ISListConfig

object ImagePicker {
    var requestCode = 1
    fun start(source: Any, reqCode: Int = 1, multiSelect: Boolean = false,
              rememberSelected: Boolean = false,
              needCrop: Boolean = false,
              needCamera: Boolean = false,
              maxNum: Int = 9
    ) {
        val config = ISListConfig.Builder() // 是否多选, 默认true
                .multiSelect(multiSelect) // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(rememberSelected) // “确定”按钮背景色
                .btnBgColor(Color.WHITE) // “确定”按钮文字颜色
                .btnTextColor(Color.parseColor("#333333")) // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#333333")) // 返回图标ResId
//                .backResId(androidx.R.drawable.abc_ic_ab_back_mtrl_am_alpha) // 标题
                .titleColor(Color.WHITE) // TitleBar背景色
                .titleBgColor(Color.parseColor("#333333"))
                .needCrop(needCrop) // 第一个是否显示相机，默认true
                .needCamera(needCamera)
                .maxNum(maxNum)
                .build()
        requestCode = reqCode
        ISNav.getInstance().toListActivity(source, config, reqCode)
    }

    /**
     * 获取照片结果
     */
    fun fetchResult(reqCode: Int, data: Intent?): List<String> {
        if (data != null && reqCode === requestCode) {
            return data.getStringArrayListExtra("result")
        }
        return listOf()
    }

}