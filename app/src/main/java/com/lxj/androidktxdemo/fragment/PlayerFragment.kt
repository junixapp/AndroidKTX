package com.lxj.androidktxdemo.fragment

import android.graphics.Color
import android.media.RingtoneManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.*
import com.lxj.androidktx.core.*
import com.lxj.audioplayer.ExoPlayerManager
import com.lxj.audioplayer.PlayState
import com.lxj.androidktxdemo.R
import com.lxj.androidktxdemo.RingtoneTool
import kotlinx.android.synthetic.main.fragment_player.*

/**
 * Description: 播放器功能展示
 * Create by dance, at 2019/1/4
 */
class PlayerFragment : BaseFragment() {
    val list = listOf(
            "http://54.151.127.160/downloads/radio_welcome_en.mp3",
            "http://54.151.127.160/downloads/radio_exit_en.mp3",
            "https://zigtone.com/wp-content/uploads/2020/06/Loud-Music1.mp3",
            "https://zigtone.com/wp-content/uploads/2021/01/Copines.mp3",
            "https://zigtone.com/wp-content/uploads/2020/06/Harry-Potter-Theme.mp3",
            "https://zigtone.com/wp-content/uploads/2020/06/Sorry-Darling.mp3",
            "https://zigtone.com/wp-content/uploads/2021/01/Got-The-Man-With-The-Plan-Right-Here.mp3",
            "https://zigtone.com/wp-content/uploads/2020/10/Positions.mp3",
            "https://zigtone.com/wp-content/uploads/2021/08/iPhone-12-Pro-Max.mp3"
    )
    override fun getLayoutId() = R.layout.fragment_player

    fun findPosition(uri: String): Int{
        return list.indexOfFirst { it == uri }
    }
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
            val position = findPosition(ExoPlayerManager.currentUri())
            if(position>=0){
                rvMusic.adapter?.notifyItemChanged(position)
            }
        }, true)

        ExoPlayerManager.playInfo.observe(this, Observer {
            seekBar.max = it.total.toInt()
            seekBar.progress = it.current.toInt()
            tvIndex.text = "当前播放的是第${it.index}个"
            tvUrl.text = "当前播放地址：${it.uri}"
            val position = findPosition(ExoPlayerManager.currentUri())
            if(position>=0){
                rvMusic.adapter?.notifyItemChanged(position)
            }
            if(it.lastUri.isNotEmpty() && it.lastUri!=it.uri){
                //change uri
                val lastposition = findPosition(it.lastUri)
                if(position>=0){
                    rvMusic.adapter?.notifyItemChanged(lastposition)
                }
            }
            LogUtils.e(it.toJson())
        }, true)

        ExoPlayerManager.cacheInfo.observe(this, Observer {
            tvCache.text = " percent: ${it.percent} file: ${it.cacheFile.absolutePath}  url: ${it.url}"
        }, true)


        btnRingtone.click {
            PermissionUtils.requestWriteSettings(object : PermissionUtils.SimpleCallback{
                override fun onGranted() {

                    val t = ExoPlayerManager.currentUri()

                    val title = t.substring(t.lastIndexOf("/")+1)
                    val cacheFile = ExoPlayerManager.cacheFile(ExoPlayerManager.currentUri())
                    RingtoneTool.setRingtone(cacheFile.absolutePath, title, ringtoneType = RingtoneManager.TYPE_ALARM)
                }

                override fun onDenied() {
                }

            })


        }

        btnPlay.click {
            ExoPlayerManager.toggle()

//            PermissionUtils.requestWriteSettings(object : PermissionUtils.SimpleCallback{
//                override fun onGranted() {
//                    val cacheFile = ExoPlayerManager.cacheFile(list[0])
//
//                    //copy to Ringtones dir
//                    val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES), AndroidKTX.context.string(R.string.app_name))
//                    if(!dir.exists()) dir.mkdirs()
//                    val newPath = "${dir}/${cacheFile.name}"
//                    val layout = FileUtils.copy(cacheFile.absolutePath, newPath)
//                    LogUtils.e("copy file layout: $layout  $newPath")
//
//                    RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE,
//                    UriUtils.file2Uri(File(newPath)))
////
//
////                    Settings.System.putString(
////                        AndroidKTX.context.contentResolver, Settings.System.RINGTONE,
////                        UriUtils.file2Uri(cacheFile).toString())
//                }
//
//                override fun onDenied() {
//                }
//
//            })
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
        inList()
    }

    fun inList(){
        rvMusic.disableItemAnimation()
        rvMusic.vertical()
            .divider(color = Color.parseColor("#cccccc"))
            .bindData(list, R.layout.adapter_music_list, bindFn = {holder, t, position ->
                if(ExoPlayerManager.isBuffering() && ExoPlayerManager.currentUri()==t){
                    holder.getView<ProgressBar>(R.id.progressBar).visible()
                    holder.getView<Button>(R.id.btnPlay).gone()
                }else{
                    holder.getView<ProgressBar>(R.id.progressBar).gone()
                    holder.getView<Button>(R.id.btnPlay).visible()
                }

                val title = t.substring(t.lastIndexOf("/")+1)
                if(ExoPlayerManager.currentUri()!=t || !ExoPlayerManager.isPlaying()){
                    holder.getView<Button>(R.id.btnPlay).setText("播放")
                    holder.getView<TextView>(R.id.title).text = title
                }else{
                    holder.getView<Button>(R.id.btnPlay).setText("暂停")
                    val value = ExoPlayerManager.playInfo.value!!
                    holder.getView<TextView>(R.id.title).text = "${title} - ${ NumberUtils.format((value.current*100f)/value.total, 1) }%"
                }

                holder.getView<Button>(R.id.btnPlay).click {
                    if(ExoPlayerManager.currentUri()!=t){
                        ExoPlayerManager.playSingle(t)
                    }else{
                        ExoPlayerManager.toggle()
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        ExoPlayerManager.release()
    }

}


