package com.lxj.androidktx.livedata

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * Description: 自动在UI销毁时移除msg和任务的Handler，不会有内存泄露
 * Create by dance, at 2019/5/21
 */
class LifecycleHandler(private val lifecycleOwner: LifecycleOwner?,
                       looper: Looper = Looper.getMainLooper())
    : Handler(looper), LifecycleObserver {
    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        removeCallbacksAndMessages(null)
        lifecycleOwner?.lifecycle?.removeObserver(this)
    }
}