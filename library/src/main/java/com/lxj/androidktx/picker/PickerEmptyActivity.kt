package com.lxj.androidktx.picker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.*
import com.lxj.androidktx.AndroidKTX.context
import com.lxj.androidktx.R
import com.lxj.androidktx.core.toast
import com.lxj.androidktx.luban.Luban
import com.lxj.androidktx.util.DirManager
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.CaptureStrategy
import com.zhihu.matisse.internal.entity.IncapableCause
import com.zhihu.matisse.internal.entity.Item
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import java.io.File

/**
 * 选择器的中转界面，本身是透明的，为了统一处理startActivityForResult
 */
class PickerEmptyActivity : AppCompatActivity() {
    val _pickerCode = 1000
    val _cameraCode = 1001
    val _cropCode = 1002
    var pickerData: _PickerData? = null
    lateinit var loadingPopupView: LoadingPopupView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(window, Color.TRANSPARENT)
        pickerData = intent.getSerializableExtra("pickerData") as _PickerData?
        if (pickerData == null) {
            finish()
            return
        }
        loadingPopupView = XPopup.Builder(this)
            .asLoading().setStyle(LoadingPopupView.Style.ProgressBar)
        when (pickerData!!.action) {
            //选择器
            "picker" -> {
                Matisse.from(this).choose(pickerData!!.types)
                    .countable(true)
                    .capture(pickerData!!.showCapture)
                    .spanCount(pickerData!!.spanCount)
                    .captureStrategy(
                        CaptureStrategy(
                            true,
                            "${context.packageName}.fileprovider",
                            "picker"
                        )
                    )
                    .maxSelectable(pickerData!!.maxNum)
                    .theme(R.style.Matisse_Dracula)
                    .showSingleMediaType(true)
                    .addFilter(VideoSizeFilter(maxSize = if (pickerData!!.maxVideoSize > 0) pickerData!!.maxVideoSize else 20 * 1024 * 1024))
                    //.gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    //.thumbnailScale(0.6f)
                    .imageEngine(GlideEngine())
                    .showPreview(true) // Default is `true`
                    .forResult(_pickerCode)
            }
            "camera" -> {
                //打开相机
                startCamera()
            }
        }
    }

    private fun startCamera() {
        val intent = Intent(this, KTXCameraActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivityForResult(intent, _cameraCode)
    }

    private var cropImageFile: File? = null
    private fun startCrop(uri: Uri) {
        val options = UCrop.Options()
        options.setCompressionQuality(90)
        val primaryColor = Color.parseColor("#263237") //Matisse的主题色
        options.setToolbarColor(primaryColor)
        options.setToolbarWidgetColor(Color.WHITE)
        options.setStatusBarColor(Color.parseColor("#1D282C"))//Matisse的主题色
        options.setActiveControlsWidgetColor(primaryColor)
        options.setHideBottomControls(true)
        cropImageFile = File(DirManager.tempDir, "_ktx_${System.currentTimeMillis()}_crop.jpg")
        UCrop.of(uri, Uri.fromFile(cropImageFile))
            .withAspectRatio(1f, 1f)
            .withOptions(options)
            .start(this, _cropCode)
    }

    private fun compressImage(uri: Uri): Deferred<Uri> {
        val deferred = CompletableDeferred<Uri>()
        deferred.complete(
            UriUtils.file2Uri(
                Luban.with(context)
                    .load(UriUtils.uri2File(uri))
                    .ignoreBy(100)
                    .get()[0]
            )
        )
        return deferred
    }

    private fun tryCompressImgs(list: ArrayList<Uri>) {
        //如果未开启压缩，或者是视频，都直接返回原文件
        if (!pickerData!!.isCompress || pickerData!!.types.containsAll(MimeType.ofVideo())) {
            finishWithResult(list)
            return
        }

        loadingPopupView.show()
        lifecycleScope.launch {
            try {
                val compressUris = arrayListOf<Uri>()
                list.forEach {
                    val f = compressImage(it).await()
//                    FileUtils.delete(UriUtils.uri2File(it))
                    compressUris.add(f)
                }
                runOnUiThread {
                    loadingPopupView.delayDismissWith(300, Runnable {
                        finishWithResult(compressUris)
                    })
                }
            } catch (e: Exception) {
                //如果发生异常，则返回原图
                e.printStackTrace()
                runOnUiThread {
                    loadingPopupView.delayDismissWith(300, Runnable {
                        finishWithResult(list)
                    })
                }
            }
        }
    }

    fun finishWithResult(list: ArrayList<Uri>) {
        ImagePicker.onFinish?.invoke(list)
        val intent = Intent()
        intent.putParcelableArrayListExtra("result", list)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        finishWithResult(list = arrayListOf())
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var list = arrayListOf<Uri>()
            if (requestCode == _pickerCode && data != null) {
                //打开照片和视频选择器
                if (data != null) {
                    list.clear()
                    list.addAll(Matisse.obtainResult(data))
                }
                if (list.isEmpty()) {
                    finishWithResult(arrayListOf())
                    return
                }
                if (pickerData!!.isCrop) {
                    //裁剪
                    startCrop(uri = list[0])
                } else {
                    //看看是否需要压缩
                    tryCompressImgs(list)
                }
            } else if (requestCode == _cameraCode && data?.data != null) {
                list.clear()
                list.add(data.data!!)
                //拍照
                if (pickerData!!.isCrop) {
                    //裁剪
                    startCrop(uri = list[0])
                } else {
                    //看看是否需要压缩
                    tryCompressImgs(list)
                }
            } else if (requestCode == _cropCode && data != null) {
                val cropUri = UCrop.getOutput(data)
                if (cropUri == null) {
                    toast("crop failed")
                    finishWithResult(arrayListOf())
                    return
                }
                list.clear()
                list.add(cropUri)
                tryCompressImgs(list)
            } else {
                finishWithResult(arrayListOf())
            }
        } else {
            finishWithResult(arrayListOf())
        }
    }

    //上限是100M
    class VideoSizeFilter(var maxSize: Long = 1020 * 1024 * 1024) : Filter() {
        override fun filter(context: Context, item: Item): IncapableCause? {
            val size = UriUtils.uri2File(item.uri)?.length() ?: 0
            if (size > maxSize) {
                return IncapableCause(
                    IncapableCause.DIALOG,
                    "large than ${ConvertUtils.byte2FitMemorySize(maxSize, 0)}"
                )
            }
            return null
        }

        override fun constraintTypes(): MutableSet<MimeType> {
            return mutableSetOf(MimeType.MP4, MimeType.AVI, MimeType.MPEG)
        }

    }
}