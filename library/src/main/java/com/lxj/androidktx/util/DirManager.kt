package com.lxj.androidktx.util

import android.os.Environment
import com.lxj.androidktx.AndroidKTX.context
import java.io.File

/**
 * 自动创建常用目录，并检查权限
 */
object DirManager{
    var rootDir: String = ""
    val tempDir: String by lazy { "${rootDir}/temp" }
    val downloadDir: String by lazy { "${rootDir}/download" }
    val cacheDir: String by lazy { "${rootDir}/cache" }

    fun init(){
        val sdcardExist = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()
        rootDir = if(sdcardExist) context.getExternalFilesDir(null)?.absolutePath?:""
        else context.filesDir.absolutePath

        createDirIfNotExist(rootDir)
        createDirIfNotExist(tempDir)
        createDirIfNotExist(downloadDir)
        createDirIfNotExist(cacheDir)
    }

    private fun createDirIfNotExist(path: String){
        val file = File(path)
        if(!file.exists()) file.mkdirs()
    }

}