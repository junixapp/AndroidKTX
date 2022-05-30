package com.lxj.androidktxdemo

import android.content.ContentUris
import android.content.ContentValues
import android.media.MediaMetadataRetriever
import android.media.RingtoneManager
import android.os.Build
import android.provider.MediaStore
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.AndroidKTX
import java.io.*

object RingtoneTool {
    fun setRingtone(path: String, name: String, ringtoneType: Int = RingtoneManager.TYPE_RINGTONE){
//        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES), AndroidKTX.context.string(R.string.app_name))
//        if(!dir.exists()) dir.mkdirs()
//        val newPath = "${dir}/${File(path).name}"
//        val layout = FileUtils.copy(path, newPath)
//        LogUtils.e("copy file layout: $layout  $newPath")
//
////        val newUri = UriUtils.file2Uri(File(newPath))
//        val newUri = Uri.fromFile(File(newPath))
//            RingtoneManager.setActualDefaultRingtoneUri(AndroidKTX.context, RingtoneManager.TYPE_RINGTONE,newUri)
//            LogUtils.e("insert newUri: ${newUri}")

//        Settings.System.putString(AndroidKTX.context.contentResolver, Settings.System.RINGTONE,
//            UriUtils.file2Uri(File(newPath)).toString())


//        MediaScannerConnection.scanFile(AndroidKTX.context, arrayOf(newPath), arrayOf("audio/mp3"), object : MediaScannerConnection.OnScanCompletedListener{
//            override fun onScanCompleted(path: String?, uri: Uri?) {
//                LogUtils.e("newPath: ${path} uri: ${uri}")
//                RingtoneManager.setActualDefaultRingtoneUri(AndroidKTX.context, RingtoneManager.TYPE_RINGTONE,
//                    UriUtils.file2Uri(File(newPath)))
//                ToastUtils.showShort(AndroidKTX.context.string(R.string.set_successfully))
//            }
//        })


        val sdFile =  File(path)
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(sdFile.absolutePath)
        val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()?:0

        //ComtentValues 用于存储数据的  contenvalues只能存储基本类型的数据
        val values =  ContentValues()
        values.put(MediaStore.MediaColumns.DATA,sdFile.absolutePath)
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        values.put(MediaStore.MediaColumns.TITLE, name)
        values.put(MediaStore.MediaColumns.SIZE,sdFile.length())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.DURATION, duration)
        }
        values.put(MediaStore.MediaColumns.MIME_TYPE,"audio/mp3")
        values.put(MediaStore.Audio.Media.IS_MUSIC, true)

        when(ringtoneType){
            RingtoneManager.TYPE_RINGTONE -> {
                values.put(MediaStore.Audio.Media.IS_RINGTONE,true)
                values.put(MediaStore.Audio.Media.IS_NOTIFICATION,false)
                values.put(MediaStore.Audio.Media.IS_ALARM,false)
            }
            RingtoneManager.TYPE_NOTIFICATION -> {
                values.put(MediaStore.Audio.Media.IS_RINGTONE, false)
                values.put(MediaStore.Audio.Media.IS_NOTIFICATION,true)
                values.put(MediaStore.Audio.Media.IS_ALARM,false)
            }
            RingtoneManager.TYPE_ALARM -> {
                values.put(MediaStore.Audio.Media.IS_RINGTONE, false)
                values.put(MediaStore.Audio.Media.IS_NOTIFICATION,false)
                values.put(MediaStore.Audio.Media.IS_ALARM,true)
            }
            RingtoneManager.TYPE_ALL -> {
                values.put(MediaStore.Audio.Media.IS_RINGTONE, true)
                values.put(MediaStore.Audio.Media.IS_NOTIFICATION,true)
                values.put(MediaStore.Audio.Media.IS_ALARM,true)
            }
        }

        val uri = MediaStore.Audio.Media.getContentUriForPath(sdFile.absolutePath) ?: return
        LogUtils.e("uri: $uri   path: $path")
        val cursor = AndroidKTX.context.contentResolver.query(uri, null, MediaStore.MediaColumns.DATA + "=?",
            arrayOf(path), null) ?: return
        LogUtils.e("cursor: ${cursor.count}")
        val resolver = AndroidKTX.context.contentResolver
        if (cursor.moveToFirst() && cursor.count >0){
//            val _id = cursor.getString(0)
//            resolver.update(uri, values, MediaStore.MediaColumns.DATA + "=?", arrayOf(path))
//
//            LogUtils.e("_id: ${_id}")
//            val newUri = ContentUris.withAppendedId(uri, _id.toLongOrNull() ?: 0)
//            LogUtils.e("newUri: ${newUri}")
//            RingtoneManager.setActualDefaultRingtoneUri(AndroidKTX.context, RingtoneManager.TYPE_RINGTONE,newUri)
        }else{
            val newUri = resolver.insert(uri,values) ?: return
            LogUtils.e("insert newUri: ${newUri}")
            val out = resolver.openOutputStream(newUri) ?: return
            writeFileFromIS(out, FileInputStream(sdFile))
            RingtoneManager.setActualDefaultRingtoneUri(AndroidKTX.context, ringtoneType, newUri)
        }
    }

    private fun writeFileFromIS(fos: OutputStream, `is`: InputStream): Boolean {
        var os: OutputStream? = null
        return try {
            os = BufferedOutputStream(fos)
            val data = ByteArray(8192)
            var len: Int
            while (`is`.read(data, 0, 8192).also { len = it } != -1) {
                os.write(data, 0, len)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}