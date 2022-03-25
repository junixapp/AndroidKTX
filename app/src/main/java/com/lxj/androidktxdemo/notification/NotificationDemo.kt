package com.lxj.androidktxdemo.notification

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.NotificationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktxdemo.DemoActivity
import com.lxj.androidktxdemo.R

object NotificationDemo {
    var notificationId = 0
    val url = "https://img1.baidu.com/it/u=2051604422,4029392742&fm=26&fmt=auto&gp=0.jpg"
    fun show(){
        Glide.with(AndroidKTX.context)
            .asBitmap()
            .load(url)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    innerShow(resource)
                    return false
                }
            })
            .preload(300,300)
    }

    fun innerShow(bitmap: Bitmap){
        NotificationUtils.notify(notificationId) { param ->
            val intent = Intent(AndroidKTX.context, DemoActivity::class.java)
            intent.putExtra("id", notificationId)
            val pendingIntent = PendingIntent.getActivity(
                AndroidKTX.context, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val layout = RemoteViews(AndroidKTX.context.packageName, R.layout.layout_custom_notification)
            val layout2 = RemoteViews(AndroidKTX.context.packageName, R.layout.layout_custom_notification)
            layout.setTextViewText(R.id.tvName, "dasdasda大叔大婶大所大所")
            layout.setTextViewText(R.id.tvDesc, "正在直播中，快乐开那你")
            layout.setImageViewBitmap(R.id.ivAvatar, bitmap)
            param.setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLargeIcon(ImageUtils.getBitmap(R.mipmap.ic_launcher))
                .setFullScreenIntent(pendingIntent, true)
                .setTicker("大萨达所大撒大所多")
                .setSmallIcon(R.drawable.ic_empty_zhihu)
                .setCustomContentView(layout)
                .setContentIntent(pendingIntent)
                .setCustomHeadsUpContentView(layout2)
                .setAutoCancel(true)

        }
        notificationId += 1
    }
}