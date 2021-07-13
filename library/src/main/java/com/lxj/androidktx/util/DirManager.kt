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
    //可以共享给三方的文件目录
    val shareDir: String by lazy { "${rootDir}/share" }

    fun init(){
        rootDir = context.getExternalFilesDir(null)?.absolutePath?: context.filesDir.absolutePath

        createDirIfNotExist(rootDir)
        createDirIfNotExist(tempDir)
        createDirIfNotExist(downloadDir)
        createDirIfNotExist(cacheDir)
        createDirIfNotExist(shareDir)
    }

    private fun createDirIfNotExist(path: String){
        val file = File(path)
        if(!file.exists()) file.mkdirs()
    }

}