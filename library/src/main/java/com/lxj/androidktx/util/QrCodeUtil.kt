package com.lxj.androidktx.util

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.king.zxing.Intents
import com.lxj.androidktx.qrcode.CustomCaptureActivity

/**
 * 二维码工具类
 */
object QrCodeUtil {
    var requestCode = 1
    fun start(source: Any, reqCode: Int = 1, color: Int? = null){
        requestCode = reqCode
        PermissionUtils.permission(
                PermissionConstants.CAMERA,
                PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .callback(object: PermissionUtils.SimpleCallback{
                    override fun onGranted() {
                        if(source is Activity){
                            val intent = Intent(source, CustomCaptureActivity::class.java)
                            if(color!=null) intent.putExtra("color", color)
                            source.startActivityForResult(intent, reqCode)

                        }else if(source is Fragment){
                            val intent = Intent(source.context, CustomCaptureActivity::class.java)
                            if(color!=null) intent.putExtra("color", color)
                            source.startActivityForResult(intent, reqCode)
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