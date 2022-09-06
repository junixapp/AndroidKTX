package com.lxj.androidktx.picker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
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
import kotlinx.coroutines.GlobalScope
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

    private var tempPhotoFile: File? = null
    fun startCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.action = MediaStore.ACTION_IMAGE_CAPTURE_SECURE
        tempPhotoFile = File(DirManager.tempDir, "${System.currentTimeMillis()}.jpg")
        FileUtils.createFileByDeleteOldFile(tempPhotoFile)
        val uri: Uri =
            FileProvider.getUriForFile(this, "${packageName}.fileprovider", tempPhotoFile!!)
        val resInfoList =
            packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri) //Uri.fromFile(tempFile)
        startActivityForResult(cameraIntent, _cameraCode)

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
        cropImageFile = File(DirManager.tempDir, "${System.currentTimeMillis()}_crop.jpg")
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
        val intent = Intent()
        intent.putParcelableArrayListExtra("result", list)
        setResult(Activity.RESULT_OK, intent)
        finish()
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
//                if(Build.VERSION.SDK_INT >= 29){
//                    uriList.forEach {
//                        //相册在android11之后只能以uri方式访问，为了统一返回路径，需要拷贝的有权限的目录
//                        val cursor = context.contentResolver.query(it, null, null, null, null)
//                        if(cursor!=null && cursor.moveToFirst()){
//                            val ois = context.contentResolver.openInputStream(it) ?:return@forEach
//                            val displayName =
//                                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
//                            val file = File(DirManager.cacheDir, "${System.currentTimeMillis()}$displayName")
//                            val fos = FileOutputStream(file)
//                            android.os.FileUtils.copy(ois, fos)
//                            fos.close()
//                            ois.close()
//                            list.add(file.absolutePath)
//                        }
//                    }
//                }else{
//                }
                if (list.isEmpty()) {
                    finish()
                    return
                }
                if (pickerData!!.isCrop) {
                    //裁剪
                    startCrop(uri = list[0])
                } else {
                    //看看是否需要压缩
                    tryCompressImgs(list)
                }
            } else if (requestCode == _cameraCode) {
                list.clear()
                list.add(UriUtils.file2Uri(tempPhotoFile!!))
                //拍照
                if (pickerData!!.isCrop) {
                    //裁剪
                    if (tempPhotoFile == null) return
                    startCrop(UriUtils.file2Uri(tempPhotoFile!!))
                } else {
                    //看看是否需要压缩
                    tryCompressImgs(list)
                }
            } else if (requestCode == _cropCode && data != null) {
                val cropUri = UCrop.getOutput(data)
                if (cropUri == null) {
                    toast("crop failed")
                    finish()
                    return
                }
                list.clear()
                list.add(cropUri)
                tryCompressImgs(list)
            } else {
                finish()
            }
        } else {
            finish()
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