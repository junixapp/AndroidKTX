package com.lxj.androidktx.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.danikula.videocache.CacheListener
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.core.getObject
import com.lxj.androidktx.core.putObject
import com.lxj.androidktx.core.putString
import com.lxj.androidktx.core.sp
import com.lxj.androidktx.livedata.StateLiveData
import java.io.File
import kotlin.random.Random


/**
 * ExoPlayer实现的播放器封装，带缓存
 */
object ExoPlayerManager : CacheListener{
    val RandomMode = "RandomMode" //随机播放
    val RepeatOneMode = "RepeatOneMode" //单曲循环播放
    val RepeatAllMode = "RepeatAllMode" //顺序循环播放

    private var autoPlayNext = false
    private val handler = Handler(Looper.getMainLooper())
    var player :SimpleExoPlayer
    var playMode = StateLiveData<String>()  //播放模式
    val playState = StateLiveData<PlayState>() //播放状态
    val cacheInfo = StateLiveData<CacheInfo>() //缓存状态
    var currentIndex = -1
    val playInfo = StateLiveData<PlayInfo>() //播放进度, 位置
    val uriList = arrayListOf<String>()
    var isCacheLastData = false
    init {
        playState.value = PlayState.Idle
        playMode.value = sp().getString("_ktx_player_mode", RepeatAllMode) ?: RepeatAllMode

        player = SimpleExoPlayer.Builder(AndroidKTX.context).build()
        player.repeatMode = Player.REPEAT_MODE_OFF
        player.shuffleModeEnabled = false
        player.addListener(object : Player.Listener{
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if(isPlaying){
                    playState.setValue(PlayState.Playing)
                    postProgress()
                }else{
                    if(player.playbackState==Player.STATE_READY && !player.playWhenReady){
                        //暂停
                        playState.setValue(PlayState.Pause)
                        stopPostProgress()
                    }
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                when(state){
                    // This is the initial state, the state when the player is stopped, and when playback failed.
                    // The player will hold only limited resources in this state.
                    Player.STATE_IDLE -> playState.setValue(PlayState.Idle)
                    Player.STATE_BUFFERING -> {
                        // The player is not able to immediately play from its current position.
                        // This mostly happens because more data needs to be loaded.
                        playState.errMsg = null
                        stopPostProgress()
                        playState.setValue(PlayState.Buffering)
                    }
                    Player.STATE_READY -> {
                        //The player is able to immediately play from its current position
                        playState.errMsg = null
                        playState.setValue(PlayState.Ready)
                        if(!player.playWhenReady)playState.setValue(PlayState.Pause)
                    }
                    Player.STATE_ENDED -> {
                        //The player finished playing all media.
                        playState.errMsg = null
                        playState.setValue(PlayState.Complete)
                        stopPostProgress()
                        if(autoPlayNext)autoNextWhenComplete()
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                LogUtils.e("onPlayerError:  ${error.localizedMessage}")
                playState.errMsg = "${error.errorCode}"
                playState.setValue(PlayState.Error)
                stopPostProgress()
            }
        })
    }


    fun cacheLastData(b: Boolean) {
        this.isCacheLastData = b
        playInfo.value = if(isCacheLastData) sp().getObject<PlayInfo>("_last_playinfo_")?: PlayInfo() else PlayInfo()
        currentIndex = playInfo.value?.index ?: -1
    }

    /**
     * 绑定列表不播放
     * @param list 地址列表
     */
    fun bindList(list: List<String>){
        uriList.clear()
        uriList.addAll(list)
    }

    /**
     * 设置列表并播放第一个
     */
    fun playList(list: List<String>){
        bindList(list)
        play(0)
    }

    fun currentUri() = if(isIndexOrListWrong()) "" else uriList[currentIndex]

    private fun isIndexOrListWrong() = currentIndex<0 || currentIndex>=uriList.size

    private fun postProgress(){
        if(isIndexOrListWrong()) return
        val info = PlayInfo(index = currentIndex, current = currentPosition(),
                total = duration(), uri = uriList[currentIndex])
        playInfo.setValue(info)
        if(isCacheLastData) sp().putObject("_last_playinfo_", info)
        handler.postDelayed({ postProgress()}, 1000)
    }

    private fun stopPostProgress(){
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * 是否自动播放下一个，默认为true
     */
    fun autoPlayNext(b: Boolean){
        autoPlayNext = b
    }

    /**
     * 设置播放模式
     */
    fun setPlayMode(mode: String){
        playMode.setValue(mode)
        sp().putString("_ktx_player_mode", mode)
    }

    /**
     * 切换播放模式
     */
    fun autoSwitchPlayMode(){
        when(playMode.value){
            RepeatAllMode -> {  //切换到单曲循环
                setPlayMode(RepeatOneMode)
            }
            RepeatOneMode -> {  //切换到随机循环
                setPlayMode(RandomMode)
            }
            RandomMode -> {  //切换到顺序循环
                setPlayMode(RepeatAllMode)
            }
            else->{
                setPlayMode(RepeatAllMode)
            }
        }
    }

    fun hasPrevious() = !isIndexOrListWrong() && currentIndex>0
    fun hasNext() = !isIndexOrListWrong() && currentIndex<(uriList.size-1)
    fun previous() {
        if(isIndexOrListWrong()) return
        when(playMode.value){
            RandomMode -> {
                currentIndex = Random.Default.nextInt(uriList.size)
            }
            else -> {
                if(currentIndex== 0) currentIndex = uriList.lastIndex
                else currentIndex -= 1
            }
        }
        play(currentIndex)
    }

    fun next(){
        if(isIndexOrListWrong()) return
        when(playMode.value){
            RandomMode -> {
                currentIndex = Random.Default.nextInt(uriList.size)
            }
            else -> {
                if(currentIndex== uriList.lastIndex) currentIndex = 0
                else currentIndex += 1
            }
        }
        play(currentIndex)
    }

    /**
     * 自动播放下一个
     */
    fun autoNextWhenComplete(){
        if(isIndexOrListWrong()) return
        when(playMode.value){
            RandomMode -> {
                currentIndex = Random.Default.nextInt(uriList.size)
            }
            RepeatAllMode -> {
                if(currentIndex== uriList.lastIndex) currentIndex = 0
                else currentIndex += 1
            }
        }
        play(currentIndex)
    }

    fun isPlaying() = player.isPlaying
    fun duration() = player.duration
    fun currentPosition() = player.currentPosition

    /**
     * 播放指定位置，必须在bindList()之后调用，否则无效
     */
    fun play(index: Int){
        currentIndex = index
        if(isIndexOrListWrong())return

        //开启缓存
        val proxy = ProxyMediaCacheManager.getProxy()
        val url = uriList[currentIndex]
        proxy.unregisterCacheListener(this)
        proxy.registerCacheListener(this, url)
        val mediaItem: MediaItem = MediaItem.fromUri(proxy.getProxyUrl(url))
        player.setMediaItem(mediaItem)
        player.stop()
        player.prepare()
        player.play()
    }


    fun seekTo(position: Long){
        player.seekTo(position)
    }

    fun resume(){
        if(player.playbackState==Player.STATE_IDLE){
            //闲置
            play(currentIndex)
        }else{
            if(playState.value==PlayState.Complete){
                player.seekTo(0)
            }
            player.play()
        }
    }

    /**
     * 自动切换播放和暂停
     * @param autoPlayFirst 当没有指定索引时，自动播放第1个
     */
    fun toggle(autoPlayFirst: Boolean = true){
        if(autoPlayFirst && currentIndex<0){
            play(0)
        }else{
            if(isIndexOrListWrong())return
            if(isPlaying()) pause()
            else resume()
        }
    }
    fun pause(){
        player.pause()
    }

    fun stop(){
        stopPostProgress()
        player.stop()
    }

    fun release(){
        stop()
        currentIndex = -1
        uriList.clear()
        val info = PlayInfo(index = currentIndex, current = 0,
            total = 0, uri = "")
        playInfo.setValue(info)
    }

    override fun onCacheAvailable(cacheFile: File, url: String, percentsAvailable: Int) {
        cacheInfo.postValueAndSuccess(CacheInfo(cacheFile, url, percentsAvailable))
    }

}

data class PlayInfo(
        var index: Int = -1,
        var uri: String = "",
        var current: Long = 0,
        var total: Long = 0,
)

data class CacheInfo(var cacheFile: File, var url: String, var percent: Int)