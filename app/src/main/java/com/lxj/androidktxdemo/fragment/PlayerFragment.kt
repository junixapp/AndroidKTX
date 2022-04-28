package com.lxj.androidktxdemo.fragment

import android.widget.SeekBar
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.core.click
import com.lxj.androidktx.player.ExoPlayerManager
import com.lxj.androidktx.player.PlayState
import com.lxj.androidktx.player.ProxyMediaCacheManager
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_player.*

/**
 * Description: 播放器功能展示
 * Create by dance, at 2019/1/4
 */
class PlayerFragment : BaseFragment() {
    val list = listOf(
            "https://zigtone.com/wp-content/uploads/2020/06/Loud-Music.mp3",
            "https://zigtone.com/wp-content/uploads/2021/01/Copines.mp3",
            "https://zigtone.com/wp-content/uploads/2020/06/Harry-Potter-Theme.mp3",
    )
    override fun getLayoutId() = R.layout.fragment_player
    override fun initView() {
        ExoPlayerManager.bindList(list)

        ExoPlayerManager.playMode.observe(this, Observer {
            tvMode.text = "当前播放模式：${it}"
        }, true)

        ExoPlayerManager.playState.observe(this, {
            tvState.text = "当前播放状态: ${it}"
            btnPlay.text = when(it){
                PlayState.Ready -> "播放"
                PlayState.Playing -> "暂停"
                PlayState.Complete -> "重新播放"
                PlayState.Buffering -> "缓冲中"
                PlayState.Error -> {
                    ToastUtils.showShort("播放出错")
                    "播放出错"
                }
                PlayState.Pause -> "播放"
                else -> "播放"
            }
        }, true)

        ExoPlayerManager.playInfo.observe(this, Observer {
            seekBar.max = it.total.toInt()
            seekBar.progress = it.current.toInt()
            tvIndex.text = "当前播放的是第${it.index}个"
            tvUrl.text = "当前播放地址：${it.uri}"
        }, true)

        ExoPlayerManager.cacheInfo.observe(this, Observer {
            tvCache.text = " percent: ${it.percent} file: ${it.cacheFile.absolutePath}  url: ${it.url}"
        }, true)

        btnPlay.click {
            ExoPlayerManager.toggle()
        }

        btnSwitchMode.click {
            ExoPlayerManager.autoSwitchPlayMode()
        }
        btnPlayOne.click {
            ExoPlayerManager.play(0)
        }
        btnPlayList.click {
            ExoPlayerManager.playList(list)
        }
        btnPre.click {
            ExoPlayerManager.previous()
        }

        btnNext.click {
            ExoPlayerManager.next()
        }
        btnPlayByIndex.click {
            ExoPlayerManager.play(1)
        }
        btnForbiddenMode.click {
            ExoPlayerManager.autoPlayNext(false)
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                ExoPlayerManager.seekTo(seekBar.progress.toLong())
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ExoPlayerManager.release()
    }

}


