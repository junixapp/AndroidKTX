package com.lxj.androidktx.base

import android.content.Intent
import android.view.ViewGroup
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.AndroidKtxConfig
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import kotlinx.android.synthetic.main._ktx_activity_player.*

/**
 * Description:
 * Create by dance, at 2019/8/15
 */
class PlayerActivity : AdaptActivity(){

    companion object{
        fun start(url:String, title: String = ""){
            val intent = Intent(AndroidKtxConfig.context, PlayerActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("url", url)
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
        video_player.setUp(url, true, title)
        video_player.startAfterPrepared()

        video_player.backButton.scaleX = 1.2f
        video_player.backButton.scaleY = 1.2f
        (video_player.backButton.parent as ViewGroup).translationY = AdaptScreenUtils.pt2Px(10f)*1f
        video_player.backButton.click { finish() }

    }

    override fun initView() {
    }

    override fun onDestroy() {
        super.onDestroy()
        video_player.release()
    }
}