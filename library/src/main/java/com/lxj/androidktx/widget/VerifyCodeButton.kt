package com.lxj.androidktx.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import com.lxj.androidktx.R

/**
 * Description:
 * Create by dance, at 2019/6/9
 */
/**
 * Description: 发送验证码View，功能如下：
 * 1. 显示默认文字：如发送验证码
 * 2. 执行倒计时功能
 * 3. 显示倒计时文字，如：60秒后重发
 * 4. 显示重发文字：如重新发送验证码
 * Create by dance, at 2019/6/9
 */
class VerifyCodeButton @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShapeTextView(context, attributeSet, defStyleAttr){

    var defText = "发送验证码" //默认文字
    var countDownText = "秒后重新发送" //执行倒计时期间的文字，前面会拼上时间
    var resendText = "重新发送验证码" //重新发送的文字
    var resendDuration = 60 //重新发送的时间周期，默认是60秒
    var currTime = 0 //当前时间
    var alphaWhenCountDown = true //在倒计时的时候是否显示半透明
    var callback: CountDownCallback? = null
    var status = VerifyStatus.Init
    val mHandler = Handler()
    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.VerifyCodeButton)
        defText = ta.getString(R.styleable.VerifyCodeButton_vcb_defText) ?: defText
        countDownText = ta.getString(R.styleable.VerifyCodeButton_vcb_countDownText) ?: countDownText
        resendText = ta.getString(R.styleable.VerifyCodeButton_vcb_resendText) ?: resendText
        resendDuration = ta.getInt(R.styleable.VerifyCodeButton_vcb_resendDuration, resendDuration)
        alphaWhenCountDown = ta.getBoolean(R.styleable.VerifyCodeButton_vcb_alphaWhenCountDown, alphaWhenCountDown)
        ta.recycle()
        currTime = resendDuration
        text = defText
    }

    //开始倒计时
    fun start(){
        if(currTime==0){
            animate().alpha(1f).setDuration(300).setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    callback?.onEnd()
                }
            }).start()
            isEnabled = true
            text = resendText
            currTime = resendDuration
            mHandler.removeCallbacksAndMessages(null)
            status = VerifyStatus.End
            return
        }
        if(alphaWhenCountDown) animate().alpha(.6f).setDuration(300).start()
        isEnabled = false
        if(countDownText.contains("time")){
            text = countDownText.replace("time", "$currTime")
        }else{
            text = "${currTime}${countDownText}"
        }
        currTime--
        mHandler.postDelayed({start()}, 1000)
        callback?.onStart()
        status = VerifyStatus.CountingDown
    }

    /**
     * 结束倒计时
     */
    fun stop(){
        animate().cancel()
        mHandler.removeCallbacksAndMessages(null)
        text = defText
        currTime = resendDuration
        callback?.onStop()
        status = VerifyStatus.End
        isEnabled = true
        alpha = 1f
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

    interface CountDownCallback{
        fun onStart(){}
        fun onStop(){}
        fun onEnd(){}
    }
}

enum class VerifyStatus{
    Init, CountingDown, End
}