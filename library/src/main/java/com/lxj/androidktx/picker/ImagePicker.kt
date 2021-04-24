package com.lxj.androidktx.picker

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.core.startForResult
import com.zhihu.matisse.MimeType
import java.io.Serializable

data class _PickerData(
        var action: String = "",
        var isCrop: Boolean = false,
        var isCompress: Boolean = true,
        var maxNum: Int = 9,
        var maxVideoSize: Long = 0,
        var types: Set<MimeType> = MimeType.ofImage()
): Serializable

/**
 * 图片选择器，整合了Matisse，uCrop和Luban。
 */
object ImagePicker {
    /**
     * 从Activity中直接开启相机拍照。
     * @param isCrop 是否开启裁剪，默认false
     * @param isCompress 是否使用Luban压缩，默认是true
     */
    fun startCamera(from: Activity, reqCode: Int, isCrop: Boolean = false, isCompress: Boolean = true) {
        PermissionUtils
                .permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        from.startForResult<PickerEmptyActivity>(bundle = arrayOf("pickerData" to _PickerData(
                                isCrop = isCrop, action = "camera", maxNum = 1, isCompress = isCompress
                        )), requestCode = reqCode)
                    }
                    override fun onDenied() {
                        ToastUtils.showShort("权限获取失败，无法使用拍照功能")
                    }
                })
                .request()
    }

    /**
     * 从Fragment中直接开启相机拍照。
     * @param isCrop 是否开启裁剪，默认false
     * @param isCompress 是否使用Luban压缩，默认是true
     */
    fun startCamera(from: Fragment, reqCode: Int, isCrop: Boolean = false, isCompress: Boolean = true) {
        PermissionUtils
                .permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        from.startForResult<PickerEmptyActivity>(bundle = arrayOf("pickerData" to _PickerData(
                                isCrop = isCrop, action = "camera", maxNum = 1, isCompress = isCompress
                        )), requestCode = reqCode)
                    }
                    override fun onDenied() {
                        ToastUtils.showShort("权限获取失败，无法使用拍照功能")
                    }
                })
                .request()
    }

    /**
     * 从Activity中直接开启选择器，选择器自带拍摄功能，注意：maxNum>1时和types包含video时无法使用裁剪；types包含video时无法使用压缩
     * @param isCrop 是否开启裁剪，默认false
     * @param isCompress 是否使用Luban压缩，默认是true
     * @param maxNum 最多选择数量，默认是1
     * @param types 选择的类型，默认是Image类型
     * @param maxVideoSize 最大视频大小，默认是20M
     */
    fun startPicker(from: Activity, reqCode: Int, isCrop: Boolean = false, isCompress: Boolean = true, maxNum: Int = 1, types: Set<MimeType> = MimeType.ofImage(),
    maxVideoSize: Long = 0) {
        PermissionUtils
                .permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        //必须三个条件同时成立，才能开启裁剪
                        val canCrop = isCrop && maxNum==1 && !types.containsAll(MimeType.ofVideo())
                        from.startForResult<PickerEmptyActivity>(bundle = arrayOf("pickerData" to _PickerData(
                                isCrop = canCrop, maxNum = maxNum, types = types, action = "picker", isCompress = isCompress,
                                maxVideoSize = maxVideoSize
                        )), requestCode = reqCode)
                    }
                    override fun onDenied() {
                        ToastUtils.showShort("权限获取失败，无法使用相册功能")
                    }
                })
                .request()
    }

    /**
     * 从Fragment中直接开启选择器，注意：maxNum>1时和types包含video时无法使用裁剪；types包含video时无法使用压缩
     * @param isCrop 是否开启裁剪，默认false
     * @param isCompress 是否使用Luban压缩，默认是true
     * @param maxNum 最多选择数量，默认是1
     * @param types 选择的类型，默认是Image类型
     * @param maxVideoSize 最大视频大小，默认是20M
     */
    fun startPicker(from: Fragment, reqCode: Int, isCrop: Boolean = false, isCompress: Boolean = true, maxNum: Int = 1, types: Set<MimeType> = MimeType.ofImage(),
                    maxVideoSize: Long = 0) {
        PermissionUtils
                .permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        //必须三个条件同时成立，才能开启裁剪
                        val canCrop = isCrop && maxNum==1 && !types.containsAll(MimeType.ofVideo())
                        from.startForResult<PickerEmptyActivity>(bundle = arrayOf("pickerData" to _PickerData(
                                isCrop = canCrop, maxNum = maxNum, types = types, action = "picker", isCompress = isCompress,
                                maxVideoSize = maxVideoSize
                        )), requestCode = reqCode)
                    }
                    override fun onDenied() {
                        ToastUtils.showShort("权限获取失败，无法使用相册功能")
                    }
                })
                .request()
    }

    /**
     * 获取结果
     */
    fun fetchResult(data: Intent?): ArrayList<String>{
        return data?.getStringArrayListExtra("result") ?: arrayListOf()
    }
}