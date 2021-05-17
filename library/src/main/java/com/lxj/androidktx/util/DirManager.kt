package com.lxj.androidktx.util

import android.os.Build
import android.os.Environment
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.AndroidKTX.context
import java.io.File

/**
 * 自动创建常用目录，并检查权限
 */
object DirManager{
    //Environment.getExternalStorageDirectory()
    var rootDir: String = ""
    val tempDir: String by lazy { "${rootDir}/temp" }
    val downloadDir: String by lazy { "${rootDir}/download" }
    val cacheDir: String by lazy { "${rootDir}/cache" }

    fun init(permissionResult: ((b: Boolean)->Unit)? = null){
        val sdcardExist = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()
        rootDir = if(sdcardExist) context.getExternalFilesDir(null)?.absolutePath?:""
        else context.filesDir.absolutePath

        createDirIfNotExist(rootDir)
        createDirIfNotExist(tempDir)
        createDirIfNotExist(cacheDir)
        //检查权限
//        PermissionUtils.permission(PermissionConstants.STORAGE)
//            .callback(object : PermissionUtils.SimpleCallback{
//                override fun onGranted() {
//                    //初始化根目录
//                    createDirIfNotExist(rootDir)
//                    createDirIfNotExist(downloadDir)
//                    createDirIfNotExist(tempDir)
//                    createDirIfNotExist(cacheDir)
//                    permissionResult?.invoke(true)
//                }
//                override fun onDenied() {
//                    permissionResult?.invoke(false)
//                }
//            }).request()
    }

    private fun createDirIfNotExist(path: String){
        val file = File(path)
        if(!file.exists()) file.mkdirs()
    }

}