package com.lxj.videoplayer

import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videoplayer.player.AbstractPlayer
import xyz.doikki.videoplayer.player.VideoView

class CustomVideoView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : VideoView(context, attributeSet, defStyleAttr){

    var onVideoSizeChange: ((Int,Int)->Unit)? = null


    override fun onVideoSizeChanged(videoWidth: Int, videoHeight: Int) {
        super.onVideoSizeChanged(videoWidth, videoHeight)
        onVideoSizeChange?.invoke(videoWidth, videoHeight)
    }

}