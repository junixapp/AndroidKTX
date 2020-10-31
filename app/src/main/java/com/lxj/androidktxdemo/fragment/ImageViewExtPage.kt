package com.lxj.androidktxdemo.fragment

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.base.PlayerActivity
import com.lxj.androidktx.base.WebActivity
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.load
import com.lxj.androidktx.picker.ImagePicker
import com.lxj.androidktxdemo.R
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_imageview_ext.*

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
class ImageViewExtPage: BaseFragment(){
    private val images = arrayOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602328780180&di=ab7ecb26ac85e9ff97f6cbb0248103a1&imgtype=0&src=http%3A%2F%2Ft7.baidu.com%2Fit%2Fu%3D3616242789%2C1098670747%26fm%3D79%26app%3D86%26f%3DJPEG%3Fw%3D900%26h%3D1350",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544028336056&di=a120afc7cfb3708de4f7a22741901b20&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F908fa0ec08fa513d946d2de5366d55fbb3fbd9c2.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544028336055&di=e51bbe4f5b67266d644c8f650950df9c&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D3b5bbe029ddda144ce0464f1dadebad7%2Fac345982b2b7d0a23b781e8cc1ef76094b369a69.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544028336055&di=f3c3a95d264cbf33841127370aa0b018&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F6f061d950a7b0208bf8b59c969d9f2d3572cc85c.jpg"
    )
    override fun getLayoutId() = R.layout.fragment_imageview_ext

    override fun initView() {
        loadImage()
    }

    private fun loadImage(){
        image1.load(images[0], placeholder = R.mipmap.ic_launcher,
                onImageLoad = {
//            LogUtils.e("图片加载完成：${(it as BitmapDrawable).bitmap.byteCount}")
        })
        title1.text = "image1.load(url)"


        image2.load(images[2], placeholder = R.mipmap.ic_launcher, isCircle = true)
        title2.text = "image2.load(url, isCircle = true)"

        image3.load(images[2], placeholder = R.mipmap.ic_launcher, roundRadius = 20, isCenterCrop = true)
        title3.text = "image3.load(url, roundRadius = 20)"

        image1.click {
//            Share.shareWithUI(activity!!, SharePlatform.WxCircle)
//            WebActivity.start(
//                    title = "xxxx",
//                    url = "https://player.youku.com/embed/XNDI1MTY2MTYwMA==?client_id=fe317d1cbae86c63&password=&autoplay=true#www.wu888.cn",
//                    rightIconRes = R.mipmap.ic_launcher, rightIconClickAction = {
//                ToastUtils.showShort("点击了")
//            })

//            ImagePicker.startCamera(this, 1) //打开相机
//            ImagePicker.startCamera(this, 1, isCompress = false) //打开相机不压缩
//            ImagePicker.startCamera(this, 1, isCrop = true) //打开相机并裁剪
//            ImagePicker.startCamera(this, 1) //打开相机不裁剪
            ImagePicker.startPicker(this, 1, isCrop = true) //打开相机并裁剪
//            ImagePicker.startPicker(this, 1, isCrop = true) //打开相机并裁剪
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
        if(requestCode==1&& resultCode==-1){
            val url = ImagePicker.fetchResult(data)
            image1.load(url[0])
//            val len = FileUtils.getFileLength(url[0])
//            LogUtils.e("拍照返回：${url}   大小：${len/1024}k")
//            PlayerActivity.start(url = url[0])
        }
    }
}