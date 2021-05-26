package com.lxj.androidktx.player

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginTop
import com.blankj.utilcode.util.ScreenUtils
import com.lxj.androidktx.R
import com.lxj.androidktx.core.margin
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.player.VideoView
import xyz.doikki.videoplayer.util.PlayerUtils

class CustomTitleView : FrameLayout, IControlComponent {
    private var mControlWrapper: ControlWrapper? = null
    private var mTitle: TextView? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    init {
        visibility = GONE
        LayoutInflater.from(context).inflate(R.layout._ktx_player_titleview, this, true)
        if(ScreenUtils.isLandscape()) getChildAt(0).margin(topMargin = 0)
        val btnBack = findViewById<ImageView>(R.id.ivBack)
        btnBack.setOnClickListener {
            PlayerUtils.scanForActivity(context)?.onBackPressed()
        }
        mTitle = findViewById(R.id.tvTitle)
    }
    fun setTitle(title: String?) {
        mTitle!!.text = title
    }

    override fun attach(controlWrapper: ControlWrapper) {
        mControlWrapper = controlWrapper
    }

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
        if (isVisible) {
            if (visibility == GONE) {
                visibility = VISIBLE
                if (anim != null) {
                    startAnimation(anim)
                }
            }
        } else {
            if (visibility == VISIBLE) {
                visibility = GONE
                if (anim != null) {
                    startAnimation(anim)
                }
            }
        }
    }

    override fun onPlayStateChanged(playState: Int) {
        when (playState) {
            VideoView.STATE_IDLE, VideoView.STATE_START_ABORT, VideoView.STATE_PREPARING, VideoView.STATE_PREPARED, VideoView.STATE_ERROR, VideoView.STATE_PLAYBACK_COMPLETED -> visibility =
                GONE
        }
    }

    override fun onPlayerStateChanged(playerState: Int) {}
    override fun setProgress(duration: Int, position: Int) {}
    override fun onLockStateChanged(isLocked: Boolean) {
        if (isLocked) {
            visibility = GONE
        } else {
            visibility = VISIBLE
        }
    }

}
