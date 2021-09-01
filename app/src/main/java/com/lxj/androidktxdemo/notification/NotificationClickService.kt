package com.lxj.androidktxdemo.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NotificationUtils
import com.lxj.androidktx.core.startActivity
import com.lxj.androidktxdemo.DemoActivity
import com.lxj.androidktxdemo.MainActivity
import com.lxj.androidktxdemo.WelcomeActivity

class NotificationClickService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.e("NotificationClickService  cfreate" )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val id = intent?.getIntExtra("id", -1)
        if(id!=null) {
//            NotificationUtils.cancel(id)
        }
        if(!ActivityUtils.isActivityExistsInStack(MainActivity::class.java)){
            startActivity<WelcomeActivity>(bundle = arrayOf("fromNotification" to true))
        }else{
            startActivity<DemoActivity>()
        }
        LogUtils.e("notiId: $id")
        return super.onStartCommand(intent, flags, startId)
    }
}