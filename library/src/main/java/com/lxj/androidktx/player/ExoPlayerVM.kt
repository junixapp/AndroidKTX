package com.lxj.androidktx.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.danikula.videocache.HttpProxyCacheServer
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.core.getObject
import com.lxj.androidktx.core.putObject
import com.lxj.androidktx.core.putString
import com.lxj.androidktx.core.sp
import com.lxj.androidktx.livedata.StateLiveData
import java.io.File
import kotlin.random.Random


/**
 * ExoPlayer实现的播放器封装
 */
object ExoPlayerVM : ViewModel(){
    val RandomMode = "RandomMode" //随机播放
    val RepeatOneMode = "RepeatOneMode" //单曲循环播放
    val RepeatAllMode = "RepeatAllMode" //顺序循环播放

    private var autoPlayNext = true
    private val handler = Handler(Looper.getMainLooper())
    var player :SimpleExoPlayer
    var playMode = StateLiveData<String>()
    val playState = StateLiveData<PlayState>()
    var currentIndex = -1
    val playInfo = StateLiveData<PlayInfo>() //播放进度, 位置
    val uriList = arrayListOf<String>()
    var isCacheLastData = false
    private val proxy: HttpProxyCacheServer by lazy { HttpProxyCacheServer.Builder(AndroidKTX.context)
        .cacheDirectory(File(AndroidKTX.context.externalCacheDir, "exoplayer-cache"))
        .build() }
    private val maxCacheSize = 1024*1024L*100 //100M
    init {
        playState.value = PlayState.Idle
        playMode.value = sp().getString("_ktx_player_mode", RepeatAllMode) ?: RepeatAllMode

        val cacheDir = File(AndroidKTX.context.externalCacheDir, "exoplayer-cache")
        player = SimpleExoPlayer.Builder(AndroidKTX.context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(CacheDataSource.Factory()
                .setUpstreamDataSourceFactory(DefaultDataSourceFactory(AndroidKTX.context,"AndroidKtx ExoPlayer"))
                .setCache(SimpleCache(cacheDir, LeastRecentlyUsedCacheEvictor(maxCacheSize)))))
            .build()
        player.repeatMode = Player.REPEAT_MODE_OFF
        player.shuffleModeEnabled = false
//        if(AndroidKTX.isDebug)player.addAnalyticsListener( EventLogger(DefaultTrackSelector()))
        player.addListener(object : Player.EventListener{
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
//                LogUtils.e("onIsPlayingChanged:  isPlaying: ${isPlaying} state: ${player.playbackState}" +
//                        " playWhenReady : ${player.playWhenReady}")
                if(isPlaying){
                    playState.postValueAndSuccess(PlayState.Playing)
                    postProgress()
                }else{
                    if(player.playbackState==Player.STATE_READY && !player.playWhenReady){
                        //暂停
                        playState.postValueAndSuccess(PlayState.Pause)
                        stopPostProgress()
                    }
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                when(state){
                    Player.STATE_IDLE -> playState.postValueAndSuccess(PlayState.Idle)
                    Player.STATE_BUFFERING -> {
                        stopPostProgress()
                        playState.postValueAndSuccess(PlayState.Buffering)
                    }
                    Player.STATE_READY -> {
                        playState.postValueAndSuccess(PlayState.Ready)
                        if(!player.playWhenReady)playState.postValueAndSuccess(PlayState.Pause)
                    }
                    Player.STATE_ENDED -> {
                        playState.postValueAndSuccess(PlayState.Complete)
                        stopPostProgress()
                        if(autoPlayNext)autoNextWhenComplete()
                    }
                }
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                LogUtils.e("onPlayerError:  ${error.localizedMessage}")
                playState.postValueAndSuccess(PlayState.Error)
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
        playInfo.postValueAndSuccess(info)
        if(isCacheLastData) sp().putObject("_last_playinfo_", info)
        handler.postDelayed({ postProgress()}, 500)
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
        playMode.postValueAndSuccess(mode)
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
//        player.seekTo(index, 0)
//        if(player.playbackState==Player.STATE_IDLE || index!= playInfo.value?.index){
//            innerPlay()
//        }
        val mediaItem: MediaItem = MediaItem.fromUri(uriList[currentIndex])
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
            total = duration(), uri = "")
        playInfo.postValueAndSuccess(info)
//        player.release()
    }
}

data class PlayInfo(
        var index: Int = -1,
        var uri: String = "",
        var current: Long = 0,
        var total: Long = 0,
)