package com.lxj.androidktx.util

import android.content.Context
import android.os.Environment
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import java.io.File

/**
 * 自动创建常用目录，并检查权限
 */
object DirManager{
    lateinit var rootDir: String
    lateinit var downloadDir: String
    lateinit var tempDir: String
    lateinit var cacheDir: String

    fun init(context: Context, permissionResult: ((b: Boolean)->Unit)? = null){
        rootDir = "${Environment.getExternalStorageDirectory().absolutePath}/${context.packageName}"
        downloadDir = "${rootDir}/download"
        tempDir = "${rootDir}/temp"
        cacheDir = "${rootDir}/cache"

        //检查权限
        PermissionUtils.permission(PermissionConstants.STORAGE)
            .callback(object : PermissionUtils.SimpleCallback{
                override fun onGranted() {
                    //初始化根目录
                    createDirIfNotExist(rootDir)
                    createDirIfNotExist(downloadDir)
                    createDirIfNotExist(tempDir)
                    createDirIfNotExist(cacheDir)
                    permissionResult?.invoke(true)
                }
                override fun onDenied() {
                    permissionResult?.invoke(false)
                }
            }).request()
    }

    private fun createDirIfNotExist(path: String){
        val file = File(path)
        if(!file.exists()) file.mkdirs()
    }

}