package com.lxj.androidktx.qrcode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.color
import kotlinx.android.synthetic.main._ktx_activity_qr_code.*

class QrCodeActivity : AppCompatActivity(), QRCodeView.Delegate {
    var openFlash = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._ktx_activity_qr_code)
        zxingView.setDelegate(this)
        var color = intent.getIntExtra("color", color(R.color.colorPrimary))
        zxingView.scanBoxView.cornerColor = color
        zxingView.scanBoxView.borderColor = color
//        zxingView.scanBoxView.scanLineColor = color  //无效
        btnBack.click { finish() }
        btnFlash.click {
            if(openFlash){
                zxingView.closeFlashlight()
            }else{
                zxingView.openFlashlight()
            }
            openFlash = !openFlash
            btnFlash.setImageResource(if(openFlash) R.mipmap._ktx_flash_open else R.mipmap._ktx_flash_close)
        }
    }

    override fun onStart() {
        super.onStart()
        zxingView.startSpotAndShowRect()
    }

    override fun onStop() {
        zxingView.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        zxingView.onDestroy()
        super.onDestroy()
    }

    override fun onScanQRCodeSuccess(result: String?) {
        val intent = Intent()
        intent.putExtra("result", result)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
    }

    override fun onScanQRCodeOpenCameraError() {
        ToastUtils.showLong("打开相机出错")
    }
}