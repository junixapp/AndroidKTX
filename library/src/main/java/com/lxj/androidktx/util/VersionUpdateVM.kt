package com.lxj.androidktx.util

import android.content.Context
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.okhttp.*
import com.lxj.androidktx.popup.VersionUpdatePopup
import com.lxj.xpopup.XPopup
import java.io.File
import java.io.IOException


data class CommonUpdateInfo(
    var download_url: String? = null,
    var update_info: String? = null,
    var need_update: Boolean? = null
)

/**
 * 版本更新检测工具类型，功能有2个：
 * 1. 弹出版本更新提示的弹窗，如果对UI有要求，可以自己实现弹窗，然后调用第2个方法
 * 2. 下载并安装Apk
 */
object VersionUpdateUtil {

    fun showUpdatePopup(context: Context, updateData: CommonUpdateInfo){
        XPopup.Builder(context)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(VersionUpdatePopup(context = context, updateInfo = updateData, onOkClick = {
                    downloadAndInstallApk(url = updateData.download_url?:"")
                }))
                .show()
    }

    /**
     * 下载并安装Apk
     */
    fun downloadAndInstallApk(url:String){
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(object : PermissionUtils.SimpleCallback{
                    override fun onGranted() {
                        val downloadDir = File(DirManager.downloadDir)
                        if(!downloadDir.exists()) downloadDir.mkdirs()
                        ToastUtils.showShort("后台下载中...")
                        val file = File("${DirManager.downloadDir}/apk-${System.currentTimeMillis()}.apk")
                        file.createNewFile()
                        LogUtils.e("apk下载保存路径：${file.absolutePath}")
                        if(url.isNullOrEmpty()){
                            LogUtils.e("apk下载地址为空")
                            return
                        }
                        url.http(baseUrlTag = "")
                                .savePath(file.absolutePath)
                                .get<File>(object : HttpCallback<File>{
                                    override fun onSuccess(t: File) {
                                        //执行安装
                                        AppUtils.installApp(t)
                                    }
                                    override fun onFail(e: IOException) {
                                        super.onFail(e)
                                        ToastUtils.showShort("下载失败")
                                    }
                                })
                    }
                    override fun onDenied() {
                        ToastUtils.showShort("权限获取失败，无法下载新版本")
                    }
                }).request()
    }

}