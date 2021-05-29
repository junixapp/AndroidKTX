package com.lxj.androidktxdemo.fragment

import android.content.Intent
import android.media.MediaMetadataRetriever
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.load
import com.lxj.androidktx.picker.ImagePicker
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_imageview_ext.*

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
class ImageViewExtPage: BaseFragment(){
    private val images = arrayOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606302655766&di=52555c75e485292ca456f44e0f98fcc1&imgtype=0&src=http%3A%2F%2Fimg.aiimg.com%2Fuploads%2Fuserup%2F0909%2F2Z64022L38.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606302655766&di=4e7d90ee24391f684eaa421e4a4ab423&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F11%2F48%2F01300000195282124296481807051.jpg"
    )
    override fun getLayoutId() = R.layout.fragment_imageview_ext

    override fun initView() {
        Glide.getPhotoCacheDir(context!!)?.deleteRecursively()
        loadImage()
    }

    private fun loadImage(){
        image1.load(images[0], placeholder = R.mipmap.ic_launcher,
                onImageLoad = {
//            LogUtils.e("图片加载完成：${(it as BitmapDrawable).bitmap.byteCount}")
        })
        title1.text = "image1.load(url)"


        image2.load(images[0], placeholder = R.mipmap.ic_launcher, roundRadius = 20, isCrossFade = true)
        title2.text = "image2.load(url, isCircle = true)"

        image3.load(images[1])
        title3.text = "image3.load(url, roundRadius = 20)"

        image1.click {
//            QrCodeUtil.start(this,1, color = Color.parseColor("#ff0000"))
//            Share.shareWithUI(activity!!, SharePlatform.WxCircle)
//            WebActivity.start(
//                    hideTitleBar = true,
//                    title = "xxxx",
//                    url = "https://player.youku.com/embed/XNDI1MTY2MTYwMA==?client_id=fe317d1cbae86c63&password=&autoplay=true#www.wu888.cn",
//                    rightIconRes = R.mipmap.ic_launcher, rightIconClickAction = {
//                ToastUtils.showShort("点击了")
//            })

//            ImagePicker.startCamera(this, 1) //打开相机
//            ImagePicker.startCamera(this, 1, isCompress = false) //打开相机不压缩
//            ImagePicker.startCamera(this, 1, isCrop = true) //打开相机并裁剪
//            ImagePicker.startCamera(this, 1) //打开相机不裁剪
//            ImagePicker.startRecord(this, 1, ) //打开相机并裁剪
            ImagePicker.startPicker(this, 1, isCrop = true) //打开相机并裁剪
//            ImagePicker.startPicker(this, 1, types = MimeType.ofVideo()) //打开相机并裁剪
//
//            Share.init(context!!, BuildConfig.DEBUG, umengAppKey = "5e5d0267570df3806d0002fb",
//                    wxAppId = "wx31386f28f849f6fe", wxAppKey = "6414a5c0dd7b97d540adef8922d4c31b")
//
//            Share.shareToMiniProgram(activity = activity!!,
//                    url = "https://developer.umeng.com/docs/66632/detail/66875?um_channel=sdk", title = "标题", desc = "描述",
////                    bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher),
//                    imgRes = R.mipmap.ic_launcher,
//                    path = "/pages/home/index", miniAppId = "wx596f62867a2bbc17", forTestVersion = true,
//                    callback = object :Share.ShareCallback{
//
//                    })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data==null)return
//        ToastUtils.showShort(QrCodeUtil.fetchResult(1, data))
        if(requestCode==1&& resultCode==-1){
            val url = ImagePicker.fetchResult(data)
            LogUtils.e(url)
            image1.load(url[0])
//            QrCodeUtil.parseQrCode(url[0], onFinish= { result->
//                ToastUtils.showLong(result)
//            })
//            val videoPath = ImagePicker.fetchRecordResult(data)
//            val len = FileUtils.getFileLength(videoPath)
//            LogUtils.e("拍照返回：${videoPath}   大小：${len/1024}k")
//            PlayerActivity.start(url = videoPath)
        }
    }
}