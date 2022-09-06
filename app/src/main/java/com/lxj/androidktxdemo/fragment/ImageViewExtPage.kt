package com.lxj.androidktxdemo.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.lxj.androidktx.core.*
import com.lxj.androidktx.picker.ImagePicker
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_imageview_ext.*

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
class ImageViewExtPage: BaseFragment(){
    private val images = arrayOf(
            "https://images.unsplash.com/photo-1606787619248-f301830a5a57?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            "https://images.unsplash.com/photo-1624020491079-a369d85f00cd?ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxOHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1624020491079-a369d85f00cd?ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxOHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
    )
    override fun getLayoutId() = R.layout.fragment_imageview_ext

    override fun initView() {
//        Glide.getPhotoCacheDir(requireContext())?.deleteRecursively()
        title1.click {
            ImageUtils.save2Album(nsvImage.toBitmap(), Bitmap.CompressFormat.PNG)
        }
        loadImage()
    }

    private fun loadImage(){
        image1.load(images[0], roundArray = floatArrayOf(50f, 10f, 80f, 10f),
            borderColor = Color.GREEN, borderSize = 4.dp,
                 onImageFail = {
                ToastUtils.showShort("图片加载失败")
                     image1.setImageResource(R.mipmap.test)
            }, onImageLoad = {
                ToastUtils.showShort("图片加载成功：dw ${it?.intrinsicWidth}")
            })

        title1.text = "image1.load(url)"

//        (0..4).forEach {
//            val image = ImageView(context)
//            image.layoutParams = CardView.LayoutParams(imgw, 106.dp)
////                image.scaleType = ImageView.ScaleType.CENTER_CROP
//            cardView.addView(image)
//            image.load(it, roundRadius = 8.dp)
//        }

        image2.load(images[1], isCircle = true, isCrossFade = true, isCenterCrop = true,
            targetWidth = 400, targetHeight = 400, borderColor = Color.RED, borderSize = 5.dp)
        title2.text = "image2.load(url, isCircle = true)"

        image3.load(images[2], isCenterCrop = true , placeholder = R.mipmap.ic_launcher,  borderColor = Color.RED, borderSize = 5.dp,)
        image4.load(images[2], isCenterCrop = true, blurScale = 1f, roundArray = floatArrayOf(100f, 100f, 0f,0f), )
        image5.load(images[2], isCenterCrop = true, blurScale = 0.3f, roundRadius = 20.dp, borderColor = Color.RED, borderSize = 5.dp,
            roundArray = floatArrayOf(50f, 10f, 80f, 10f),)
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

            ImagePicker.startCamera(this, 1, isCompress = true, isCrop = true) //打开相机
//            ImagePicker.startCamera(this, 1, isCompress = false) //打开相机不压缩
//            ImagePicker.startCamera(this, 1, isCrop = true) //打开相机并裁剪
//            ImagePicker.startCamera(this, 1) //打开相机不裁剪
//            ImagePicker.startRecord(this, 1, ) //打开相机并裁剪
//            ImagePicker.startPicker(this, 1, spanCount = 4, isCrop = true) //打开相机并裁剪
//            ImagePicker.startPicker(this, 1, types = MimeType.ofVideo()) //打开相机并裁剪
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data==null)return
//        ToastUtils.showShort(QrCodeUtil.fetchResult(1, data))
        if(requestCode==1&& resultCode==Activity.RESULT_OK){
            val url = ImagePicker.fetchUriResult(data)
            LogUtils.e(url + "  size: ${FileUtils.getSize(UriUtils.uri2File(url[0]))}")
            image1.load(url[0], isCrossFade = true)
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