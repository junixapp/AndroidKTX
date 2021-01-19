package com.lxj.androidktx.player

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.lxj.androidktx.livedata.StateLiveData


/**
 * 原生MediaPlayer实现的音频播放
 */
@Deprecated(message = "请使用更强大的ExoPlayerVM")
object AudioPlayer : ViewModel(){

    var mUrl = ""
    var mediaPlayer: MediaPlayer? = null
    val state = StateLiveData<PlayState>()
    val handler = Handler(Looper.getMainLooper())

    init {
        state.value = PlayState.Idle
    }

    fun play(url: String){
        if(mediaPlayer !=null){
            if(mUrl ==url){
                //正在播放同一个资源
                if(isPlaying()){
                    return
                }else{
                    mediaPlayer?.start()
                    handler.removeCallbacksAndMessages(null)
                    postPlaying()
                }
            }else{
                stop()
                playInternal(url)
            }
        }else{
            playInternal(url)
        }
    }

    fun isPlaying() = state.value== PlayState.Playing

    fun pause(){
        if(mediaPlayer?.isPlaying==true){
            mediaPlayer?.pause()
            handler.removeCallbacksAndMessages(null)
            state.postValueAndSuccess(PlayState.Pause)
        }
    }

    private fun playInternal(url: String){
        mUrl = url
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.setDataSource(mUrl)
        mediaPlayer?.setOnPreparedListener {
            mediaPlayer?.start()
            handler.removeCallbacksAndMessages(null)
            postPlaying()
        }
        mediaPlayer?.setOnCompletionListener {
            handler.removeCallbacksAndMessages(null)
            state.postValueAndSuccess(PlayState.Complete)
            state.postValueAndSuccess(PlayState.Idle)
        }
        mediaPlayer?.prepareAsync()
    }
    
    fun duration() = mediaPlayer?.duration
    fun currentPosition() = mediaPlayer?.currentPosition

    fun postPlaying(){
        state.postValueAndSuccess(PlayState.Playing)
        handler.postDelayed({ postPlaying() }, 1000)
    }

    fun stop(){
        if(state.value==PlayState.Idle)return
        mediaPlayer?.stop()
        mUrl = ""
        handler.removeCallbacksAndMessages(null)
        state.value = PlayState.Idle
    }

    fun release(){
        stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}