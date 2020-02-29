package com.lxj.androidktx.util

import android.content.Intent
import android.graphics.Color
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.yuyh.library.imgsel.ISNav
import com.yuyh.library.imgsel.config.ISCameraConfig
import com.yuyh.library.imgsel.config.ISListConfig

object ImagePicker {
    var requestCode = 1
    /**
     * multiSelect和needCrop不能同时为true
     */
    fun start(source: Any, reqCode: Int = 1, multiSelect: Boolean = false,
              rememberSelected: Boolean = false,
              needCrop: Boolean = false,
              needCamera: Boolean = false,
              maxNum: Int = 9
    ) {
        val util = if(needCamera) PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
        else PermissionUtils.permission(PermissionConstants.STORAGE)
        util.callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        val config = ISListConfig.Builder() // 是否多选, 默认true
                                .multiSelect(multiSelect) // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                                .rememberSelected(rememberSelected) // “确定”按钮背景色
                                .btnBgColor(Color.WHITE) // “确定”按钮文字颜色
                                .btnTextColor(Color.parseColor("#333333")) // 使用沉浸式状态栏
                                .statusBarColor(Color.parseColor("#333333")) // 返回图标ResId
//                .backResId(androidx.R.drawable.abc_ic_ab_back_mtrl_am_alpha) // 标题
                                .titleColor(Color.WHITE) // TitleBar背景色
                                .titleBgColor(Color.parseColor("#333333"))
                                .needCrop(needCrop)
                                .needCamera(needCamera)
                                .maxNum(maxNum)
                                .build()
                        requestCode = reqCode
                        ISNav.getInstance().toListActivity(source, config, reqCode)
                    }
                    override fun onDenied() {
                        ToastUtils.showShort("权限获取失败，无法使用相册和拍照功能")
                    }
                })
                .request()

    }

    /**
     * 直接跳拍照
     */
    fun startCamera(source: Any, reqCode: Int = 1, needCrop: Boolean = false) {
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .callback(object : PermissionUtils.SimpleCallback {
            override fun onGranted() {
                val config = ISCameraConfig.Builder()
                        .needCrop(needCrop) // 裁剪
                        .build()
                requestCode = reqCode
                ISNav.getInstance().toCameraActivity(source, config, reqCode)
            }
            override fun onDenied() {
                ToastUtils.showShort("权限获取失败，无法使用拍照功能")
            }
        })
                .request()

    }

    /**
     * 获取照片结果
     */
    fun fetchResult(reqCode: Int, data: Intent?): List<String> {
        if (data != null && reqCode == requestCode) {
            return data.getStringArrayListExtra("result")
        }
        return listOf()
    }

    /**
     * 如果直接跳拍照，用这个方法获取结果
     */
    fun fetchFromCamera(reqCode: Int, data: Intent?): String? {
        if (data != null && reqCode == requestCode) {
            return data.getStringExtra("result")
        }
        return null
    }

}