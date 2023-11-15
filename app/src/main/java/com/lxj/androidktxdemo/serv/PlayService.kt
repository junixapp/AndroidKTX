package com.lxj.androidktxdemo.serv

import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NotificationUtils
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.core.string
import com.lxj.androidktxdemo.MainActivity
import com.lxj.androidktxdemo.R
import com.lxj.audioplayer.ExoPlayerManager

class PlayService : Service() {
    private val notificationId = 1
    private val tag = "RecordService"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var url = intent.getStringExtra("url")
        LogUtils.dTag(tag,"onStartCommand: ${url}")
        if(!url.isNullOrEmpty()) ExoPlayerManager.playSingle(url)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.dTag(tag,"onCreate")
        val notification = NotificationUtils.getNotification(
            NotificationUtils.ChannelConfig.DEFAULT_CHANNEL_CONFIG
        ) { builder ->
            val pn = PendingIntent.getActivity(this, 1, mainIntent(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            builder.setContentIntent(pn)
            builder.setOngoing(true)
            builder.setContentTitle(string(R.string.app_name))
            builder.setContentText("播放中")
            builder.setSmallIcon(R.mipmap.ic_launcher)
        }
        startForeground(notificationId, notification)
    }

    fun mainIntent(): Intent{
        val launchIntent = Intent(this, MainActivity::class.java)
        launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return launchIntent
    }

    override fun onDestroy() {
        super.onDestroy()
        NotificationUtils.cancel(notificationId)
    }

    companion object {
        fun start(url : String) {
            val intent = Intent(AndroidKTX.context, PlayService::class.java)
            intent.putExtra("url", url)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AndroidKTX.context.startForegroundService(intent)
            } else {
                AndroidKTX.context.startService(intent)
            }
        }
    }
}