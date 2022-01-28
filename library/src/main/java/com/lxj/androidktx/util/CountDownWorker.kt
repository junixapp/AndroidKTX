package com.lxj.androidktx.util

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.lxj.androidktx.livedata.LifecycleHandler

/**
 * 相对倒计时实现，保证每步任务都能执行,如果传入了LifecycleOwner，将自动cancel，无需调用cancel。
 * 系统的CountDownTimer以时间戳作为任务值，容易有误差，但是以绝对倒计时为准，时间更准确。
 * @param total 总步数
 * @param step 步长
 * @param countDownInterval 递减时间间隔
 * @param immediately 是否立即执行onChange，false的话会间隔一个countDownInterval再执行onChange
 * @param onChange 递减回调
 * @param onCancel 取消回调
 * @param onFinish 倒计时结束回调
 */
class CountDownWorker(var owner: LifecycleOwner,
                      var total: Int = 60, var step: Int = 1, var countDownInterval: Long = 1000,
                      var immediately: Boolean = true, var from : Int = 0,
                      var onChange: ((s: Int)->Unit)? = null,
                      var onCancel: ((s: Int)->Unit)? = null,
                      var onFinish: (()->Unit)? = null) : LifecycleObserver{

    init {
        owner.lifecycle.addObserver(this)
    }

    private var mCancelled = false
    private var steps = from
    private val what = 1

    fun start(){
        mHandler.removeMessages(what)
        mCancelled = false
        steps = from
        if(immediately) onChange?.invoke(total)
        mHandler.sendEmptyMessageDelayed(what, countDownInterval)
    }

    fun cancel(){
        onCancel?.invoke(steps)
        mCancelled = true
        mHandler.removeMessages(what)
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    fun onUIDestroy(){
        cancel()
    }

    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : LifecycleHandler(owner) {
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