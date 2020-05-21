package com.lxj.androidktx.util

import android.content.Context
import android.util.Base64
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.*
import com.lxj.androidktx.core.putString
import com.lxj.androidktx.core.sp
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
 * 版本更新工具，功能有2个：
 * 1. 弹出版本更新提示的弹窗，如果对UI有要求，可以自己实现弹窗，然后调用第2个方法
 * 2. 下载并安装Apk
 */
object VersionUpdateUtil {
    const val cacheKey = "_version_update_download_apk_"
    private fun showUpdatePopup(context: Context, updateData: CommonUpdateInfo,path: String){
        XPopup.Builder(context)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(VersionUpdatePopup(context = context, updateInfo = updateData, onOkClick = {
                    //删除缓存
                    sp().putString(cacheKey, "")
                    AppUtils.installApp(path)
                }))
                .show()
    }

    /**
     * 下载并安装Apk，会自动检测是否有缓存过的apk文件，如果有则直接提示安装。如果没有则进行下载，一旦安装就删除缓存的文件路径
     * @param updateData 更新相关信息
     * @param onShowUpdateUI 默认会有个更新的提示，如果想自己实现UI，则实现这个监听器
     */
    fun downloadAndInstallApk(context: Context, updateData: CommonUpdateInfo, onShowUpdateUI: ((apkPath: String)->Unit)?=null ){
        //检测是否有缓存的apk路径，如果有说明已经下载过了
        val cacheApkPath = sp().getString(cacheKey,"")
        if(cacheApkPath.isNotEmpty() && FileUtils.isFileExists(cacheApkPath)){
            if(onShowUpdateUI!=null){
                onShowUpdateUI(cacheApkPath)
            }else{
                showUpdatePopup(context, updateData, cacheApkPath)
            }
            return
        }
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(object : PermissionUtils.SimpleCallback{
                    override fun onGranted() {
                        if(updateData.download_url.isNullOrEmpty()){
                            LogUtils.e("apk下载地址为空")
                            return
                        }
                        val filename = "${Base64.encode(updateData.download_url!!.toByteArray(), Base64.DEFAULT)}.apk"
                        val downloadDir = File(DirManager.downloadDir)
                        FileUtils.createOrExistsDir(downloadDir)
                        val file = File("${DirManager.downloadDir}/${filename}")
                        FileUtils.createFileByDeleteOldFile(file)
                        LogUtils.e("apk下载保存路径：${file.absolutePath}")
                        updateData.download_url!!.http(baseUrlTag = "")
                                .savePath(file.absolutePath)
                                .get<File>(object : HttpCallback<File>{
                                    override fun onSuccess(t: File) {
                                        //缓存路径
                                        sp().putString(cacheKey, t.absolutePath)
                                        if(onShowUpdateUI!=null){
                                            onShowUpdateUI(t.absolutePath)
                                        }else{
                                            showUpdatePopup(context, updateData, t.absolutePath)
                                        }
                                    }
                                    override fun onFail(e: IOException) {
                                        super.onFail(e)
                                    }
                                })
                    }
                    override fun onDenied() {
                        ToastUtils.showShort("权限获取失败，无法下载新版本")
                    }
                }).request()
    }

}