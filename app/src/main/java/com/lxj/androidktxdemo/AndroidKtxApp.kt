package com.lxj.androidktxdemo

import android.graphics.Typeface
import androidx.multidex.MultiDexApplication
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.entity.User
import com.lxj.androidktxdemo.entity.UserGender
import com.lxj.audioplayer.ExoPlayerManager
import java.lang.Exception
import java.util.*

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
class AndroidKtxApp: MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
//        AndroidKtxConfig.init(this)
        AndroidKTX.init(context = this,
                isDebug = BuildConfig.DEBUG,
                defaultLogTag = "androidktx",
                sharedPrefName = "demo")
        ExoPlayerManager.init(shortTimeout = true)

        val user = sp().getObject<User>("user")
        LogUtils.d("user: ${user}")
        sp().putObject("user", User(name = "李晓俊", age = 1000))
        LogUtils.d("user: ${sp().getObject<User>("user")}")


        val json = User(name = "啊啊", date = Date(), gender = UserGender.Man).toJson()
        LogUtils.d("json1: ${json}")
        LogUtils.d("json: ${json.toBean<User>()}")
        UserGender.Man.ordinal

        replaceSystemDefaultFont()
    }

    fun replaceSystemDefaultFont() {
        Thread(Runnable {
            try {
                val start = System.currentTimeMillis()
                val font = Typeface.createFromAsset(assets, "OPPOSans-R.ttf")
                val defaultField = android.graphics.Typeface::class.java.getDeclaredField("MONOSPACE")
                defaultField.isAccessible = true
                defaultField.set(null, font)
                LogUtils.d("替换默认字体成功，耗时：${System.currentTimeMillis()-start}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

}