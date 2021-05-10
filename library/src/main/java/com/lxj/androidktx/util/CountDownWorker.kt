package com.lxj.androidktx.util

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * 精准倒计时实现，如果传入了LifecycleOwner，将自动cancel
 * 系统的CountDownTimer以时间戳作为任务值，容易有误差
 */
class CountDownWorker(var total: Int = 60, var step: Int = 1, var countDownInterval: Long = 1000,
                      var immediately: Boolean = true,
    var onChange: ((s: Int)->Unit)? = null,  var onFinish: (()->Unit)? = null) : LifecycleObserver{
    private var mCancelled = false
    private var steps = 0
    private val what = 1
    fun start(owner: LifecycleOwner? = null){
        owner?.lifecycle?.addObserver(this)
        mHandler.removeMessages(what)
        steps = 0
        if(immediately) onChange?.invoke(total)
        mHandler.sendEmptyMessageDelayed(what, countDownInterval)
    }

    fun cancel(){
        mCancelled = true
        mHandler.removeMessages(what)
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    fun onUIDestroy(){
        cancel()
    }

    private val mHandler: Handler = @SuppressLint("HandlerLeak") object : Handler() {
        override fun handleMessage(msg: Message) {
            synchronized(this@CountDownWorker) {
                if (mCancelled) return
                if(steps>=total){
                    onFinish?.invoke()
                }else{
                    steps += step
                    onChange?.invoke(total - steps)
                    sendEmptyMessageDelayed(what, countDownInterval)
                }
            }
        }
    }
}