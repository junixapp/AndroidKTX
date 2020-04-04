package com.lxj.androidktxdemo.fragment

import android.graphics.BitmapFactory
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.base.WebActivity
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.load
import com.lxj.androidktx.share.Share
import com.lxj.androidktx.share.SharePlatform
import com.lxj.androidktxdemo.BuildConfig
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_imageview_ext.*

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
class ImageViewExtPage: BaseFragment(){
    private val images = arrayOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544028336053&di=76f9c9280955ba7151c05f548d985c59&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F472309f790529822f7c1a2e8dcca7bcb0a46d42f.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544028336056&di=a120afc7cfb3708de4f7a22741901b20&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F908fa0ec08fa513d946d2de5366d55fbb3fbd9c2.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544028336055&di=e51bbe4f5b67266d644c8f650950df9c&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D3b5bbe029ddda144ce0464f1dadebad7%2Fac345982b2b7d0a23b781e8cc1ef76094b369a69.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544028336055&di=f3c3a95d264cbf33841127370aa0b018&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F6f061d950a7b0208bf8b59c969d9f2d3572cc85c.jpg"
    )
    override fun getLayoutId() = R.layout.fragment_imageview_ext

    override fun initView() {
        loadImage()
    }

    private fun loadImage(){
        image1.load(images[0], placeholder = R.mipmap.ic_launcher)
        title1.text = "image1.load(url)"


        image2.load(images[1], placeholder = R.mipmap.ic_launcher, isCircle = true)
        title2.text = "image2.load(url, isCircle = true)"

        image3.load(images[2], placeholder = R.mipmap.ic_launcher, roundRadius = 20)
        title3.text = "image3.load(url, roundRadius = 20)"

        image1.click {
//            Share.shareWithUI(activity!!, SharePlatform.WxCircle)
            WebActivity.start( url = "http://www.jd.com", rightIconRes = R.mipmap.ic_launcher, rightIconClickAction = {
                ToastUtils.showShort("点击了")
            })
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
}