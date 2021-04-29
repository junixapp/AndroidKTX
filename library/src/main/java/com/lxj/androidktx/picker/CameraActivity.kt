package com.lxj.androidktx.picker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.fragment.app.Fragment
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.cjt2325.cameralibrary.JCameraView
import com.cjt2325.cameralibrary.listener.ErrorListener
import com.cjt2325.cameralibrary.listener.JCameraListener
import com.lxj.androidktx.R
import com.lxj.androidktx.base.AdaptActivity
import com.lxj.androidktx.core.gone
import com.lxj.androidktx.core.margin
import com.lxj.androidktx.util.DirManager
import kotlinx.android.synthetic.main._ktx_activity_camera.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.io.FileOutputStream

/**
 * 视频录制
 */
class CameraActivity : AdaptActivity() {

    var mode = All //两种都拍

    companion object{
        const val All = "VideoMode" //拍视频
        const val OnlyVideo = "OnlyVideo" //拍视频
        const val OnlyImage = "OnlyImage" //拍照片

        fun start(from: Activity, requestCode: Int = 1, mode: String = All){
            PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.MICROPHONE,PermissionConstants.STORAGE)
                    .callback(object : PermissionUtils.SimpleCallback{
                        override fun onGranted() {
                            var intent = Intent(from, CameraActivity::class.java)
                            intent.putExtra("mode", mode)
                            from.startActivityForResult(intent, requestCode)
                        }
                        override fun onDenied() {
                            ToastUtils.showShort("权限获取失败，无法进行拍摄")
                        }
                    }).request()
        }
        fun start(from: Fragment, requestCode: Int = 1, mode: String = All){
            PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.MICROPHONE,PermissionConstants.STORAGE)
                    .callback(object : PermissionUtils.SimpleCallback{
                        override fun onGranted() {
                            var intent = Intent(from.activity, CameraActivity::class.java)
                            intent.putExtra("mode", mode)
                            from.startActivityForResult(intent, requestCode)
                        }
                        override fun onDenied() {
                            ToastUtils.showShort("权限获取失败，无法进行拍摄")
                        }
                    }).request()
        }
    }

    override fun getLayoutId() = R.layout._ktx_activity_camera

    override fun initData() {
    }

    override fun initView() {
        mode = intent.getStringExtra("mode")
        DirManager.init {
            jCameraView.findViewById<View>(com.cjt2325.cameralibrary.R.id.image_flash).gone()
            jCameraView.findViewById<View>(com.cjt2325.cameralibrary.R.id.image_switch).margin(topMargin = AdaptScreenUtils.pt2Px(30f))
            jCameraView.setSaveVideoPath(DirManager.tempDir)
            jCameraView.setFeatures(when(mode){
                All -> JCameraView.BUTTON_STATE_BOTH
                OnlyImage -> JCameraView.BUTTON_STATE_ONLY_CAPTURE
                else -> {
                    jCameraView.setTip("")
                    JCameraView.BUTTON_STATE_ONLY_RECORDER
                }
            })
            jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE)
        }

        jCameraView.setErrorLisenter(object : ErrorListener {
            override fun AudioPermissionError() {
                ToastUtils.showShort("录制权限获取失败，无法进行录制")
                finish()
            }
            override fun onError() {
                ToastUtils.showShort("录制出错")
                finish()
            }
        })

        jCameraView.setJCameraLisenter(object : JCameraListener {
            override fun recordSuccess(url: String?, firstFrame: Bitmap?) {
                val intent = Intent()
                intent.putExtra("path", url)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            override fun captureSuccess(bitmap: Bitmap?) {
                val rawFile = File(DirManager.tempDir, "${System.currentTimeMillis()}.png")
                if(!rawFile.exists()) rawFile.createNewFile()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(rawFile))
                //拍照成功，压缩
                Luban.with(this@CameraActivity)
                    .load(rawFile)
                    .ignoreBy(100)
                    .setTargetDir(DirManager.tempDir)
                    .setCompressListener(object : OnCompressListener {
                        override fun onStart() {}
                        override fun onSuccess(file: File) {
                            val intent = Intent()
                            intent.putExtra("path", file.absolutePath)
                            setResult(Activity.RESULT_OK, intent)
                            //删除旧的图片文件
                            rawFile.delete()
                            finish()
                        }
                        override fun onError(e: Throwable) {
                            val intent = Intent()
                            intent.putExtra("path", rawFile.absolutePath)
                            setResult(Activity.RESULT_OK, intent)
                            //删除旧的图片文件
                            rawFile.delete()
                            finish()
                        }
                    }).launch()
            }

        })
        jCameraView.setLeftClickListener {
            finish()
        }
        jCameraView.setRightClickListener {
            finish()
        }
    }

//    override fun onResume() {
//        super.onResume()
//        jCameraView.onResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        jCameraView.onPause()
//    }
}