package com.lxj.androidktx.core

import android.media.MediaMetadataRetriever
import com.blankj.utilcode.util.FileUtils
import java.io.File

/**
 * 获取文件时长，可能为0
 */
fun String.getMediaDuration():Long{
    if(this.isNullOrEmpty() || !FileUtils.isFileExists(this)) return 0L
    val mmr =  MediaMetadataRetriever()
    try {
        mmr.setDataSource(this)
        val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        if(duration!=null) return duration.toLong()
        return 0L
    } catch (e: Exception) {
        return 0L
    } finally {
        mmr.release();
    }
}

fun File.getDuration() = absolutePath.getMediaDuration()