package com.lxj.androidktx.util

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import com.lxj.androidktx.livedata.StateLiveData

enum class PlayState{
    Idle, Playing, Pause
}

/**
 * 音频播放
 */
object AudioPlayer : ViewModel(){

    var mUrl = ""
    var mediaPlayer: MediaPlayer? = null
    val state = StateLiveData<PlayState>()

    init {
        state.value = PlayState.Idle
    }

    fun play(url: String){
        if(mediaPlayer!=null){
            if(mUrl==url){
                //正在播放同一个资源
                if(isPlaying()){
                    return
                }else{
                    mediaPlayer?.start()
                    state.postValueAndSuccess(PlayState.Playing)
                }
            }else{
                mediaPlayer?.stop()
                mediaPlayer = null
                state.postValueAndSuccess(PlayState.Idle)
                playInternal(url)
            }
        }else{
            playInternal(url)
        }
    }

    fun isPlaying() = state.value==PlayState.Playing

    fun pause(){
        mediaPlayer?.pause()
        state.postValueAndSuccess(PlayState.Pause)
    }

    private fun playInternal(url: String){
        mUrl = url
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.setDataSource(mUrl)
        mediaPlayer?.setOnPreparedListener {
            mediaPlayer?.start()
            state.postValueAndSuccess(PlayState.Playing)
        }
        mediaPlayer?.setOnCompletionListener {
            state.postValueAndSuccess(PlayState.Idle)
        }
        mediaPlayer?.prepareAsync()
    }

    fun stop(){
        mediaPlayer?.stop()
        mUrl = ""
        state.postValueAndSuccess(PlayState.Idle)
    }

    fun release(){
        stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}