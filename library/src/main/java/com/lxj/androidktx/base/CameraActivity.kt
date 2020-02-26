package com.lxj.androidktx.base

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.cjt2325.cameralibrary.JCameraView
import com.cjt2325.cameralibrary.listener.ErrorListener
import com.cjt2325.cameralibrary.listener.JCameraListener
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lxj.androidktx.R
import kotlinx.android.synthetic.main._ktx_activity_camera.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.io.FileOutputStream


class CameraActivity : AdaptActivity() {

    val videoDir =
        Environment.getExternalStorageDirectory().path + File.separator + packageName + File.separator + "_ktx_" + File.separator + "video"
    val tempDir =
        Environment.getExternalStorageDirectory().path + File.separator + packageName + File.separator + "_ktx_" + File.separator + "temp"

    companion object{
        const val CaptureVideo = "CaptureVideo"
        const val CaptureImage = "CaptureImage"
    }

    override fun getLayoutId() = R.layout._ktx_activity_camera

    override fun initData() {
    }

    override fun initView() {
        var dir = File(videoDir)
        if(!dir.exists())dir.mkdirs()
        dir = File(tempDir)
        if(!dir.exists())dir.mkdirs()

        jCameraView.setSaveVideoPath(videoDir)
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH)
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_LOW)
        jCameraView.setErrorLisenter(object : ErrorListener {
            override fun AudioPermissionError() {
                ToastUtils.showShort("录制权限获取失败，无法使用功能")
            }
            override fun onError() {
                ToastUtils.showShort("录制出错")
            }
        })

        jCameraView.setJCameraLisenter(object : JCameraListener {
            override fun recordSuccess(url: String?, firstFrame: Bitmap?) {
                //录制成功，路径
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(url)
                val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                LiveEventBus.get(CaptureVideo).post(mapOf(
                    "path" to url,
                    "duration" to duration
                ))
                LogUtils.d("录制成功：" + url +"  文件大小：${File(url).length()}  时长：${duration}")
                finish()
            }

            override fun captureSuccess(bitmap: Bitmap?) {
                val rawFile = File(tempDir, "${System.currentTimeMillis()}.png")
                if(!rawFile.exists()) rawFile.createNewFile()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(rawFile))
                //拍照成功，压缩
                Luban.with(this@CameraActivity)
                    .load(rawFile)
                    .ignoreBy(100)
                    .setTargetDir(tempDir)
                    .setCompressListener(object : OnCompressListener {
                        override fun onStart() {}
                        override fun onSuccess(file: File) {
                            LogUtils.d("压缩成功，文件大小：${file.length()}")
                            LiveEventBus.get(CaptureImage).post(file.absolutePath)
                            //删除旧的图片文件
                            rawFile.delete()
                            finish()
                        }
                        override fun onError(e: Throwable) {}
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