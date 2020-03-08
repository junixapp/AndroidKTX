package com.lxj.androidktx.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.king.zxing.CaptureActivity
import com.king.zxing.Intents

/**
 * 二维码工具类
 */
object QrCodeUtil {
    var requestCode = 1
    fun start(source: Any, reqCode: Int = 1){
        requestCode = reqCode
        PermissionUtils.permission(
                PermissionConstants.CAMERA,
                PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .callback(object: PermissionUtils.SimpleCallback{
                    override fun onGranted() {
                        if(source is Activity){
                            source.startActivityForResult(Intent(source, CaptureActivity::class.java), reqCode)
                        }else if(source is Fragment){
                            source.startActivityForResult(Intent(source.context, CaptureActivity::class.java), reqCode)
                        }
                    }
                    override fun onDenied() {
                        ToastUtils.showShort("权限拒绝，无法使用扫描功能")
                    }
                }).request()
    }

    /**
     * 获取扫描结果
     */
    fun fetchResult(reqCode: Int, data: Intent?) : String{
        if (data != null && requestCode ==reqCode) {
            return data.getStringExtra(Intents.Scan.RESULT)
        }
        return ""
    }
}