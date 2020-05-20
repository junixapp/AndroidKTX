package com.lxj.androidktx.base

import android.content.Intent
import android.widget.ImageView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.AndroidKtxConfig
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.gone
import com.lxj.androidktx.core.load
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.utils.GSYVideoType.SCREEN_TYPE_FULL
import kotlinx.android.synthetic.main._ktx_activity_player.*


/**
 * Description:
 * Create by dance, at 2019/8/15
 */
class PlayerActivity : AdaptActivity(){

    companion object{
        fun start(url: String, title: String = "", cover: String = ""){
            val intent = Intent(AndroidKtxConfig.context, PlayerActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            intent.putExtra("cover", cover)
            AndroidKtxConfig.context.startActivity(intent)
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
        var cover = intent.getStringExtra("cover")?:""
        if(!cover.isNullOrEmpty()){
            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.load(cover)
            video_player.thumbImageView = imageView
        }
        GSYVideoType.setShowType(SCREEN_TYPE_FULL)
        video_player.setUp(url, true, title)
        video_player.startPlayLogic()
        video_player.backButton.scaleX = 1.2f
        video_player.backButton.scaleY = 1.2f
//        (video_player.backButton.parent as ViewGroup).translationY = AdaptScreenUtils.pt2Px(10f)*1f
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