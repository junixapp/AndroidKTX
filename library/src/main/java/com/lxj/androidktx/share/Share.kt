package com.lxj.androidktx.share

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.lxj.xpermission.PermissionConstants
import com.lxj.xpermission.XPermission
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.*
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb

/**
 * Description:
 * Create by dance, at 2019/4/1
 */
@SuppressLint("StaticFieldLeak")
object Share {
    lateinit var context: Context
    private var isDebug: Boolean = false
    fun init(
        context: Context, isDebug: Boolean, umengAppKey: String,
        wxAppId: String, wxAppKey: String,
        qqAppId: String, qqAppKey: String,
        weiboAppId: String, weiboAppKey: String, weiboCallbackUrl: String
    ) {
        this.context = context
        this.isDebug = isDebug

        UMConfigure.setLogEnabled(isDebug)
        UMConfigure.init(
                this.context,
            umengAppKey,
            "Umeng",
            UMConfigure.DEVICE_TYPE_PHONE,
            null
        )
        PlatformConfig.setWeixin(wxAppId, wxAppKey)
        PlatformConfig.setQQZone(qqAppId, qqAppKey)
        PlatformConfig.setSinaWeibo(weiboAppId, weiboAppKey, weiboCallbackUrl)
    }


    private fun login(activity: Activity, platform: SHARE_MEDIA, callback: ShareLoginCallback) {
        UMShareAPI.get(activity).getPlatformInfo(activity, platform, object : UMAuthListener {
            override fun onComplete(p: SHARE_MEDIA, p1: Int, map: MutableMap<String, String>) {
                log("login->onComplete：$p $map")
                callback.onComplete(p, LoginData.fromMap(map))
            }

            override fun onError(p: SHARE_MEDIA, p1: Int, t: Throwable) {
                log("login->onError：$p ${t.message}")
                callback.onError(p, t)
            }

            override fun onStart(p: SHARE_MEDIA) {
                log("login->onStart：$p")
                callback.onStart(p)
            }

            override fun onCancel(p: SHARE_MEDIA, p1: Int) {
                log("login->onCancel：$p")
                callback.onCancel(p)
            }
        })
    }

    fun wechatLogin(activity: Activity, callback: ShareLoginCallback) {
        login(activity, SHARE_MEDIA.WEIXIN, callback)
    }

    fun qqLogin(activity: Activity, callback: ShareLoginCallback) {
        login(activity, SHARE_MEDIA.QQ, callback)
    }

    fun sinaLogin(activity: Activity, callback: ShareLoginCallback) {
        login(activity, SHARE_MEDIA.SINA, callback)
    }

    fun share(
        activity: Activity, platform: SHARE_MEDIA, bitmap: Bitmap? = null, text: String = "", url: String = "",
        title: String = "分享的标题"
    ) {
        checkPermission {
            ShareAction(activity)
                .setPlatform(platform)//传入平台
                .apply {
                    if (bitmap != null) withMedia(UMImage(activity, bitmap))
                    if (text.isNotEmpty()) withText(text)
                    if (url.isNotEmpty()) withMedia(UMWeb(url).apply {
                        //微信分享链接时需要标题和描述
                        description = text
                        setTitle(title)
                    })
                }
                .setCallback(object : UMShareListener {
                    override fun onResult(p: SHARE_MEDIA?) {
                        log("share->onResult $p")
                    }

                    override fun onCancel(p: SHARE_MEDIA?) {
                        log("share->onCancel $p")
                    }

                    override fun onError(p: SHARE_MEDIA?, t: Throwable?) {
                        log("share->onError $p  ${t?.message}")
                    }

                    override fun onStart(p: SHARE_MEDIA?) {
                        log("share->onStart $p")
                    }
                })
                .share()
        }
    }

    private fun checkPermission(action: () -> Unit) {
        XPermission.create(context, PermissionConstants.STORAGE)
            .callback(object : XPermission.SimpleCallback {
                override fun onGranted() {
                    action()
                }

                override fun onDenied() {
                    Toast.makeText(context, "没有存储权限，无法分享文件", Toast.LENGTH_SHORT).show()
                    action()
                }
            })
            .request()
    }

    private fun log(msg: String) {
        if (isDebug) Log.e("share", msg)
    }

    interface ShareLoginCallback {
        fun onCancel(platform: SHARE_MEDIA) {}
        fun onStart(platform: SHARE_MEDIA) {}
        fun onError(platform: SHARE_MEDIA, t: Throwable) {}
        fun onComplete(platform: SHARE_MEDIA, loginData: LoginData) {}
    }
}