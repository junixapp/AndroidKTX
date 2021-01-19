package com.lxj.androidktxdemo.fragment

import android.widget.SeekBar
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.core.click
import com.lxj.androidktx.player.ExoPlayerVM
import com.lxj.androidktx.player.PlayState
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_player.*

/**
 * Description: 播放器功能展示
 * Create by dance, at 2019/1/4
 */
class PlayerFragment : BaseFragment() {
    val list = listOf(
            "https://yiyi-admin.jiker.info/File/resources/16f19ec74e6911585c54ffc6290102dd.mp3",
            "https://yiyi-admin.jiker.info/File/resources/9e9fc77280ddc60c530543fb4c32d0b2.mp3",
            "https://yiyi-admin.jiker.info/File/resources/16f19ec74e6911585c54ffc6290102dd.mp3",
            "https://yiyi-admin.jiker.info/File/resources/9e9fc77280ddc60c530543fb4c32d0b2.mp3",
            "https://yiyi-admin.jiker.info/File/resources/16f19ec74e6911585c54ffc6290102dd.mp3",
            "https://yiyi-admin.jiker.info/File/resources/9e9fc77280ddc60c530543fb4c32d0b2.mp3",
    )
    override fun getLayoutId() = R.layout.fragment_player
    var first = true
    override fun initView() {
        ExoPlayerVM.init()
        ExoPlayerVM.bindList(list)

        ExoPlayerVM.playMode.observe(this, Observer {
            tvMode.text = "当前播放模式：${it}"
        }, true)

        ExoPlayerVM.playState.observe(this, {
            tvState.text = "当前播放状态: ${it}"
            btnPlay.text = when(it){
                PlayState.Ready -> "播放"
                PlayState.Playing -> "暂停"
                PlayState.Complete -> "重新播放"
                PlayState.Buffering -> "缓冲中"
                PlayState.Error -> "播放出错"
                PlayState.Pause -> "播放"
                else -> "播放"
            }
        }, true)

        ExoPlayerVM.playInfo.observe(this, Observer {
            seekBar.max = it.total.toInt()
            seekBar.progress = it.current.toInt()
            tvIndex.text = "当前播放的是第${it.index}个"
            tvUrl.text = "当前播放地址：${it.uri}"
        }, true)

        btnPlay.click {
            ExoPlayerVM.toggle()
        }

        btnSwitchMode.click {
            ExoPlayerVM.autoSwitchPlayMode()
        }
        btnPlayOne.click {
            ExoPlayerVM.play(0)
        }
        btnPlayList.click {
            ExoPlayerVM.playList(list)
        }
        btnPre.click {
            ExoPlayerVM.previous()
        }

        btnNext.click {
            ExoPlayerVM.next()
        }
        btnPlayByIndex.click {
            ExoPlayerVM.play(1)
        }
        btnForbiddenMode.click {
            ExoPlayerVM.disableRepeatMode()
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                ExoPlayerVM.seekTo(seekBar.progress.toLong())
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ExoPlayerVM.release()
    }

}


