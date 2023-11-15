package com.lxj.videoplayer

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main._ktx_activity_player.*
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.*
import xyz.doikki.videoplayer.player.VideoView
import xyz.doikki.videoplayer.util.PlayerUtils
import java.lang.Exception


/**
 * Description: 视频播放界面
 * Create by dance, at 2019/8/15
 */
class VideoPlayerActivity : AppCompatActivity() {
    companion object {
        /**
         * 开启视频播放器界面
         * @param url 视频文件地址，如果是raw资源，则"android.resource://" + packageName + "/" + R.raw.test
         */
        fun start( url: String, title: String = "", cover: String = "") {
            val intent = Intent()
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            intent.putExtra("cover", cover)
            ActivityUtils.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._ktx_activity_player)
        initView()
        initData()
    }


    val mController: StandardVideoController by lazy { StandardVideoController(this) }

     fun initView() {
        BarUtils.setStatusBarVisibility(this, false)
        adaptCutoutAboveAndroidP();
    }

     fun initData() {
        val url = intent.getStringExtra("url")
        if (url.isNullOrEmpty()) {
            ToastUtils.showShort("视频地址为空")
            finish()
        }
        val title = intent.getStringExtra("title") ?: ""
        val cover = intent.getStringExtra("cover")

        videoView.setUrl(url)
        videoView.setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT)
        mController.addControlComponent(CompleteView(this))
        mController.addControlComponent(ErrorView(this))

        //封面处理
        val prepareView = PrepareView(this)
        val thumb = prepareView.findViewById<ImageView>(xyz.doikki.videocontroller.R.id.thumb)
        thumb.scaleType = ImageView.ScaleType.FIT_CENTER
        if (cover.isNullOrEmpty()) {
            //主动获取首帧图片
            try {
                val isNetUri = url?.startsWith("http")?:false
                val retriever = MediaMetadataRetriever()
                if (isNetUri) retriever.setDataSource(url, HashMap<String, String>())
                else retriever.setDataSource(url)
                val bitmap = retriever.getFrameAtTime()
                thumb.setImageBitmap(bitmap)
            } catch (e: Exception) {
            }
        } else {
            //传入了封面图片
            Glide.with(thumb).load(cover).into(thumb)
        }
        mController.addControlComponent(prepareView)

        //标题处理
        val titleView = CustomTitleView(this)
        titleView.setTitle(title)
        mController.addControlComponent(titleView)

        val vodControlView = VodControlView(this)
        vodControlView.findViewById<View>(R.id.fullscreen).visibility = View.GONE
        val lp =
            vodControlView.findViewById<View>(R.id.total_time).layoutParams as LinearLayout.LayoutParams
        lp.rightMargin = PlayerUtils.dp2px(this, 16f)
        mController.addControlComponent(vodControlView)
        mController.addControlComponent(GestureView(this))
        videoView.setVideoController(mController)
        videoView.onVideoSizeChange = { w, h ->
            if (w != 0 && h != 0) {
                if (w > h) ScreenUtils.setLandscape(this)
            }
        }
        videoView.start()
    }

    private fun adaptCutoutAboveAndroidP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
    }

    override fun onBackPressed() {
        if (!mController.isLocked) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.release()
    }
}