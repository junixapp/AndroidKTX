package com.lxj.androidktx.player

import xyz.doikki.videoplayer.player.VideoView

/**
 * 设置raw视频资源
 */
fun VideoView<*>.setRawRes(rawId: Int){
    setUrl("android.resource://" + context!!.packageName + "/" + rawId)
}