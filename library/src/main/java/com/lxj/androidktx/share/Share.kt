package com.lxj.androidktx.share

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.lxj.xpopup.XPopup
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.*
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMMin
import com.umeng.socialize.media.UMVideo
import com.umeng.socialize.media.UMWeb
import java.net.URLEncoder


/**
 * Description:
 * Create by dance, at 2019/4/1
 */
@SuppressLint("StaticFieldLeak")
object Share {
    private var isDebug: Boolean = false
    fun init(
            context: Context, isDebug: Boolean, umengAppKey: String, umengMessageSecret: String? = null,
            wxAppId: String, wxAppKey: String,
            qqAppId: String = "", qqAppKey: String = "",
            weiboAppId: String = "", weiboAppKey: String = "", weiboCallbackUrl: String = ""
    ) {
        this.isDebug = isDebug

        UMConfigure.setLogEnabled(isDebug)
        UMConfigure.init( context, umengAppKey, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, umengMessageSecret)
        PlatformConfig.setWeixin(wxAppId, wxAppKey)
        if(qqAppId.isNotEmpty()) PlatformConfig.setQQZone(qqAppId, qqAppKey)
        if(weiboAppId.isNotEmpty()) PlatformConfig.setSinaWeibo(weiboAppId, weiboAppKey, weiboCallbackUrl)
    }


    private fun login(activity: Activity, platform: SHARE_MEDIA, callback: ShareCallback) {
        UMShareAPI.get(activity).getPlatformInfo(activity, platform, OauthCallback(callback))
    }

    fun wechatLogin(activity: Activity, callback: ShareCallback) {
        login(activity, SHARE_MEDIA.WEIXIN, callback)
    }

    fun qqLogin(activity: Activity, callback: ShareCallback) {
        login(activity, SHARE_MEDIA.QQ, callback)
    }

    fun sinaLogin(activity: Activity, callback: ShareCallback) {
        login(activity, SHARE_MEDIA.SINA, callback)
    }

    fun share(
            activity: Activity, platform: SharePlatform, bitmap: Bitmap? = null, text: String? = null, url: String? = null,
            title: String? = null, videoUrl: String? = null,  callback: ShareCallback? = null
    ) {
        checkPermission(activity) {
            doShare(activity, convertPlatform(platform), bitmap, text, url, title, videoUrl, callback)
        }
    }


    /**
     * 直接打开小程序
     * @param miniAppId 不是小程序的id，是小程序的原始id，在小程序设置界面
     */
    fun openMiniProgram(activity: Activity,appId: String, miniAppId: String,
                        path: String, forTestVersion: Boolean = false,
                        forPreviewVersion: Boolean = false){
        val api = WXAPIFactory.createWXAPI(activity, appId)
        val req: WXLaunchMiniProgram.Req = WXLaunchMiniProgram.Req()
        req.userName = miniAppId // 填小程序原始id
        req.path = path ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        if(forTestVersion){
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST // 可选打开 开发版，体验版和正式版
        }else if(forPreviewVersion){
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW // 可选打开 开发版，体验版和正式版
        }else{
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE // 可选打开 开发版，体验版和正式版
        }
        api.sendReq(req)
    }

    /**
     * 分享到微信小程序
     * @param miniAppId 不是小程序的id，是小程序的原始id，在小程序设置界面
     */
    fun shareToMiniProgram(activity: Activity, url: String, bitmap: Bitmap? = null, imgRes: Int? = null, title: String, desc: String, path: String,
                           miniAppId: String, forTestVersion: Boolean = false,
                           forPreviewVersion: Boolean = false , callback: ShareCallback? = null){
        if(forTestVersion){
            Config.setMiniTest()
        }
        if(forPreviewVersion){
            Config.setMiniPreView()
        }
        val umMin = UMMin(url) //兼容低版本的网页链接
        val img = if(bitmap==null)UMImage(activity,  imgRes!!) else UMImage(activity,  bitmap)
        umMin.setThumb(img) // 小程序消息封面图片
        umMin.title = title // 小程序消息title
        umMin.description = desc // 小程序消息描述
        umMin.path = path //小程序页面路径
        umMin.userName = miniAppId // 小程序原始id,在微信平台查询
        ShareAction(activity)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(object : UMShareListener {
                    override fun onResult(p: SHARE_MEDIA) {
                        log("share->onResult $p")
                        callback?.onComplete(p)
                    }

                    override fun onCancel(p: SHARE_MEDIA) {
                        log("share->onCancel $p")
                        callback?.onCancel(p)
                    }

                    override fun onError(p: SHARE_MEDIA, t: Throwable) {
                        log("share->onError $p  ${t?.message}")
                        callback?.onError(p, t)
                    }

                    override fun onStart(p: SHARE_MEDIA) {
                        log("share->onStart $p")
                        callback?.onStart(p)
                    }
                }).share()
    }

    fun deleteOauth(activity: Activity, platform: SharePlatform, callback: ShareCallback? = null){
        UMShareAPI.get(activity).deleteOauth(activity, convertPlatform(platform), OauthCallback(callback))
    }

//    fun shareWithUI(
//            activity: Activity, platform: SharePlatform, bitmap: Bitmap? = null, text: String = "", url: String = "",
//            title: String = "", callback: ShareCallback? = null
//    ) {
//        checkPermission(activity) {
//            XPopup.Builder(activity).asCustom(SharePopup(activity)).show()
////            doShare(activity, platform, bitmap, text, url, title, callback)
//        }
//    }

    private fun doShare(
            activity: Activity, platform: SHARE_MEDIA, bitmap: Bitmap? = null, text: String? = null, url: String? = null,
            title: String? = null, videoUrl: String? = null, callback: ShareCallback? = null) {
        ShareAction(activity)
                .setPlatform(platform)//传入平台
                .apply {
                    if (bitmap != null) withMedia(UMImage(activity, bitmap))
                    if (text!=null) withText(text)
                    if (videoUrl!=null) withMedia(UMVideo(videoUrl).apply {
                        description = text
                        setTitle(title)
                    })

                    if (url!=null) withMedia(UMWeb(url).apply {
                        //微信分享链接时需要标题和描述
                        description = text
                        setTitle(title)
                        if(bitmap!=null)setThumb(UMImage(activity, bitmap))
                    })
                }
                .setCallback(object : UMShareListener {
                    override fun onResult(p: SHARE_MEDIA) {
                        log("share->onResult $p")
                        callback?.onComplete(p)
                    }

                    override fun onCancel(p: SHARE_MEDIA) {
                        log("share->onCancel $p")
                        callback?.onCancel(p)
                    }

                    override fun onError(p: SHARE_MEDIA, t: Throwable) {
                        log("share->onError $p  ${t?.message}")
                        callback?.onError(p, t)
                    }

                    override fun onStart(p: SHARE_MEDIA) {
                        log("share->onStart $p")
                        callback?.onStart(p)
                    }
                })
                .share()
    }



    private fun checkPermission(context: Context, action: () -> Unit) {
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(object : PermissionUtils.SimpleCallback{
                    override fun onGranted() {
                        action()
                    }
                    override fun onDenied() {
                        Toast.makeText(context, "没有存储权限，无法分享文件", Toast.LENGTH_SHORT).show()
                        action()
                    }

                }).request()
    }

    private fun convertPlatform(platform: SharePlatform) = when(platform) {
        SharePlatform.WxFriend -> SHARE_MEDIA.WEIXIN
        SharePlatform.WxCircle -> SHARE_MEDIA.WEIXIN_CIRCLE
        SharePlatform.QQ -> SHARE_MEDIA.QQ
        SharePlatform.QZone -> SHARE_MEDIA.QZONE
        SharePlatform.Sina -> SHARE_MEDIA.SINA
    }

    private fun log(msg: String) {
        if (isDebug) Log.e("share", msg)
    }

    class OauthCallback(var callback: ShareCallback?) : UMAuthListener{
        override fun onComplete(p: SHARE_MEDIA, p1: Int, map: MutableMap<String, String>?) {
            log("UMAuthListener->onComplete：$p $map")
            callback?.onComplete(p, if(map==null) null else LoginData.fromMap(map))
        }

        override fun onError(p: SHARE_MEDIA, p1: Int, t: Throwable) {
            log("UMAuthListener->onError：$p ${t.message}")
            callback?.onError(p, t)
        }

        override fun onStart(p: SHARE_MEDIA) {
            log("UMAuthListener->onStart：$p")
            callback?.onStart(p)
        }

        override fun onCancel(p: SHARE_MEDIA, p1: Int) {
            log("UMAuthListener->onCancel：$p")
            callback?.onCancel(p)
        }
    }

    interface ShareCallback {
        fun onCancel(platform: SHARE_MEDIA) {}
        fun onStart(platform: SHARE_MEDIA) {}
        fun onError(platform: SHARE_MEDIA, t: Throwable) {}
        fun onComplete(platform: SHARE_MEDIA, loginData: LoginData? = null) {}
    }
}