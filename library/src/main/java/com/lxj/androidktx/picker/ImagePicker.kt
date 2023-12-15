package com.lxj.androidktx.picker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import com.blankj.utilcode.util.UtilsTransActivity
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.R
import com.lxj.androidktx.core.startForResult
import com.lxj.androidktx.core.string
import com.lxj.androidktx.core.toast
import com.lxj.androidktx.util.UriHelper
import com.zhihu.matisse.MimeType
import java.io.Serializable

data class _PickerData(
        var action: String = "",
        var isCrop: Boolean = false,
        var isCompress: Boolean = true,
        var showCapture: Boolean = true,
        var maxNum: Int = 9,
        var spanCount: Int = 4,
        var maxVideoSize: Long = 0,
        var types: Set<MimeType> = MimeType.ofImage()
): Serializable

/**
 * 图片和视频选择器，整合了Matisse，uCrop和Luban。
 * 获取结果有两种方式（二选一），onFinish回调和onActivityResult
 */
object ImagePicker {
    var onFinish: ((List<Uri>)->Unit)? = null
    /**
     * @param reqCode 如果你想通过onActivityResult方法获取结果时就传递
     * @param isCrop 是否开启裁剪，默认false
     * @param isCompress 是否使用Luban压缩，默认是true
     */
    fun startCamera(from: Any, reqCode: Int = 1000, isCrop: Boolean = false, isCompress: Boolean = true,
                    onFinish: ((List<Uri>)->Unit)? = null) {
        this.onFinish = onFinish
        PermissionUtils
                .permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        if(from is Activity){
                            from.startForResult<PickerEmptyActivity>(bundle = arrayOf("pickerData" to _PickerData(
                                isCrop = isCrop, action = "camera", maxNum = 1, isCompress = isCompress
                            )), requestCode = reqCode)
                        }else if (from is Fragment){
                            from.startForResult<PickerEmptyActivity>(bundle = arrayOf("pickerData" to _PickerData(
                                isCrop = isCrop, action = "camera", maxNum = 1, isCompress = isCompress
                            )), requestCode = reqCode)
                        }
                    }
                    override fun onDenied() {
                        ToastUtils.showShort(AndroidKTX.context.string(R.string._ktx_permisison_deny))
                    }
                })
                .request()
    }

    /**
     * 直接开启选择器，选择器自带拍摄功能，注意：maxNum>1时和types包含video时无法使用裁剪；types包含video时无法使用压缩
     * @param reqCode 如果你想通过onActivityResult方法获取结果时就传递
     * @param isCrop 是否开启裁剪，默认false
     * @param isCompress 是否使用Luban压缩，默认是true
     * @param maxNum 最多选择数量，默认是1
     * @param types 选择的类型，默认是Image类型
     * @param maxVideoSize 最大视频大小，默认是20M
     * @param showCapture 是否显示拍照
     * @param spanCount
     */
    fun startPicker(from: Any, reqCode: Int = 2000, isCrop: Boolean = false, isCompress: Boolean = true,
                    maxNum: Int = 1, types: Set<MimeType> = MimeType.ofImage(),
                    maxVideoSize: Long = 0, showCapture: Boolean = true, spanCount: Int = 4,
                    onFinish: ((List<Uri>)->Unit)? = null) {
        this.onFinish = onFinish
        PermissionUtils
                .permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA)
            .rationale(object : PermissionUtils.OnRationaleListener{
                override fun rationale(
                    activity: UtilsTransActivity,
                    shouldRequest: PermissionUtils.OnRationaleListener.ShouldRequest
                ) {
                    toast("rationale")
                }
            })
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        val max = if(maxNum < 1) 1 else maxNum
                        //必须三个条件同时成立，才能开启裁剪
                        val canCrop = isCrop && max==1 && !types.containsAll(MimeType.ofVideo())
                        if(from is Activity){
                            from.startForResult<PickerEmptyActivity>(bundle = arrayOf("pickerData" to _PickerData(
                                isCrop = canCrop, maxNum = max, types = types, action = "picker", isCompress = isCompress,
                                maxVideoSize = maxVideoSize, showCapture = showCapture, spanCount = spanCount
                            )), requestCode = reqCode)
                        }else if (from is Fragment){
                            from.startForResult<PickerEmptyActivity>(bundle = arrayOf("pickerData" to _PickerData(
                                isCrop = canCrop, maxNum = max, types = types, action = "picker", isCompress = isCompress,
                                maxVideoSize = maxVideoSize, showCapture = showCapture, spanCount = spanCount
                            )), requestCode = reqCode)
                        }
                    }
                    override fun onDenied() {
                        toast(AndroidKTX.context.string(R.string._ktx_permisison_deny))
                    }
                })
                .request()
    }

    /**
     * 开启录制视频
     * @param from
     * @param reqCode
     * @param maxDuration 最大时长限制，默认是15秒，暂时实现
     */
//    fun startRecord(from: Any, reqCode: Int,){
//        PermissionUtils
//            .permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA, )
//            .callback(object : PermissionUtils.SimpleCallback {
//                override fun onGranted() {
//                    if(from is Activity){
//                        CameraActivity.start(from, requestCode = reqCode, mode = CameraActivity.OnlyVideo)
//                    }else if(from is Fragment){
//                        CameraActivity.start(from, requestCode = reqCode, mode = CameraActivity.OnlyVideo)
//                    }
//                }
//                override fun onDenied() {
//                    ToastUtils.showShort("权限获取失败，无法使用相册功能")
//                }
//            })
//            .request()
//
//    }

    /**
     * 获取结果，返回的是文件路径列表。适用于通过onActivityResult获取数据
     */
    fun fetchResult(data: Intent?): ArrayList<String>{
        val list = fetchUriResult(data)
        val paths = arrayListOf<String>()
        list.forEach {
            UriHelper.grantPermissions(data, it, true)
            paths.add(UriUtils.uri2File(it).absolutePath)
        }
        return paths
    }

    /**
     * uri列表转路径列表
     */
    fun uriListToPathList(uriList: List<Uri>): List<String>{
        val paths = arrayListOf<String>()
        uriList.forEach {
            paths.add(UriUtils.uri2File(it).absolutePath)
        }
        return paths
    }

    /**
     * 获取结果，返回的是uri列表。适用于通过onActivityResult获取数据
     */
    fun fetchUriResult(data: Intent?): ArrayList<Uri>{
        val list = data?.getParcelableArrayListExtra<Uri>("result") ?: arrayListOf()
        list.forEach {
            UriHelper.grantPermissions(data, it, true)
        }
        return list
    }

}