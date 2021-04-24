package com.lxj.androidktx.base

import android.widget.ImageView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.gone
import com.lxj.androidktx.core.load
import com.lxj.androidktx.core.startActivity
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import kotlinx.android.synthetic.main._ktx_activity_player.*


/**
 * Description: 视频播放界面
 * Create by dance, at 2019/8/15
 */
class PlayerActivity : AdaptActivity(){

    companion object{
        fun start(url: String, title: String = "", cover: String = ""){
            AndroidKTX.context.startActivity<PlayerActivity>(bundle = arrayOf(
                    "title" to title, "url" to url, "cover" to cover
            ))
        }
    }

    override fun getLayoutId() = R.layout._ktx_activity_player

    override fun initData() {
        var url = intent.getStringExtra("url")
        if(url.isNullOrEmpty()){
            ToastUtils.showShort("视频地址为空")
            finish()
        }
        var title = intent.getStringExtra("title")?:""
        var cover = intent.getStringExtra("cover")
        if(cover.isNullOrEmpty())cover = url
        val imageView = ImageView(this)
        imageView.load(cover)
        video_player.thumbImageView = imageView

        video_player.setVideoAllCallBack(object : GSYSampleCallBack() {
            override fun onPrepared(url: String?, vararg objects: Any?) {
                super.onPrepared(url, *objects)
                video_player.post {
                    val isRotate = video_player.gsyVideoManager.videoWidth > video_player.gsyVideoManager.videoHeight && video_player.renderProxy.rotation==0f
                            && ScreenUtils.isPortrait()
//                    LogUtils.e("isRotate:$isRotate   rotation: ${video_player.renderProxy.rotation}")
                    if(isRotate){
                        ScreenUtils.setLandscape(this@PlayerActivity)
                    }
                }
            }
        })
        video_player.setUp(url, true, title)
        video_player.startPlayLogic()
        video_player.backButton.scaleX = 1.2f
        video_player.backButton.scaleY = 1.2f
        video_player.backButton.click { finish() }
        video_player.fullscreenButton.gone()
    }

    override fun initView() {
        BarUtils.setStatusBarVisibility(this, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        video_player.release()
    }
}