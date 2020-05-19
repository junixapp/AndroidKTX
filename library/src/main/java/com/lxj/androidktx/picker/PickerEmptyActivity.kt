package com.lxj.androidktx.picker

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import com.lxj.androidktx.AndroidKtxConfig.context
import com.lxj.androidktx.R
import com.lxj.androidktx.widget.LoadingDialog
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import top.zibin.luban.Luban
import java.io.File

/**
 * 选择器的中转界面，本身是透明的，为了统一处理startActivityForResult
 */
class PickerEmptyActivity : AppCompatActivity() {
    val _pickerCode = 1000
    val _cameraCode = 1001
    val _cropCode = 1002
    var pickerData: _PickerData? = null
    lateinit var progressDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickerData = intent.getSerializableExtra("pickerData") as _PickerData?
        if (pickerData == null) {
            finish()
            return
        }
        progressDialog = LoadingDialog(this).setMessage("压缩中...")
        when (pickerData!!.action) {
            //选择器
            "picker" -> {
                Matisse.from(this).choose(pickerData!!.types)
                        .countable(true)
                        .maxSelectable(pickerData!!.maxNum)
                        .theme(R.style.Matisse_Dracula)
//                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
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
        if (cameraIntent.resolveActivity(packageManager) != null) {
            tempPhotoFile = File(createRootPath() + "/" + System.currentTimeMillis() + ".jpg")
            FileUtils.createFileByDeleteOldFile(tempPhotoFile)
            val uri: Uri = FileProvider.getUriForFile(this, "$packageName.image_provider", tempPhotoFile!!)
            val resInfoList = packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri) //Uri.fromFile(tempFile)
            startActivityForResult(cameraIntent, _cameraCode)
        } else {
            ToastUtils.showShort("打开相机失败")
            finish()
        }
    }

    private fun createRootPath(): String? {
        var cacheRootPath: String? = ""
        cacheRootPath = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            // /sdcard/Android/data/<application package>/cache
            externalCacheDir.path
        } else {
            // /data/data/<application package>/cache
            cacheDir.path
        }
        return cacheRootPath
    }

    private var cropImageFile: File? = null
    private fun startCrop(uri: Uri){
        val options = UCrop.Options()
        options.setCompressionQuality(90)
        val primaryColor = Color.parseColor("#263237") //Matisse的主题色
        options.setToolbarColor(primaryColor)
        options.setToolbarWidgetColor(Color.WHITE)
        options.setStatusBarColor(Color.parseColor("#1D282C"))//Matisse的主题色
        options.setActiveControlsWidgetColor(primaryColor)
        options.setHideBottomControls(true)
        cropImageFile = File(context.cacheDir, System.nanoTime().toString() + "_crop.jpg")
        UCrop.of(uri, Uri.fromFile(cropImageFile))
                .withAspectRatio(1f, 1f)
                .withOptions(options)
                .start(this, _cropCode)
    }

    fun compressImage(path: String): Deferred<File> {
        val deferred = CompletableDeferred<File>()
        deferred.complete(
                Luban.with(context)
                        .load(path)
                        .ignoreBy(100)
                        .get()[0]
        )
        return deferred
    }

    private fun tryCompressImgs(list: ArrayList<String>){
        //如果未开启压缩，或者是视频，都直接返回原文件
        if(!pickerData!!.isCompress || pickerData!!.types.containsAll(MimeType.ofVideo())){
            finishWithResult(list)
            return
        }
        progressDialog.show()
        GlobalScope.launch {
            try {
                val compressPaths = arrayListOf<String>()
                list.map {
//                    LogUtils.e("压缩之前大小：${File(it).length()/1024}k")
                    val f = compressImage(it).await()
                    compressPaths.add(f.absolutePath)
//                    LogUtils.e("压缩后大小：${f.length()/1024}k")
                }
                finishWithResult(compressPaths)
            }catch (e: Exception){
                //如果发生异常，则返回原图
                finishWithResult(list)
            }finally {
                progressDialog.dismiss()
            }
        }
    }

    fun finishWithResult(list: ArrayList<String>){
        val intent = Intent()
        intent.putStringArrayListExtra("result", list)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog?.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            val list = arrayListOf<String>()
            if (requestCode == _pickerCode) {
                //打开照片和视频选择器
                val uriList = if(data!=null) Matisse.obtainResult(data) else listOf()
                uriList.forEach { list.add(UriUtils.uri2File(it).absolutePath) }
                if(pickerData!!.isCrop){
                    //裁剪
                    if(uriList.isEmpty())finish()
                    startCrop(uriList[0])
                }else{
                    //看看是否需要压缩
                    tryCompressImgs(list)
                }
            } else if (requestCode == _cameraCode) {
                //拍照
                if(pickerData!!.isCrop){
                    //裁剪
                    if(tempPhotoFile==null)return
                    startCrop(UriUtils.file2Uri(tempPhotoFile!!))
                }else{
                    //看看是否需要压缩
                    list.add(tempPhotoFile?.absolutePath ?: "")
                    tryCompressImgs(list)
                }
            }else if(requestCode == _cropCode){
                list.add(cropImageFile?.absolutePath ?: "")
                tryCompressImgs(list)
            }else{
                finish()
            }
        }else{
            finish()
        }
    }
}